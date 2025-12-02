package com.example.workmanaging.view.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workmanaging.R;
import com.example.workmanaging.model.entity.JobStatus;
import com.example.workmanaging.model.entity.Progetto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<Progetto> projects = new ArrayList<>();
    private OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Progetto progetto);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setProjects(List<Progetto> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Progetto currentProject = projects.get(position);
        holder.tvTitle.setText(currentProject.titolo);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String start = currentProject.inizio != null ? sdf.format(currentProject.inizio) : "-";
        String end = currentProject.scadenza != null ? sdf.format(currentProject.scadenza) : "-";
        holder.tvDates.setText(String.format("%s - %s", start, end));

        JobStatus status = calculateStatus(currentProject.inizio, currentProject.scadenza);
        
        int colorRes;
        switch (status) {
            case COMPLETATO:
                colorRes = R.color.status_completed;
                break;
            case IN_CORSO:
                colorRes = R.color.status_in_progress;
                break;
            case NON_INIZIATO:
            default:
                colorRes = R.color.status_not_started;
                break;
        }
        holder.statusIndicator.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentProject);
            }
        });
    }

    private JobStatus calculateStatus(Date start, Date end) {
        Date now = new Date();
        if (end != null && now.after(end)) {
            return JobStatus.COMPLETATO;
        } else if (start != null && now.after(start)) {
            return JobStatus.IN_CORSO;
        } else {
            return JobStatus.NON_INIZIATO;
        }
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDates;
        private View statusIndicator;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_project_title);
            tvDates = itemView.findViewById(R.id.tv_project_dates);
            statusIndicator = itemView.findViewById(R.id.status_indicator);
        }
    }
}
