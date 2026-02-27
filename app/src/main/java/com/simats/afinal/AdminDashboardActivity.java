package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Top Left Back Button -> Back to Login
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        updateAdminStats();

        // Total Staff Card Click
        findViewById(R.id.card_total_staff).setOnClickListener(v -> {
            startActivity(new Intent(this, ManageDoctorsActivity.class));
        });

        // Navigation logic
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> {}); // Already here
        
        findViewById(R.id.nav_staff_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, ManageDoctorsActivity.class));
        });

        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, PatientDatabaseActivity.class));
        });

        findViewById(R.id.nav_logs_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, SystemActivityLogsActivity.class));
        });

        findViewById(R.id.nav_more_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminSettingsActivity.class));
        });
    }

    private void updateAdminStats() {
        TextView tvStaff = findViewById(R.id.tv_total_doctors);
        TextView tvPatients = findViewById(R.id.tv_total_patients);
        TextView tvScans = findViewById(R.id.tv_ai_scans);
        TextView tvPending = findViewById(R.id.tv_pending_approvals);

        if (tvStaff != null) {
            int staffCount = DoctorDataManager.getAllDoctors().size();
            tvStaff.setText(String.valueOf(staffCount));
        }

        if (tvPatients != null) {
            int patientCount = PatientDataManager.getAllPatients().size();
            tvPatients.setText(String.valueOf(patientCount));
        }

        if (tvScans != null) {
            int scansToday = XrayReportManager.getScansToday(this);
            tvScans.setText(String.valueOf(scansToday));
        }

        if (tvPending != null) {
            int pending = XrayReportManager.getReportCount(this);
            tvPending.setText(String.valueOf(pending));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAdminStats();
    }
}
