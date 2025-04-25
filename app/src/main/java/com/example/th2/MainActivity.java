package com.example.th2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.th2.model.Tour;
import com.example.th2.model.TourAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TourAdapter.TourItemListener {
    private EditText edtStart, edtEnd, edtTime;
    private Spinner spPt;
    private Button btnThem, btnSua, btnXoa, btnTim;
    private RecyclerView rView;
    private TourAdapter adapter;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initRecyclerView();
        initButtons();

        addSampleData();
    }

    private void init() {
        edtStart = findViewById(R.id.edtStart);
        edtEnd = findViewById(R.id.edtEnd);
        edtTime = findViewById(R.id.edtTime);
        spPt = findViewById(R.id.spPt);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        btnTim = findViewById(R.id.btnTim);
        rView = findViewById(R.id.rView);
        String[] arr = {"Xe máy", "Ô tô", "Máy bay"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.phuong_tien, arr) {
            private View getCustomView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.phuong_tien, parent, false);

                TextView tvText = view.findViewById(R.id.txtPt);
                ImageView imgIcon = view.findViewById(R.id.imgPt);

                String item = getItem(position);
                tvText.setText(item);

                if ("Xe máy".equals(item)) {
                    imgIcon.setImageResource(R.drawable.xemay);
                } else if ("Ô tô".equals(item)) {
                    imgIcon.setImageResource(R.drawable.oto);
                } else if ("Máy bay".equals(item)) {
                    imgIcon.setImageResource(R.drawable.maybay);
                }

                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return getCustomView(position, convertView, parent);
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return getCustomView(position, convertView, parent);
            }
        };

        spPt.setAdapter(adapter);
        spPt.setDropDownVerticalOffset(150);
    }


    private void initRecyclerView() {
        adapter = new TourAdapter(this);
        adapter.setTourItemListener(this);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setAdapter(adapter);
    }

    private void initButtons() {
        btnThem.setOnClickListener(v -> {
            String start = edtStart.getText().toString().trim();
            String end = edtEnd.getText().toString().trim();
            String time = edtTime.getText().toString().trim();
            String phuongTien = spPt.getSelectedItem().toString();

            if (start.isEmpty() || end.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            String tuyen = start + " - " + end;
            int image = getImgPt(phuongTien);

            Tour tour = new Tour(image, tuyen, time, phuongTien);
            adapter.add(tour);

            clearForm();
        });

        btnSua.setOnClickListener(v -> {
            if (selectedPosition == -1) {
                Toast.makeText(this, "Vui lòng chọn một tour để sửa", Toast.LENGTH_SHORT).show();
                return;
            }

            String start = edtStart.getText().toString().trim();
            String end = edtEnd.getText().toString().trim();
            String time = edtTime.getText().toString().trim();
            String phuongTien = spPt.getSelectedItem().toString();

            if (start.isEmpty() || end.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            String tuyen = start + " - " + end;
            int image = getImgPt(phuongTien);

            Tour tour = new Tour(image, tuyen, time, phuongTien);
            adapter.update(selectedPosition, tour);

            clearForm();
            selectedPosition = -1;
        });

        btnXoa.setOnClickListener(v -> {
            if (selectedPosition == -1) {
                Toast.makeText(this, "Vui lòng chọn một tour để xóa", Toast.LENGTH_SHORT).show();
                return;
            }

            adapter.remove(selectedPosition);
            clearForm();
            selectedPosition = -1;
        });

        btnTim.setOnClickListener(v -> {
            String searchText = edtStart.getText().toString().trim().toLowerCase();

            if (searchText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Tour> searchResults = new ArrayList<>();
            List<Tour> allTours = new ArrayList<>();

            // Get all tours (Assuming we have access to all tours, we'd have to adjust this)
            for (int i = 0; i < adapter.getItemCount(); i++) {
                allTours.add(adapter.getTour(i));
            }

            // Search logic
            for (Tour tour : allTours) {
                if (tour.getTuyen().toLowerCase().contains(searchText) ||
                        tour.getTime().toLowerCase().contains(searchText) ||
                        tour.getPhuongtien().toLowerCase().contains(searchText)) {
                    searchResults.add(tour);
                }
            }

            if (searchResults.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy kết quả phù hợp", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setTourList(searchResults);
            }
        });
    }

    private int getImgPt(String phuongTien) {
        if ("Xe máy".equals(phuongTien)) {
            return R.drawable.xemay;
        } else if ("Ô tô".equals(phuongTien)) {
            return R.drawable.oto;
        } else if ("Máy bay".equals(phuongTien)) {
            return R.drawable.maybay;
        }
        return R.drawable.maybay; // Default
    }

    private void clearForm() {
        edtStart.setText("");
        edtEnd.setText("");
        edtTime.setText("");
        spPt.setSelection(0);
    }

    // Sample data for demonstration
    private void addSampleData() {
        adapter.add(new Tour(R.drawable.maybay, "Hà Nội - Sapa", "3 ngày 2 đêm", "Máy bay"));
        adapter.add(new Tour(R.drawable.maybay, "Hà Nam - Sapa", "3 ngày 2 đêm", "Máy bay"));
        adapter.add(new Tour(R.drawable.oto, "Đà Nẵng - Huế", "2 ngày 1 đêm", "Ô tô"));
        adapter.add(new Tour(R.drawable.xemay, "Nha Trang - Đà Lạt", "1 ngày", "Xe máy"));
    }

    @Override
    public void onItemClick(View view, int position) {
        selectedPosition = position;
        Tour tour = adapter.getTour(position);

        String tuyen = tour.getTuyen();
        String[] parts = tuyen.split(" - ");

        if (parts.length == 2) {
            edtStart.setText(parts[0]);
            edtEnd.setText(parts[1]);
        }

        edtTime.setText(tour.getTime());

        String phuongTien = tour.getPhuongtien();
        if ("Xe máy".equals(phuongTien)) {
            spPt.setSelection(0);
        } else if ("Ô tô".equals(phuongTien)) {
            spPt.setSelection(1);
        } else if ("Máy bay".equals(phuongTien)) {
            spPt.setSelection(2);
        }
    }
}