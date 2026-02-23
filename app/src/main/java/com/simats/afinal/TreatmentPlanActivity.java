package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.imageview.ShapeableImageView;

public class TreatmentPlanActivity extends AppCompatActivity {

    private String selectedPlan = "";
    private String selectedTooth = "";
    private String diagnosis = "";
    private PatientInfo patientData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_plan);

        // Get data from Intent
        selectedPlan = getIntent().getStringExtra("selected_plan");
        selectedTooth = getIntent().getStringExtra("selected_tooth");
        diagnosis = getIntent().getStringExtra("selected_diagnosis");
        patientData = (PatientInfo) getIntent().getSerializableExtra("patient_data");

        setupPatientDetails();
        setupTreatmentSchedule();

        // Top Left Back Button -> Back to Timeline
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Proceed to Drug Recommendation
        findViewById(R.id.btn_proceed_drug).setOnClickListener(v -> {
            Intent intent = new Intent(this, AIDrugRecommendationActivity.class);
            intent.putExtra("selected_plan", selectedPlan);
            intent.putExtra("selected_tooth", selectedTooth);
            intent.putExtra("selected_diagnosis", diagnosis);
            intent.putExtra("patient_data", patientData);
            startActivity(intent);
        });

        // Bottom Navigation (Dashboard Style)
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { startActivity(new Intent(this, TodayAppointmentActivity.class)); finish(); });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { /* Already in AI flow */ });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    private void setupPatientDetails() {
        if (patientData == null) return;

        TextView tvName = findViewById(R.id.tv_patient_name);
        TextView tvDetails = findViewById(R.id.tv_patient_details);
        TextView tvMedHistory = findViewById(R.id.tv_medical_history_tag);
        ShapeableImageView ivProfile = findViewById(R.id.iv_patient_profile);

        tvName.setText(patientData.getName());
        tvDetails.setText(patientData.getAge() + " years old • " + (patientData.getGender() != null ? patientData.getGender() : "N/A"));
        
        if (patientData.isFemale()) {
            ivProfile.setImageResource(R.drawable.img1001);
        } else {
            ivProfile.setImageResource(R.drawable.img1000);
        }

        if (patientData.getMedicalHistory() != null && !patientData.getMedicalHistory().isEmpty()) {
            tvMedHistory.setText(patientData.getMedicalHistory().toUpperCase());
        } else {
            tvMedHistory.setVisibility(View.GONE);
        }
    }

    private void setupTreatmentSchedule() {
        LinearLayout container = findViewById(R.id.ll_visit_container);
        TextView tvTotalVisits = findViewById(R.id.tv_total_visits);
        TextView tvRecovery = findViewById(R.id.tv_recovery_days);
        TextView tvOptDesc = findViewById(R.id.tv_optimization_desc);

        container.removeAllViews();

        if ("plan1".equals(selectedPlan)) {
            // Root Canal Therapy
            tvTotalVisits.setText("2 Visits Total");
            tvRecovery.setText("8 Days");
            tvOptDesc.setText("Plan optimized to reduce recovery time by 15% based on your health profile.");

            addVisitView(container, "VISIT 1", "Surgical Phase", "60 min", new String[]{"Anterior Root Canal Therapy", "Deep Tissue Cleaning"});
            addVisitView(container, "VISIT 2", "Restorative Phase", "45 min", new String[]{"Porcelain Crown Placement", "Bite Adjustment & Polish"});

        } else {
            // Extraction + Implant
            tvTotalVisits.setText("3 Visits Total");
            tvRecovery.setText("6 Months");
            tvOptDesc.setText("Plan optimized for maximum bone integration success based on clinical data.");

            addVisitView(container, "VISIT 1", "Extraction Phase", "45 min", new String[]{"Surgical Extraction", "Socket Preservation"});
            addVisitView(container, "VISIT 2", "Healing Review", "20 min", new String[]{"Suture Removal", "Site Inspection"});
            addVisitView(container, "VISIT 3", "Implant Phase", "90 min", new String[]{"Implant Placement", "Primary Stability Test"});
        }
    }

    private void addVisitView(LinearLayout container, String visitNum, String phase, String duration, String[] tasks) {
        View visitView = LayoutInflater.from(this).inflate(R.layout.item_visit_plan, container, false);
        
        ((TextView) visitView.findViewById(R.id.tv_visit_num)).setText(visitNum);
        ((TextView) visitView.findViewById(R.id.tv_visit_phase)).setText(phase);
        ((TextView) visitView.findViewById(R.id.tv_visit_duration)).setText(duration);

        LinearLayout taskContainer = visitView.findViewById(R.id.ll_task_container);
        for (String task : tasks) {
            View taskView = LayoutInflater.from(this).inflate(R.layout.item_task_checkmark, taskContainer, false);
            ((TextView) taskView.findViewById(R.id.tv_task_name)).setText(task);
            taskContainer.addView(taskView);
        }

        container.addView(visitView);
    }
}
