package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class ProblemMappingActivity extends AppCompatActivity {

    private String selectedTooth = ""; 
    private String selectedDiagnosis = "";
    private String selectedSeverityLabel = "";
    private int selectedPercent = 0;
    private View selectedView = null;
    private int selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_mapping);

        selectedImage = getIntent().getIntExtra("selected_image", R.drawable.img_22);
        ImageView ivAnalyzedXray = findViewById(R.id.iv_analyzed_xray);
        ivAnalyzedXray.setImageResource(selectedImage);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Setup dynamic issues based on the X-ray image
        setupDynamicIssues(selectedImage);

        // FINAL REDIRECTION FIX: Strictly go to SuggestionsActivity (Clinical Suggestions)
        findViewById(R.id.btn_continue_suggestions).setOnClickListener(v -> {
            if (selectedTooth.isEmpty()) {
                Toast.makeText(this, "Please select an issue to continue", Toast.LENGTH_SHORT).show();
            } else {
                // REDIRECTION TRIGGER - STRICT TARGET
                Intent intent = new Intent(ProblemMappingActivity.this, SuggestionsActivity.class);
                intent.putExtra("selected_tooth", "Tooth " + selectedTooth);
                intent.putExtra("selected_diagnosis", selectedDiagnosis);
                intent.putExtra("selected_severity_label", selectedSeverityLabel);
                intent.putExtra("selected_percent", selectedPercent);
                intent.putExtra("selected_image", selectedImage);
                startActivity(intent);
            }
        });

        // Navigation (Dashboard style)
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { startActivity(new Intent(this, TodayAppointmentActivity.class)); finish(); });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { /* Already here */ });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    private void setupDynamicIssues(int imageResId) {
        findViewById(R.id.circle_1).setVisibility(View.GONE);
        findViewById(R.id.circle_2).setVisibility(View.GONE);
        findViewById(R.id.circle_3).setVisibility(View.GONE);
        findViewById(R.id.card_issue_1).setVisibility(View.GONE);
        findViewById(R.id.card_issue_2).setVisibility(View.GONE);
        findViewById(R.id.card_issue_3).setVisibility(View.GONE);

        int count = 0;
        if (imageResId == R.drawable.img_23) {
            count = 2;
            showIssue(1, "14", "Enamel Caries", "HIGH SEVERITY", "#FF4B4B", 92, "Fluoride Treatment", R.id.circle_1, R.id.card_issue_1);
            showIssue(2, "26", "Mild Gingivitis", "MODERATE", "#F1C40F", 75, "Professional Cleaning", R.id.circle_2, R.id.card_issue_2);
            highlightChartTooth("14", "#FF4B4B");
            highlightChartTooth("26", "#F1C40F");
        } else if (imageResId == R.drawable.img_24) {
            count = 2;
            showIssue(1, "46", "Deep Cavity", "HIGH SEVERITY", "#FF4B4B", 96, "Root Canal Therapy", R.id.circle_1, R.id.card_issue_1);
            showIssue(2, "11", "Abscess", "MODERATE", "#F1C40F", 88, "Apicoectomy", R.id.circle_2, R.id.card_issue_2);
            highlightChartTooth("46", "#FF4B4B");
            highlightChartTooth("11", "#F1C40F");
        } else {
            count = 3;
            showIssue(1, "16", "Deep Caries", "HIGH SEVERITY", "#FF4B4B", 95, "Root Canal Therapy", R.id.circle_1, R.id.card_issue_1);
            showIssue(2, "21", "Periapical Lesion", "MODERATE", "#F1C40F", 88, "Apicoectomy", R.id.circle_2, R.id.card_issue_2);
            showIssue(3, "38", "Enamel Caries", "HIGH SEVERITY", "#FF4B4B", 92, "Composite Filling", R.id.circle_3, R.id.card_issue_3);
            findViewById(R.id.tv_label_caries).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_label_lesion).setVisibility(View.VISIBLE);
            highlightChartTooth("16", "#FF4B4B");
            highlightChartTooth("21", "#F1C40F");
            highlightChartTooth("38", "#FF4B4B");
        }
        ((TextView)findViewById(R.id.tv_issue_count_header)).setText("AI DETECTED ISSUES (" + count + ")");
    }

    private void showIssue(int index, String tooth, String title, String severity, String color, int percent, String treatment, int circleId, int cardId) {
        findViewById(circleId).setVisibility(View.VISIBLE);
        MaterialCardView card = findViewById(cardId);
        card.setVisibility(View.VISIBLE);
        TextView tvToothNum = findViewById(index == 1 ? R.id.tv_issue_1_tooth_num : (index == 2 ? R.id.tv_issue_2_tooth_num : R.id.tv_issue_3_tooth_num));
        TextView tvTitle = findViewById(index == 1 ? R.id.tv_issue_1_title : (index == 2 ? R.id.tv_issue_2_title : R.id.tv_issue_3_title));
        TextView tvSeverityLabel = findViewById(index == 1 ? R.id.tv_issue_1_severity_label : (index == 2 ? R.id.tv_issue_2_severity_label : R.id.tv_issue_3_severity_label));
        ProgressBar pbIssue = findViewById(index == 1 ? R.id.pb_issue_1 : (index == 2 ? R.id.pb_issue_2 : R.id.pb_issue_3));
        TextView tvPercent = findViewById(index == 1 ? R.id.tv_issue_1_percent : (index == 2 ? R.id.tv_issue_2_percent : R.id.tv_issue_3_percent));
        TextView tvTreatment = findViewById(index == 1 ? R.id.tv_issue_1_treatment : (index == 2 ? R.id.tv_issue_2_treatment : R.id.tv_issue_3_treatment));
        tvToothNum.setText(tooth);
        tvTitle.setText(title);
        tvSeverityLabel.setText(severity);
        tvSeverityLabel.setTextColor(android.graphics.Color.parseColor(color));
        pbIssue.setProgress(percent);
        pbIssue.setProgressTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(color)));
        tvPercent.setText(percent + "%");
        tvTreatment.setText(treatment);
        card.setOnClickListener(v -> {
            if (selectedView != null) ((MaterialCardView)selectedView).setStrokeWidth(0);
            selectedTooth = tooth;
            selectedDiagnosis = title;
            selectedSeverityLabel = severity;
            selectedPercent = percent;
            selectedView = card;
            card.setStrokeWidth(4);
            card.setStrokeColor(android.graphics.Color.parseColor("#3498DB"));
            Toast.makeText(this, "Tooth " + tooth + " selected", Toast.LENGTH_SHORT).show();
        });
    }

    private void highlightChartTooth(String toothNum, String color) {
        int resId = getResources().getIdentifier("tooth_" + toothNum, "id", getPackageName());
        if (resId != 0) {
            TextView tv = findViewById(resId);
            tv.setBackgroundResource(R.drawable.circle_outline);
            tv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(color)));
            tv.setTextColor(android.graphics.Color.parseColor(color));
        }
    }
}
