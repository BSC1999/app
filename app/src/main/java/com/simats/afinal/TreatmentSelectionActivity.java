package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class TreatmentSelectionActivity extends AppCompatActivity {

    private String selectedPlan = "";
    private String selectedTooth = "";
    private String diagnosis = "";
    private MaterialButton btnConfirm;
    private MaterialCardView card1, card2;
    private ImageView icon1, icon2;
    private boolean fromPatientProfile = false;
    private PatientInfo patientData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_selection);

        // Get data from ProblemActivity
        selectedTooth = getIntent().getStringExtra("selected_tooth");
        diagnosis = getIntent().getStringExtra("selected_diagnosis");
        fromPatientProfile = getIntent().getBooleanExtra("from_patient_profile", false);
        patientData = (PatientInfo) getIntent().getSerializableExtra("patient_data");

        TextView tvTooth = findViewById(R.id.tv_selected_tooth);
        TextView tvDiag = findViewById(R.id.tv_diagnosis_name);

        if (selectedTooth != null) tvTooth.setText(selectedTooth);
        if (diagnosis != null) tvDiag.setText("Diagnosis: " + diagnosis);

        card1 = findViewById(R.id.card_plan_1);
        card2 = findViewById(R.id.card_plan_2);
        icon1 = findViewById(R.id.iv_plan_1_icon);
        icon2 = findViewById(R.id.iv_plan_2_icon);
        btnConfirm = findViewById(R.id.btn_confirm_choice);

        // Top Left Arrow -> Back
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        card1.setOnClickListener(v -> selectPlan("plan1"));
        card2.setOnClickListener(v -> selectPlan("plan2"));

        btnConfirm.setOnClickListener(v -> {
            if (!selectedPlan.isEmpty()) {
                Intent intent = new Intent(this, TreatmentTimelineActivity.class);
                intent.putExtra("selected_plan", selectedPlan);
                intent.putExtra("selected_tooth", selectedTooth);
                intent.putExtra("selected_diagnosis", diagnosis);
                intent.putExtra("from_patient_profile", fromPatientProfile);
                intent.putExtra("patient_data", patientData);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select a treatment plan first", Toast.LENGTH_SHORT).show();
            }
        });

        // --- Bottom Navigation (Dashboard Style) ---
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

    private void selectPlan(String plan) {
        selectedPlan = plan;
        btnConfirm.setEnabled(true);
        // Change to login button color (#00E5FF)
        btnConfirm.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#00E5FF")));
        btnConfirm.setTextColor(android.graphics.Color.BLACK);

        String highlightColor = "#B2EBF2"; // A slightly darker version of light teal (#E8F3F3)

        if (plan.equals("plan1")) {
            // Highlight card 1
            card1.setStrokeWidth(6);
            card1.setStrokeColor(android.graphics.Color.parseColor(highlightColor));
            card1.setCardBackgroundColor(android.graphics.Color.WHITE);
            
            // Fill icon 1
            icon1.setImageResource(R.drawable.circle_bg);
            icon1.setImageTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(highlightColor)));

            // Reset card 2
            card2.setStrokeWidth(1);
            card2.setStrokeColor(android.graphics.Color.parseColor("#E0E0E0"));
            card2.setCardBackgroundColor(android.graphics.Color.WHITE);
            icon2.setImageResource(R.drawable.circle_outline);
            icon2.setImageTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#8BBABB")));
        } else {
            // Highlight card 2
            card2.setStrokeWidth(6);
            card2.setStrokeColor(android.graphics.Color.parseColor(highlightColor));
            card2.setCardBackgroundColor(android.graphics.Color.WHITE);
            
            // Fill icon 2
            icon2.setImageResource(R.drawable.circle_bg);
            icon2.setImageTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(highlightColor)));

            // Reset card 1
            card1.setStrokeWidth(1);
            card1.setStrokeColor(android.graphics.Color.parseColor("#E0E0E0"));
            card1.setCardBackgroundColor(android.graphics.Color.WHITE);
            icon1.setImageResource(R.drawable.circle_outline);
            icon1.setImageTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#8BBABB")));
        }
    }
}
