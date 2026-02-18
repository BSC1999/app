package com.simats.afinal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<PatientInfo> appointmentList;
    private Context context;

    public AppointmentAdapter(Context context, List<PatientInfo> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PatientInfo patient = appointmentList.get(position);
        holder.tvName.setText(patient.getName());
        
        // Fixed slot display - no more editing
        String slotInfo = (patient.getNextScheduleTime() != null && !patient.getNextScheduleTime().isEmpty()) 
                        ? patient.getNextScheduleTime() 
                        : "Slot not fixed";
        holder.tvDetails.setText(slotInfo + " | " + patient.getComplaint());

        if (patient.isFemale()) {
            holder.ivIcon.setImageResource(R.drawable.img1001);
        } else {
            holder.ivIcon.setImageResource(R.drawable.img1000);
        }

        // Role-based visibility for Doctor Name
        String role = UserManager.getCurrentRole();
        if ("Consultant".equalsIgnoreCase(role) || "Intern".equalsIgnoreCase(role)) {
            holder.tvDrName.setVisibility(View.VISIBLE);
            holder.tvDrName.setText("Dr. " + (patient.getAssignedDoctor() != null ? patient.getAssignedDoctor() : "General"));
        } else {
            holder.tvDrName.setVisibility(View.GONE);
        }

        // Open Profile on Click
        View.OnClickListener openProfileListener = v -> {
            Intent intent = new Intent(context, PatientProfileActivity.class);
            intent.putExtra("patient_data", patient);
            context.startActivity(intent);
        };
        holder.itemView.setOnClickListener(openProfileListener);
        holder.tvName.setOnClickListener(openProfileListener);
        holder.ivIcon.setOnClickListener(openProfileListener);

        // Status Mark on Long Click
        holder.ivIcon.setOnLongClickListener(v -> {
            Toast.makeText(context, "Patient Appointment Status: Scheduled", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails, tvDrName;
        ImageView ivStatusCheck;
        com.google.android.material.imageview.ShapeableImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_patient_name);
            tvDrName = itemView.findViewById(R.id.tv_doctor_name);
            tvDetails = itemView.findViewById(R.id.tv_appointment_details);
            ivIcon = itemView.findViewById(R.id.iv_patient_icon);
            ivStatusCheck = itemView.findViewById(R.id.iv_status_check);
        }
    }
}