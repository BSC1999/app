package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class AIDrugRecommendationActivity extends AppCompatActivity {

    private String selectedPlan = "";
    private String selectedTooth = "";
    private String diagnosis = "";
    private PatientInfo patientData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_drug_recommendation);

        // Get data from Intent
        selectedPlan = getIntent().getStringExtra("selected_plan");
        selectedTooth = getIntent().getStringExtra("selected_tooth");
        diagnosis = getIntent().getStringExtra("selected_diagnosis");
        patientData = (PatientInfo) getIntent().getSerializableExtra("patient_data");

        setupProcedureInfo();
        performAIAnalysis();

        // Top Left Back Button -> Back to Treatment Plan
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Generate Smart Prescription
        findViewById(R.id.btn_generate_prescription).setOnClickListener(v -> {
            Intent intent = new Intent(this, PrescriptionPreviewActivity.class);
            intent.putExtra("selected_plan", selectedPlan);
            intent.putExtra("selected_tooth", selectedTooth);
            intent.putExtra("selected_diagnosis", diagnosis);
            intent.putExtra("patient_data", patientData);
            startActivity(intent);
        });

        // Bottom Navigation
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { startActivity(new Intent(this, TodayAppointmentActivity.class)); finish(); });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { /* Already here */ });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    private void setupProcedureInfo() {
        TextView tvProcedure = findViewById(R.id.tv_procedure_name);
        if ("plan1".equals(selectedPlan)) {
            tvProcedure.setText("Root Canal & Crown Placement");
        } else {
            tvProcedure.setText("Surgical Extraction & Implant");
        }
    }

    private void performAIAnalysis() {
        LinearLayout drugsContainer = findViewById(R.id.ll_drugs_container);
        LinearLayout precautionsContainer = findViewById(R.id.ll_precautions_container);
        MaterialCardView cvWarning = findViewById(R.id.cv_interaction_warning);
        TextView tvWarningDesc = findViewById(R.id.tv_interaction_warning_desc);

        drugsContainer.removeAllViews();
        precautionsContainer.removeAllViews();

        if (patientData == null) return;

        // AI Intelligence Simulation
        if ("plan1".equals(selectedPlan)) {
            // Root Canal drugs
            addDrugView(drugsContainer, "ANTIBIOTIC", "Amoxicillin 500mg", "3 times daily for 5 days");
            addDrugView(drugsContainer, "PAINKILLER", "Ibuprofen 400mg", "Every 6 hours as needed");
            addDrugView(drugsContainer, "MOUTH RINSE", "Chlorhexidine 0.12%", "Twice daily rinse");

            addPrecaution(precautionsContainer, "Take with food to avoid gastric upset");
            addPrecaution(precautionsContainer, "Complete full course of antibiotics");
        } else {
            // Extraction drugs
            addDrugView(drugsContainer, "ANTIBIOTIC", "Augmentin 625mg", "Twice daily for 7 days");
            addDrugView(drugsContainer, "PAINKILLER", "Ketorolac 10mg", "Every 8 hours for 3 days");
            addDrugView(drugsContainer, "ANTI-INFLAMMATORY", "Dexamethasone 4mg", "Once daily for 2 days");

            addPrecaution(precautionsContainer, "Apply cold pack for 20 mins every hour");
            addPrecaution(precautionsContainer, "Avoid hot foods/drinks for 24 hours");
        }

        // Logic for Medical Conditions Analysis
        String medHistory = patientData.getMedicalHistory() != null ? patientData.getMedicalHistory().toLowerCase() : "";
        if (medHistory.contains("diabetes")) {
            addPrecaution(precautionsContainer, "Monitor blood sugar; healing may be slower");
        }
        if (medHistory.contains("blood thinner") || medHistory.contains("aspirin") || medHistory.contains("heart")) {
            cvWarning.setVisibility(View.VISIBLE);
            tvWarningDesc.setText("Patient is on blood thinners; monitor for increased bleeding risk during the course of treatment.");
        }
    }

    private void addDrugView(LinearLayout container, String type, String name, String dosage) {
        View drugView = LayoutInflater.from(this).inflate(R.layout.item_drug_recommendation, container, false);
        ((TextView) drugView.findViewById(R.id.tv_drug_type)).setText(type);
        ((TextView) drugView.findViewById(R.id.tv_drug_name)).setText(name);
        ((TextView) drugView.findViewById(R.id.tv_drug_dosage)).setText(dosage);
        container.addView(drugView);
    }

    private void addPrecaution(LinearLayout container, String text) {
        View pView = LayoutInflater.from(this).inflate(R.layout.item_task_checkmark, container, false);
        ((TextView) pView.findViewById(R.id.tv_task_name)).setText(text);
        container.addView(pView);
    }
}
