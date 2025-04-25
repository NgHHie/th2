package com.example.th2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Spinner spPt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spPt = findViewById(R.id.spPt);

        String[] arr = {"Xe may", "O to", "May bay"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.phuong_tien, arr) {
            private View getCustomView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.phuong_tien, parent, false);

                TextView tvText = view.findViewById(R.id.txtPt);
                ImageView imgIcon = view.findViewById(R.id.imgPt);

                String item = getItem(position);
                tvText.setText(item);

                // Gán ảnh theo text
                if ("Xe may".equals(item)) {
                    imgIcon.setImageResource(R.drawable.xemay);
                } else if ("O to".equals(item)) {
                    imgIcon.setImageResource(R.drawable.oto);
                } else if ("May bay".equals(item)) {
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
}