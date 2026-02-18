package com.simats.afinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private List<LogEntry> logList;

    public LogAdapter(List<LogEntry> logList) {
        this.logList = logList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audit_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogEntry log = logList.get(position);
        holder.tvName.setText(log.getUserName() + " (" + log.getRole() + ")");
        holder.tvTime.setText(log.getTimestamp());
        holder.tvIp.setText("IP: " + log.getIpAddress());
        holder.tvActivity.setText(log.getActivity());
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvIp, tvActivity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_log_name);
            tvTime = itemView.findViewById(R.id.tv_log_time);
            tvIp = itemView.findViewById(R.id.tv_log_ip);
            tvActivity = itemView.findViewById(R.id.tv_log_activity);
        }
    }
}
