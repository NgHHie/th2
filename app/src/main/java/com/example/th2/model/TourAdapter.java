package com.example.th2.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.th2.R;

import java.util.ArrayList;
import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {
    private Context context;
    private List<Tour> tourList;
    private TourItemListener listener;

    public TourAdapter(Context context) {
        this.context = context;
        this.tourList = new ArrayList<>();
    }

    public void setTourList(List<Tour> tourList) {
        this.tourList = tourList;
        notifyDataSetChanged();
    }

    public Tour getTour(int position) {
        return tourList.get(position);
    }

    public interface TourItemListener {
        void onItemClick(View view, int position);
    }

    public void setTourItemListener(TourItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tourList.get(position);

        holder.imgPhuongTien.setImageResource(tour.getImage());
        holder.txtTuyen.setText(tour.getTuyen());
        holder.txtTime.setText(tour.getTime());
        holder.txtPhuongTien.setText(tour.getPhuongtien());

        holder.cardView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void add(Tour tour) {
        tourList.add(tour);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(int position, Tour tour) {
        tourList.set(position, tour);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void remove(int position) {
        tourList.remove(position);
        notifyDataSetChanged();
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhuongTien;
        private TextView txtTuyen, txtTime, txtPhuongTien;
        private CardView cardView;

        public TourViewHolder(@NonNull View view) {
            super(view);
            imgPhuongTien = view.findViewById(R.id.imgPt);
            txtTuyen = view.findViewById(R.id.txtTuyen);
            txtTime = view.findViewById(R.id.txtTime);
            txtPhuongTien = view.findViewById(R.id.txtPt);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}