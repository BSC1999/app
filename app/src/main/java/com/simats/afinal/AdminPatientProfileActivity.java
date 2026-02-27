package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.imageview.ShapeableImageView;

public class AdminPatientProfileActivity extends AppCompatActivity {

    private PatientInfo currentPatient;
    private TextView tvName, tvId, tvAgeGender, tvAssignedDoc, tvMedHistory, tvLastTreatment;
    private ShapeableImageView ivProfilePic;
    private GridLayout glXrayReports;
    private TextView tabMed, tabXray, tabAi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_patient_profile);

        // Views
        ivProfilePic = findViewById(R.id.iv_profile_pic);
        tvName = findViewById(R.id.tv_profile_name);
        tvId = findViewById(R.id.tv_profile_id);
        tvAgeGender = findViewById(R.id.tv_profile_age_gender);
        tvAssignedDoc = findViewById(R.id.tv_assigned_doctor);
        tvMedHistory = findViewById(R.id.tv_display_medical_history);
        tvLastTreatment = findViewById(R.id.tv_last_treatment_date);
        glXrayReports = findViewById(R.id.gl_xray_reports);
        
        tabMed = findViewById(R.id.tab_medical_history);
        tabXray = findViewById(R.id.tab_xray_reports);
        tabAi = findViewById(R.id.tab_ai_analysis);

        // Get Data
        currentPatient = (PatientInfo) getIntent().getSerializableExtra("patient_data");

        if (currentPatient != null) {
            updateUI();
        }

        // Back button redirection to Patient Database
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            startActivity(new Intent(this, PatientDatabaseActivity.class));
            finish();
        });

        // Tab Clicks
        tabMed.setOnClickListener(v -> switchTab("med"));
        tabXray.setOnClickListener(v -> switchTab("xray"));
        tabAi.setOnClickListener(v -> switchTab("ai"));

        // Bottom Nav Logic
        setupBottomNav();
    }

    private void updateUI() {
        tvName.setText(currentPatient.getName());
        tvId.setText("#PT-" + currentPatient.getId());
        tvAgeGender.setText(currentPatient.getAge() + " / " + (currentPatient.getGender() != null ? currentPatient.getGender() : "N/A"));
        tvAssignedDoc.setText("Assigned: " + (currentPatient.getAssignedDoctor() != null ? currentPatient.getAssignedDoctor() : "Unassigned"));
        
        if (currentPatient.isFemale()) {
            ivProfilePic.setImageResource(R.drawable.img1001);
        } else {
            ivProfilePic.setImageResource(R.drawable.img1000);
        }

        tvMedHistory.setText(currentPatient.getMedicalHistory() != null && !currentPatient.getMedicalHistory().isEmpty() ? 
                currentPatient.getMedicalHistory() : "No history recorded");

        if (currentPatient.getNextScheduleDate() != null && !currentPatient.getNextScheduleDate().isEmpty()) {
            tvLastTreatment.setText(currentPatient.getNextScheduleDate());
        } else {
            tvLastTreatment.setText("Not available");
        }

        loadXrayReports();
    }

    private void switchTab(String tab) {
        findViewById(R.id.cv_medical_summary).setVisibility(tab.equals("med") ? View.VISIBLE : View.GONE);
        glXrayReports.setVisibility(tab.equals("xray") ? View.VISIBLE : View.GONE);
        
        tabMed.setTextColor(getResources().getColor(tab.equals("med") ? R.color.nav_icon_selected : R.color.nav_icon_unselected));
        tabXray.setTextColor(getResources().getColor(tab.equals("xray") ? R.color.nav_icon_selected : R.color.nav_icon_unselected));
        tabAi.setTextColor(getResources().getColor(tab.equals("ai") ? R.color.nav_icon_selected : R.color.nav_icon_unselected));
    }

    private void loadXrayReports() {
        glXrayReports.removeAllViews();
        int[] patientImages = {R.drawable.img_23, R.drawable.img_24};
        
        for (int resId : patientImages) {
            View card = LayoutInflater.from(this).inflate(R.layout.item_upload_card, glXrayReports, false);
            ImageView iv = card.findViewById(R.id.iv_item_upload);
            iv.setImageResource(resId);
            
            iv.setOnClickListener(v -> {
                Intent intent = new Intent(this, XrayActivity.class);
                intent.putExtra("selected_image", resId);
                intent.putExtra("from_patient_profile", true);
                intent.putExtra("patient_data", currentPatient);
                startActivity(intent);
            });
            glXrayReports.addView(card);
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, AdminDashboardActivity.class)); finish(); });
        findViewById(R.id.nav_staff_custom).setOnClickListener(v -> { startActivity(new Intent(this, ManageDoctorsActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientDatabaseActivity.class)); finish(); });
        findViewById(R.id.nav_logs_custom).setOnClickListener(v -> { startActivity(new Intent(this, SystemActivityLogsActivity.class)); finish(); });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, AdminSettingsActivity.class)); finish(); });
    }
}
