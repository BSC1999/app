package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class TreatmentTimelineActivity extends AppCompatActivity {

    private String selectedPlan = "";
    private String selectedTooth = "";
    private String diagnosis = "";
    private boolean fromPatientProfile = false;
    private PatientInfo patientData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_timeline);

        // Get data from Intent
        selectedPlan = getIntent().getStringExtra("selected_plan");
        selectedTooth = getIntent().getStringExtra("selected_tooth");
        diagnosis = getIntent().getStringExtra("selected_diagnosis");
        fromPatientProfile = getIntent().getBooleanExtra("from_patient_profile", false);
        patientData = (PatientInfo) getIntent().getSerializableExtra("patient_data");

        setupTimelineData();

        // Control "Generate Optimized Plan" button visibility
        MaterialButton btnGenerate = findViewById(R.id.btn_generate_plan);
        if (btnGenerate != null) {
            if (fromPatientProfile) {
                btnGenerate.setVisibility(View.VISIBLE);
                btnGenerate.setEnabled(true);
                btnGenerate.setOnClickListener(v -> {
                    Intent intent = new Intent(this, TreatmentPlanActivity.class);
                    intent.putExtra("selected_plan", selectedPlan);
                    intent.putExtra("selected_tooth", selectedTooth);
                    intent.putExtra("selected_diagnosis", diagnosis);
                    intent.putExtra("patient_data", patientData);
                    startActivity(intent);
                });
            } else {
                btnGenerate.setVisibility(View.GONE);
            }
        }

        // Top Left Back Button
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            finish(); // Goes back to TreatmentSelectionActivity
        });

        // Bottom Navigation
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { 
            startActivity(new Intent(this, DashboardActivity.class)); 
            finish(); 
        });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { 
            startActivity(new Intent(this, PatientsActivity.class)); 
            finish(); 
        });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { 
            startActivity(new Intent(this, TodayAppointmentActivity.class)); 
            finish(); 
        });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { 
            // Already in AI flow
        });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { 
            startActivity(new Intent(this, MoreActivity.class)); 
            finish(); 
        });
    }

    private void setupTimelineData() {
        TextView tvCount = findViewById(R.id.tv_treatment_count);
        TextView tvDuration = findViewById(R.id.tv_estimated_duration);
        
        TextView tvStep1Title = findViewById(R.id.tv_step_1_title);
        TextView tvStep1Desc = findViewById(R.id.tv_step_1_desc);
        
        TextView tvInterval1 = findViewById(R.id.tv_interval_1);
        
        TextView tvStep2Title = findViewById(R.id.tv_step_2_title);
        TextView tvStep2Desc = findViewById(R.id.tv_step_2_desc);
        TextView tvStep2Day = findViewById(R.id.tv_step_2_day);
        
        TextView tvInterval2 = findViewById(R.id.tv_interval_2);
        View containerInterval2 = findViewById(R.id.container_interval_2);
        
        View step3 = findViewById(R.id.step_3);
        TextView tvStep3Title = findViewById(R.id.tv_step_3_title);
        TextView tvStep3Desc = findViewById(R.id.tv_step_3_desc);
        
        TextView tvAiInsight = findViewById(R.id.tv_ai_insight);
        View timelineLine = findViewById(R.id.view_timeline_line);

        if ("plan1".equals(selectedPlan)) {
            // Root Canal Therapy - 2 Steps
            tvCount.setText("Selected treatments: 2");
            tvDuration.setText("Estimated duration: 8 Days");
            
            tvStep1Title.setText("Endodontic Cleaning");
            tvStep1Desc.setText("Precision cleaning and disinfection of root canals.");
            
            tvInterval1.setText("7 Days Healing Interval");
            
            tvStep2Day.setText("DAY 8");
            tvStep2Title.setText("Final Sealing & Filling");
            tvStep2Desc.setText("Sealing the canal and restorative core placement.");
            
            // Hide Step 3 elements
            containerInterval2.setVisibility(View.GONE);
            step3.setVisibility(View.GONE);
            
            // Adjust line height for 2 steps
            timelineLine.getLayoutParams().height = (int) (140 * getResources().getDisplayMetrics().density);
            
            tvAiInsight.setText("AI Prediction: Root canal success rate is 95%. Recommended 7-day interval allows periapical tissue to stabilize.");
            
        } else if ("plan2".equals(selectedPlan)) {
            // Extraction + Implant - 3 Steps
            tvCount.setText("Selected treatments: 3");
            tvDuration.setText("Estimated duration: 6 Months");
            
            tvStep1Title.setText("Surgical Extraction");
            tvStep1Desc.setText("Safe removal of compromised tooth structure.");
            
            tvInterval1.setText("3 Months Bone Healing");
            
            tvStep2Day.setText("MONTH 4");
            tvStep2Title.setText("Implant Placement");
            tvStep2Desc.setText("Titanium post integration into the jawbone.");
            
            // Show Step 3 elements
            containerInterval2.setVisibility(View.VISIBLE);
            tvInterval2.setText("2 Months Osseointegration");
            
            step3.setVisibility(View.VISIBLE);
            tvStep3Title.setText("Final Crown Attachment");
            tvStep3Desc.setText("Permanent aesthetic restorative placement.");
            
            // Adjust line height for 3 steps
            timelineLine.getLayoutParams().height = (int) (300 * getResources().getDisplayMetrics().density);
            
            tvAiInsight.setText("AI Prediction: Significant bone loss detected. 3-4 months healing is crucial for osseointegration before final loading.");
        }
    }
}
