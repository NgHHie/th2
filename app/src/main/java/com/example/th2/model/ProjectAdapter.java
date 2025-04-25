package com.example.th2.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.th2.R;
import com.example.th2.model.Project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private Context context;
    private List<Project> projects;
    private List<Project> originalProjects;
    private ProjectItemListener itemListener;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public ProjectAdapter(Context context) {
        this.context = context;
        this.projects = new ArrayList<>();
        this.originalProjects = new ArrayList<>();
    }

    public void setItemListener(ProjectItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
        this.originalProjects = new ArrayList<>(projects);
        notifyDataSetChanged();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Project getItem(int position) {
        return projects.get(position);
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projects.get(position);

        holder.tvProjectName.setText(project.getName());
        holder.tvItemDateStart.setText("từ " + sdf.format(project.getStartDate()));
        holder.tvItemDateEnd.setText("đến " + sdf.format(project.getEndDate()));
        holder.cbCompleted.setChecked(project.isCompleted());

        holder.itemView.setOnClickListener(v -> {
            if (itemListener != null) {
                itemListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public void add(Project project) {
        projects.add(project);
        originalProjects.add(project);
        notifyDataSetChanged();
    }

    public void update(int position, Project project) {
        projects.set(position, project);
        // Also update in original list
        for (int i = 0; i < originalProjects.size(); i++) {
            if (originalProjects.get(i).getId().equals(project.getId())) {
                originalProjects.set(i, project);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        Project removed = projects.remove(position);
        // Also remove from original list
        for (int i = 0; i < originalProjects.size(); i++) {
            if (originalProjects.get(i).getId().equals(removed.getId())) {
                originalProjects.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void filterProjects(Date startDate, Date endDate, Boolean completionStatus) {
        projects.clear();

        for (Project project : originalProjects) {
            boolean matches = true;

            // Filter by date range - check if project's start date is within the range
            if (startDate != null && endDate != null) {
                if (project.getStartDate().before(startDate) || project.getStartDate().after(endDate)) {
                    matches = false;
                }
            }

            // Filter by completion status if specified
            if (completionStatus != null) {
                if (project.isCompleted() != completionStatus) {
                    matches = false;
                }
            }

            if (matches) {
                projects.add(project);
            }
        }

        notifyDataSetChanged();
    }

    public interface ProjectItemListener {
        void onItemClick(int position);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProjectName, tvItemDateStart, tvItemDateEnd;
        private CheckBox cbCompleted;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tvItemProjectName);
            tvItemDateStart = itemView.findViewById(R.id.tvItemDateStart);
            tvItemDateEnd = itemView.findViewById(R.id.tvItemDateEnd);
            cbCompleted = itemView.findViewById(R.id.cbItemCompleted);
        }
    }
}