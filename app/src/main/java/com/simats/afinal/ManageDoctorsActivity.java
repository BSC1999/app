package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ManageDoctorsActivity extends AppCompatActivity {

    private LinearLayout llStaffListContainer;
    private TextView tvDocCount, tvConsultantCount, tvAssistantCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_doctors);

        llStaffListContainer = findViewById(R.id.ll_doctors_list_container);
        tvDocCount = findViewById(R.id.tv_doctor_count);
        tvConsultantCount = findViewById(R.id.tv_consultant_count);
        tvAssistantCount = findViewById(R.id.tv_assistant_count);

        // Top Left Back Button -> Back to Admin Dashboard
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Setup Add Buttons for each role
        View.OnClickListener addStaffListener = v -> {
            startActivity(new Intent(this, CreateDoctorActivity.class));
        };
        findViewById(R.id.btn_add_doctor_short).setOnClickListener(addStaffListener);
        findViewById(R.id.btn_add_consultant_short).setOnClickListener(addStaffListener);
        findViewById(R.id.btn_add_assistant_short).setOnClickListener(addStaffListener);

        refreshStaffData();

        // Bottom Navigation
        setupBottomNav();
    }

    private void refreshStaffData() {
        List<DoctorInfo> allStaff = DoctorDataManager.getAllDoctors();
        int docs = 0, consultants = 0, assistants = 0;

        llStaffListContainer.removeAllViews();

        for (DoctorInfo staff : allStaff) {
            // Update counts
            if (staff.getRole().equals("Dental Doctor")) docs++;
            else if (staff.getRole().equals("Consultant")) consultants++;
            else if (staff.getRole().contains("Intern") || staff.getRole().contains("Assistant")) assistants++;

            // Add to list
            addStaffRow(staff);
        }

        tvDocCount.setText(String.valueOf(docs));
        tvConsultantCount.setText(String.valueOf(consultants));
        tvAssistantCount.setText(String.valueOf(assistants));
    }

    private void addStaffRow(DoctorInfo staff) {
        View row = LayoutInflater.from(this).inflate(R.layout.item_system_log_row, llStaffListContainer, false);
        TextView tvName = row.findViewById(R.id.tv_log_user_name);
        TextView tvRole = row.findViewById(R.id.tv_log_action);
        TextView tvStatus = row.findViewById(R.id.tv_log_status_name);
        TextView tvDateTime = row.findViewById(R.id.tv_log_datetime);

        tvName.setText(staff.getName());
        tvRole.setText(staff.getRole());
        tvStatus.setText(staff.getStatus());
        if (tvDateTime != null) tvDateTime.setVisibility(View.GONE); // Hide timestamp for staff list
        
        // Status color logic
        if (staff.getStatus().equalsIgnoreCase("Active")) {
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        llStaffListContainer.addView(row);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStaffData();
    }

    private void setupBottomNav() {
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminDashboardActivity.class));
            finish();
        });
        findViewById(R.id.nav_staff_custom).setOnClickListener(v -> {}); // Already here
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, PatientDatabaseActivity.class));
            finish();
        });
        findViewById(R.id.nav_logs_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, SystemActivityLogsActivity.class));
            finish();
        });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminSettingsActivity.class));
            finish();
        });
    }
}
