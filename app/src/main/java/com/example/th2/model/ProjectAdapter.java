package com.example.th2.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.th2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private Context context;
    private List<Project> list;
    private List<Project> origList;
    private ItemListener listener;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public ProjectAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        this.origList = new ArrayList<>();
    }

    public void setListener(ItemListener listener) {
        this.listener = listener;
    }

    public void setList(List<Project> list) {
        this.list = list;
        this.origList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public List<Project> getList() {
        return list;
    }

    public Project getItem(int pos) {
        return list.get(pos);
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int pos) {
        Project p = list.get(pos);

        holder.txtName.setText(p.getName());
        holder.txtStart.setText("từ " + sdf.format(p.getStart()));
        holder.txtEnd.setText("đến " + sdf.format(p.getEnd()));
        holder.cbDone.setChecked(p.isDone());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(Project p) {
        list.add(p);
        origList.add(p);
        notifyDataSetChanged();
    }

    public void update(int pos, Project p) {
        list.set(pos, p);
        for (int i = 0; i < origList.size(); i++) {
            if (origList.get(i).getId().equals(p.getId())) {
                origList.set(i, p);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        Project removed = list.remove(pos);
        for (int i = 0; i < origList.size(); i++) {
            if (origList.get(i).getId().equals(removed.getId())) {
                origList.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void filter(Date start, Date end, Boolean done) {
        list.clear();

        for (Project p : origList) {
            boolean match = true;

            // Filter by date range
            if (start != null && end != null) {
                if (p.getStart().before(start) || p.getStart().after(end)) {
                    match = false;
                }
            }

            // Filter by completion status
            if (done != null) {
                if (p.isDone() != done) {
                    match = false;
                }
            }

            if (match) {
                list.add(p);
            }
        }

        notifyDataSetChanged();
    }

    public interface ItemListener {
        void onItemClick(int pos);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtStart, txtEnd;
        private CheckBox cbDone;

        public ProjectViewHolder(@NonNull View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtStart = view.findViewById(R.id.txtStart);
            txtEnd = view.findViewById(R.id.txtEnd);
            cbDone = view.findViewById(R.id.cbDone);
        }
    }
}