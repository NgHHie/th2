package com.example.th2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.th2.model.ProjectAdapter;
import com.example.th2.model.Project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity implements ProjectAdapter.ProjectItemListener {
    private TextView tvFilterStartDate, tvFilterEndDate;
    private CheckBox cbFilterCompleted;
    private Button btnFilter, btnShowAll, btnAddNew;
    private RecyclerView rvProjects;

    private ProjectAdapter adapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private TextView tvErrorStartDate, tvErrorEndDate, tvErrorDateRange;
    private static int lastProjectId = 0; // Starting ID (will be incremented)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initViews();
        initRecyclerView();
        initListeners();
    }

    private void initViews() {
        tvFilterStartDate = findViewById(R.id.tvFilterStartDate);
        tvFilterEndDate = findViewById(R.id.tvFilterEndDate);
        cbFilterCompleted = findViewById(R.id.cbFilterCompleted);
        btnFilter = findViewById(R.id.btnFilter);
        btnShowAll = findViewById(R.id.btnShowAll);
        btnAddNew = findViewById(R.id.btnAddNew);
        rvProjects = findViewById(R.id.rvProjects);

        tvErrorStartDate = findViewById(R.id.tvErrorStartDate);
        tvErrorEndDate = findViewById(R.id.tvErrorEndDate);
        tvErrorDateRange = findViewById(R.id.tvErrorDateRange);
    }

    private void initRecyclerView() {
        adapter = new ProjectAdapter(this);
        adapter.setItemListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvProjects.setLayoutManager(manager);
        rvProjects.setAdapter(adapter);

        // Add some sample data
        adapter.setProjects(generateSampleProjects());
    }

    private void initListeners() {
        // Date picker dialogs
        tvFilterStartDate.setOnClickListener(v -> showDatePickerDialog(tvFilterStartDate));
        tvFilterEndDate.setOnClickListener(v -> showDatePickerDialog(tvFilterEndDate));

        // Filter button
        btnFilter.setOnClickListener(v -> {
            boolean isValid = true;

            // Ẩn tất cả thông báo lỗi trước khi kiểm tra
            tvErrorStartDate.setVisibility(View.GONE);
            tvErrorEndDate.setVisibility(View.GONE);
            tvErrorDateRange.setVisibility(View.GONE);

            // Kiểm tra ngày bắt đầu
            String startDateStr = tvFilterStartDate.getText().toString();
            if (startDateStr == null || startDateStr.isEmpty()) {
                tvErrorStartDate.setVisibility(View.VISIBLE);
                isValid = false;
            }

            // Kiểm tra ngày kết thúc
            String endDateStr = tvFilterEndDate.getText().toString();
            if (endDateStr == null || endDateStr.isEmpty()) {
                tvErrorEndDate.setVisibility(View.VISIBLE);
                isValid = false;
            }

            if (!isValid) {
                return; // Nếu không hợp lệ thì kết thúc xử lý
            }

            try {
                Date startDate = sdf.parse(startDateStr);
                Date endDate = sdf.parse(endDateStr);

                // Kiểm tra ngày bắt đầu < ngày kết thúc
                if (startDate.after(endDate)) {
                    tvErrorDateRange.setVisibility(View.VISIBLE);
                    return;
                }

                // Trạng thái hoàn thành
                Boolean isCompleted = cbFilterCompleted.isChecked() ? true : false;

                // Thực hiện lọc dự án
                adapter.filterProjects(startDate, endDate, isCompleted);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        // Show all button
        btnShowAll.setOnClickListener(v -> {
            tvErrorStartDate.setVisibility(View.GONE);
            tvErrorEndDate.setVisibility(View.GONE);
            tvErrorDateRange.setVisibility(View.GONE);
            cbFilterCompleted.setChecked(false);
            tvFilterStartDate.setText(null);
            tvFilterEndDate.setText(null);
            adapter.filterProjects(null, null, null);
        });

        // Add new button
        btnAddNew.setOnClickListener(v -> showProjectDialog(null, -1));
    }

    private void showDatePickerDialog(TextView targetTextView) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(targetTextView.getText().toString());
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                    String selectedDate = sdf.format(selectedCalendar.getTime());
                    targetTextView.setText(selectedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void showProjectDialog(Project project, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set dialog title based on action (add or update)
        String title = project == null ? "Thêm dự án mới" : "Cập nhật dự án";
        builder.setTitle(title);

        // Inflate dialog layout
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_project_form, null);
        builder.setView(view);

        // Find views in dialog
        TextView tvDialogProjectId = view.findViewById(R.id.tvDialogProjectId);
        EditText edtDialogProjectName = view.findViewById(R.id.edtDialogProjectName);
        TextView tvDialogStartDate = view.findViewById(R.id.tvDialogStartDate);
        TextView tvDialogEndDate = view.findViewById(R.id.tvDialogEndDate);
        CheckBox cbDialogCompleted = view.findViewById(R.id.cbDialogCompleted);
        TextView tvErrorProjectName = view.findViewById(R.id.tvErrorProjectName);
        TextView tvErrorDates = view.findViewById(R.id.tvErrorDates);

        // Generate ID for new project or use existing ID
        // Tính toán ID tiếp theo nhưng chưa gán vào lastProjectId
        final int nextId = project == null ? lastProjectId + 1 : -1;
        final String projectId;

        if (project == null) {
            int id = nextId > 99 ? 0 : nextId; // Reset to 10 if exceeds 99
            projectId = String.format("%02d", id);
        } else {
            projectId = project.getId();
        }
        tvDialogProjectId.setText(projectId);

        // Set current date for new project or use existing dates
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, 0, 1); // January 1, 2025
        String startDate = sdf.format(calendar.getTime());

        calendar.set(2025, 11, 31); // December 31, 2025
        String endDate = sdf.format(calendar.getTime());

        if (project != null) {
            edtDialogProjectName.setText(project.getName());
            tvDialogStartDate.setText(sdf.format(project.getStartDate()));
            tvDialogEndDate.setText(sdf.format(project.getEndDate()));
            cbDialogCompleted.setChecked(project.isCompleted());
        } else {
            tvDialogStartDate.setText(startDate);
            tvDialogEndDate.setText(endDate);
        }

        // Setup date pickers
        tvDialogStartDate.setOnClickListener(v -> showDatePickerDialog(tvDialogStartDate));
        tvDialogEndDate.setOnClickListener(v -> showDatePickerDialog(tvDialogEndDate));

        // Create dialog buttons
        builder.setPositiveButton(project == null ? "Thêm" : "Cập nhật", null);

        if (project != null) {
            builder.setNeutralButton("Xóa", (dialog, which) -> {
                confirmDelete(position);
            });
        }

        // Khi nhấn hủy không làm gì (ID không thay đổi)
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Override the positive button click to prevent dialog from closing if validation fails
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            boolean isValid = true;

            // Validate project name
            String name = edtDialogProjectName.getText().toString().trim();
            if (name.isEmpty()) {
                tvErrorProjectName.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                tvErrorProjectName.setVisibility(View.GONE);
            }

            // Validate dates
            try {
                Date startDateObj = sdf.parse(tvDialogStartDate.getText().toString());
                Date endDateObj = sdf.parse(tvDialogEndDate.getText().toString());

                if (startDateObj.after(endDateObj)) {
                    tvErrorDates.setVisibility(View.VISIBLE);
                    isValid = false;
                } else {
                    tvErrorDates.setVisibility(View.GONE);
                }

                if (isValid) {
                    boolean completed = cbDialogCompleted.isChecked();

                    // Chỉ tăng ID khi thêm mới thành công
                    if (project == null) {
                        // Cập nhật lastProjectId khi thêm mới thành công
                        lastProjectId = nextId > 99 ? 10 : nextId;
                    }

                    Project newOrUpdatedProject = new Project(projectId, name, startDateObj, endDateObj, completed);

                    if (project == null) {
                        // Add new project
                        adapter.add(newOrUpdatedProject);
                    } else {
                        // Update existing project
                        adapter.update(position, newOrUpdatedProject);
                    }

                    dialog.dismiss();
                }

            } catch (ParseException e) {
                tvErrorDates.setText("Lỗi định dạng ngày");
                tvErrorDates.setVisibility(View.VISIBLE);
                isValid = false;
            }
        });
    }

    private void confirmDelete(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa dự án này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    adapter.remove(position);
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public void onItemClick(int position) {
        Project selected = adapter.getItem(position);
        showProjectDialog(selected, position);
    }

    private List<Project> generateSampleProjects() {
        List<Project> sampleProjects = new ArrayList<>();

        try {
            // Sample project 1
            Calendar cal1 = Calendar.getInstance();
            cal1.set(2025, 1, 15); // February 15, 2025
            Date start1 = cal1.getTime();

            cal1.set(2025, 3, 30); // April 30, 2025
            Date end1 = cal1.getTime();

            lastProjectId = 1; // Set starting ID
            Project project1 = new Project(String.format("%02d", lastProjectId), "Nấu cơm cho vợ", start1, end1, true);
            sampleProjects.add(project1);

            // Sample project 2
            lastProjectId++;
            Calendar cal2 = Calendar.getInstance();
            cal2.set(2025, 5, 1); // June 1, 2025
            Date start2 = cal2.getTime();

            cal2.set(2025, 8, 30); // September 30, 2025
            Date end2 = cal2.getTime();

            Project project2 = new Project(String.format("%02d", lastProjectId), "Dự án website", start2, end2, false);
            sampleProjects.add(project2);

            // Sample project 3
            lastProjectId++;
            Calendar cal3 = Calendar.getInstance();
            cal3.set(2025, 3, 15); // April 15, 2025
            Date start3 = cal3.getTime();

            cal3.set(2025, 11, 31); // December 31, 2025
            Date end3 = cal3.getTime();

            Project project3 = new Project(String.format("%02d", lastProjectId), "Ứng dụng di động", start3, end3, false);
            sampleProjects.add(project3);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sampleProjects;
    }
}