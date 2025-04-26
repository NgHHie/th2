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
import android.widget.TextView;

import com.example.th2.model.ProjectAdapter;
import com.example.th2.model.Project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements ProjectAdapter.ItemListener {
    private TextView txtFromDate, txtToDate;
    private CheckBox cbDone;
    private Button btnFilter, btnAll, btnAdd;
    private RecyclerView rvList;

    private ProjectAdapter adapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private TextView txtErrStart, txtErrEnd, txtErrRange;
    private static int lastId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        init();
        initRv();
        initListener();
    }

    private void init() {
        txtFromDate = findViewById(R.id.txtFromDate);
        txtToDate = findViewById(R.id.txtToDate);
        cbDone = findViewById(R.id.cbDone);
        btnFilter = findViewById(R.id.btnFilter);
        btnAll = findViewById(R.id.btnAll);
        btnAdd = findViewById(R.id.btnAdd);
        rvList = findViewById(R.id.rvList);

        txtErrStart = findViewById(R.id.txtErrStart);
        txtErrEnd = findViewById(R.id.txtErrEnd);
        txtErrRange = findViewById(R.id.txtErrRange);
    }

    private void initRv() {
        adapter = new ProjectAdapter(this);
        adapter.setListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvList.setLayoutManager(manager);
        rvList.setAdapter(adapter);

        // Add sample data
        adapter.setList(getSampleData());
    }

    private void initListener() {
        txtFromDate.setOnClickListener(v -> showDatePicker(txtFromDate));
        txtToDate.setOnClickListener(v -> showDatePicker(txtToDate));

        btnFilter.setOnClickListener(v -> {
            boolean valid = true;

            txtErrStart.setVisibility(View.GONE);
            txtErrEnd.setVisibility(View.GONE);
            txtErrRange.setVisibility(View.GONE);

            String startStr = txtFromDate.getText().toString();
            if (startStr.isEmpty()) {
                txtErrStart.setVisibility(View.VISIBLE);
                valid = false;
            }

            String endStr = txtToDate.getText().toString();
            if (endStr.isEmpty()) {
                txtErrEnd.setVisibility(View.VISIBLE);
                valid = false;
            }

            if (!valid) {
                return;
            }

            try {
                Date start = sdf.parse(startStr);
                Date end = sdf.parse(endStr);

                if (start.after(end)) {
                    txtErrRange.setVisibility(View.VISIBLE);
                    return;
                }

                Boolean done = cbDone.isChecked() ? true : null;
                adapter.filter(start, end, done);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        btnAll.setOnClickListener(v -> {
            txtErrStart.setVisibility(View.GONE);
            txtErrEnd.setVisibility(View.GONE);
            txtErrRange.setVisibility(View.GONE);
            cbDone.setChecked(false);
            txtFromDate.setText(null);
            txtToDate.setText(null);
            adapter.filter(null, null, null);
        });

        btnAdd.setOnClickListener(v -> showDialog(null, -1));
    }

    private void showDatePicker(TextView tv) {
        Calendar cal = Calendar.getInstance();
        try {
            Date date = sdf.parse(tv.getText().toString());
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(
                this,
                (view, y, m, d) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(y, m, d);
                    String selectedDate = sdf.format(selectedCal.getTime());
                    tv.setText(selectedDate);
                },
                year, month, day);

        dpd.show();
    }

    private void showDialog(Project p, int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set dialog title
        String title = p == null ? "Thêm dự án mới" : "Cập nhật dự án";
        builder.setTitle(title);

        // Inflate dialog layout
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_project_form, null);
        builder.setView(view);

        // Find views
        TextView txtId = view.findViewById(R.id.txtId);
        EditText edtName = view.findViewById(R.id.edtName);
        TextView txtStart = view.findViewById(R.id.txtStart);
        TextView txtEnd = view.findViewById(R.id.txtEnd);
        CheckBox cbDone = view.findViewById(R.id.cbDone);
        TextView txtErrName = view.findViewById(R.id.txtErrName);
        TextView txtErrDate = view.findViewById(R.id.txtErrDate);

        // Generate ID for new project
        final int nextId = p == null ? lastId + 1 : -1;
        final String pid;

        if (p == null) {
            int id = nextId > 99 ? 0 : nextId;
            pid = String.format("%02d", id);
        } else {
            pid = p.getId();
        }
        txtId.setText(pid);

        // Set default dates for new project
        Calendar cal = Calendar.getInstance();
        cal.set(2025, 0, 1); // January 1, 2025
        String startDate = sdf.format(cal.getTime());

        cal.set(2025, 11, 31); // December 31, 2025
        String endDate = sdf.format(cal.getTime());

        if (p != null) {
            edtName.setText(p.getName());
            txtStart.setText(sdf.format(p.getStart()));
            txtEnd.setText(sdf.format(p.getEnd()));
            cbDone.setChecked(p.isDone());
        } else {
            txtStart.setText(startDate);
            txtEnd.setText(endDate);
        }

        // Setup date pickers
        txtStart.setOnClickListener(v -> showDatePicker(txtStart));
        txtEnd.setOnClickListener(v -> showDatePicker(txtEnd));

        // Create dialog buttons
        builder.setPositiveButton(p == null ? "Thêm" : "Cập nhật", null);

        if (p != null) {
            builder.setNeutralButton("Xóa", (dialog, which) -> {
                confirmDel(pos);
            });
        }

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Override positive button to validate before closing
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            boolean valid = true;

            // Validate name
            String name = edtName.getText().toString().trim();
            if (name.isEmpty()) {
                txtErrName.setVisibility(View.VISIBLE);
                valid = false;
            } else {
                txtErrName.setVisibility(View.GONE);
            }

            // Validate dates
            try {
                Date start = sdf.parse(txtStart.getText().toString());
                Date end = sdf.parse(txtEnd.getText().toString());

                if (start.after(end)) {
                    txtErrDate.setVisibility(View.VISIBLE);
                    valid = false;
                } else {
                    txtErrDate.setVisibility(View.GONE);
                }

                if (valid) {
                    boolean done = cbDone.isChecked();

                    // Only increment ID when adding new project
                    if (p == null) {
                        lastId = nextId > 99 ? 10 : nextId;
                    }

                    Project newP = new Project(pid, name, start, end, done);

                    if (p == null) {
                        // Add new project
                        adapter.add(newP);
                    } else {
                        // Update existing project
                        adapter.update(pos, newP);
                    }

                    dialog.dismiss();
                }

            } catch (ParseException e) {
                txtErrDate.setText("Lỗi định dạng ngày");
                txtErrDate.setVisibility(View.VISIBLE);
                valid = false;
            }
        });
    }

    private void confirmDel(int pos) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa dự án này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    adapter.remove(pos);
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public void onItemClick(int pos) {
        Project p = adapter.getItem(pos);
        showDialog(p, pos);
    }

    private List<Project> getSampleData() {
        List<Project> samples = new ArrayList<>();

        try {
            // Sample 1
            Calendar cal1 = Calendar.getInstance();
            cal1.set(2025, 1, 15); // February 15, 2025
            Date start1 = cal1.getTime();

            cal1.set(2025, 3, 30); // April 30, 2025
            Date end1 = cal1.getTime();

            lastId = 1; // Set starting ID
            Project p1 = new Project(String.format("%02d", lastId), "Nấu cơm cho vợ", start1, end1, true);
            samples.add(p1);

            // Sample 2
            lastId++;
            Calendar cal2 = Calendar.getInstance();
            cal2.set(2025, 5, 1); // June 1, 2025
            Date start2 = cal2.getTime();

            cal2.set(2025, 8, 30); // September 30, 2025
            Date end2 = cal2.getTime();

            Project p2 = new Project(String.format("%02d", lastId), "Dự án website", start2, end2, false);
            samples.add(p2);

            // Sample 3
            lastId++;
            Calendar cal3 = Calendar.getInstance();
            cal3.set(2025, 3, 15); // April 15, 2025
            Date start3 = cal3.getTime();

            cal3.set(2025, 11, 31); // December 31, 2025
            Date end3 = cal3.getTime();

            Project p3 = new Project(String.format("%02d", lastId), "Ứng dụng di động", start3, end3, false);
            samples.add(p3);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return samples;
    }
}