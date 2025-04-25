package com.example.th2.model;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.th2.R;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {
    @NonNull
    @Override
    public TourAdapter.TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TourAdapter.TourViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class TourViewHolder extends RecyclerView.ViewHolder {

        public TourViewHolder(@NonNull View view) {
            super(view);
        }
    }
}
