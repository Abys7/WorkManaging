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
import com.example.workmanaging.model.entity.UserAction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserActionAdapter extends RecyclerView.Adapter<UserActionAdapter.ActionViewHolder> {

    private List<UserAction> actions = new ArrayList<>();
    private OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(UserAction action);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setActions(List<UserAction> actions) {
        this.actions = actions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_action, parent, false);
        return new ActionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        UserAction action = actions.get(position);
        holder.tvTitle.setText(action.title);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateStr = action.timestamp != null ? sdf.format(action.timestamp) : "";
        holder.tvSubtitle.setText(action.subtitle + " - " + dateStr);

        if (action.status != null && !action.status.isEmpty()) {
            holder.statusDot.setVisibility(View.VISIBLE);
            JobStatus status = JobStatus.valueOf(action.status);
            int colorRes;
            switch (status) {
                case COMPLETATO: colorRes = R.color.status_completed; break;
                case IN_CORSO: colorRes = R.color.status_in_progress; break;
                default: colorRes = R.color.status_not_started; break;
            }
            holder.statusDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)));
        } else {
            holder.statusDot.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    static class ActionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvSubtitle;
        private View statusDot;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_action_title);
            tvSubtitle = itemView.findViewById(R.id.tv_action_subtitle);
            statusDot = itemView.findViewById(R.id.action_status_dot);
        }
    }
}
