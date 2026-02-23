package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SuggestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        // Get data from intent
        String selectedTooth = getIntent().getStringExtra("selected_tooth");
        String diagnosis = getIntent().getStringExtra("selected_diagnosis");
        String severityLabel = getIntent().getStringExtra("selected_severity_label");
        int percent = getIntent().getIntExtra("selected_percent", 95);

        if (selectedTooth == null) selectedTooth = "Tooth 16";
        if (diagnosis == null) diagnosis = "Deep Caries";
        if (severityLabel == null) severityLabel = "High Severity";

        setupUI(selectedTooth, diagnosis, severityLabel, percent);

        // Top Left Arrow -> Back to Problem Mapping
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Select Treatment Buttons
        findViewById(R.id.btn_select_treatment_1).setOnClickListener(v -> {
            Toast.makeText(this, "Treatment 1 Selected", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.btn_select_treatment_2).setOnClickListener(v -> {
            Toast.makeText(this, "Treatment 2 Selected", Toast.LENGTH_SHORT).show();
        });

        // Bottom Nav (Dashboard Style)
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { startActivity(new Intent(this, TodayAppointmentActivity.class)); finish(); });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { /* Already here */ });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    private void setupUI(String tooth, String diagnosis, String severityLabel, int percent) {
        TextView tvSelectedTooth = findViewById(R.id.tv_selected_tooth);
        TextView tvDiagnosis = findViewById(R.id.tv_diagnosis_name);
        TextView tvSeverity = findViewById(R.id.tv_severity_text);
        ProgressBar pbSeverity = findViewById(R.id.pb_severity);
        
        tvSelectedTooth.setText(tooth);
        tvDiagnosis.setText(diagnosis);
        tvSeverity.setText(severityLabel + " (" + percent + "%)");
        pbSeverity.setProgress(percent);

        // Dynamic treatment suggestions based on diagnosis
        TextView t1Title = findViewById(R.id.tv_treatment_1_title);
        TextView t1Rec = findViewById(R.id.tv_t1_recovery);
        TextView t1Visits = findViewById(R.id.tv_t1_visits);
        TextView t1Success = findViewById(R.id.tv_t1_success);

        TextView t2Title = findViewById(R.id.tv_treatment_2_title);
        TextView t2Rec = findViewById(R.id.tv_t2_recovery);
        TextView t2Visits = findViewById(R.id.tv_t2_visits);
        TextView t2Success = findViewById(R.id.tv_t2_success);

        if (diagnosis.contains("Caries") || diagnosis.contains("Cavity")) {
            t1Title.setText("Root Canal Therapy");
            t1Rec.setText("3-5 Days"); t1Visits.setText("2 Visits"); t1Success.setText("98%");
            
            t2Title.setText("Extraction + Implant");
            t2Rec.setText("3-6 Months"); t2Visits.setText("4-5 Visits"); t2Success.setText("95%");
        } else if (diagnosis.contains("Lesion") || diagnosis.contains("Abscess")) {
            t1Title.setText("Apicoectomy");
            t1Rec.setText("1-2 Weeks"); t1Visits.setText("1 Visit"); t1Success.setText("92%");
            
            t2Title.setText("Bone Grafting");
            t2Rec.setText("4-6 Months"); t2Visits.setText("2 Visits"); t2Success.setText("88%");
        } else {
            t1Title.setText("Professional Cleaning");
            t1Rec.setText("Same Day"); t1Visits.setText("1 Visit"); t1Success.setText("99%");
            
            t2Title.setText("Fluoride Treatment");
            t2Rec.setText("1 Day"); t2Visits.setText("1 Visit"); t2Success.setText("97%");
        }
    }
}
