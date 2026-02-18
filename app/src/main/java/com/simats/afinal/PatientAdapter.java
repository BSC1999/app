package com.simats.afinal;

import android.app.AlertDialog;
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

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    private List<PatientInfo> patientList;

    public PatientAdapter(List<PatientInfo> patientList) {
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PatientInfo patient = patientList.get(position);
        holder.tvName.setText(patient.getName());
        holder.tvDetails.setText("ID: " + patient.getId() + " | Age: " + patient.getAge() + " | Last Visit: " + patient.getLastVisit());
        
        // Updated: Male -> img1000, Female -> img1001
        if (patient.isFemale()) {
            holder.ivIcon.setImageResource(R.drawable.img1001);
        } else {
            holder.ivIcon.setImageResource(R.drawable.img1000);
        }

        String role = UserManager.getCurrentRole();
        if ("Consultant".equalsIgnoreCase(role) || "Intern".equalsIgnoreCase(role)) {
            holder.tvDrName.setVisibility(View.VISIBLE);
            holder.tvDrName.setText("Dr. " + (patient.getAssignedDoctor() != null ? patient.getAssignedDoctor() : "General"));
        } else {
            holder.tvDrName.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PatientProfileActivity.class);
            intent.putExtra("patient_data", patient);
            v.getContext().startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Patient")
                    .setMessage("Are you sure you want to delete this patient profile?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        PatientDataManager.deletePatient(patient);
                        patientList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, patientList.size());
                        Toast.makeText(v.getContext(), "Patient deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails, tvDrName;
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_patient_name);
            tvDrName = itemView.findViewById(R.id.tv_doctor_name);
            tvDetails = itemView.findViewById(R.id.tv_patient_details);
            ivIcon = itemView.findViewById(R.id.iv_patient_icon);
        }
    }
}