package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class ProblemActivity extends AppCompatActivity {

    private String selectedTooth = ""; 
    private String selectedDiagnosis = "";
    private View selectedView = null;
    private int selectedImage;
    private MaterialButton btnContinue;
    private TextView tvIssueCountHeader;
    private boolean fromPatientProfile = false;
    private PatientInfo patientData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        selectedImage = getIntent().getIntExtra("selected_image", R.drawable.img_22);
        fromPatientProfile = getIntent().getBooleanExtra("from_patient_profile", false);
        patientData = (PatientInfo) getIntent().getSerializableExtra("patient_data");
        
        ImageView ivAnalyzedXray = findViewById(R.id.iv_analyzed_xray);
        ivAnalyzedXray.setImageResource(selectedImage);

        btnContinue = findViewById(R.id.btn_continue_suggestions);
        btnContinue.setEnabled(true);
        btnContinue.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#2C3E50")));
        
        tvIssueCountHeader = findViewById(R.id.tv_issue_count_header);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Mawa, AI simulation starting...
        setupDynamicIssues(selectedImage);

        btnContinue.setOnClickListener(v -> {
            if (!selectedTooth.isEmpty()) {
                Intent intent = new Intent(ProblemActivity.this, TreatmentSelectionActivity.class);
                intent.putExtra("selected_tooth", "Tooth " + selectedTooth);
                intent.putExtra("selected_diagnosis", selectedDiagnosis);
                intent.putExtra("selected_image", selectedImage);
                intent.putExtra("from_patient_profile", fromPatientProfile);
                intent.putExtra("patient_data", patientData);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Select any issue", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { /* Already here */ });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    private void setupDynamicIssues(int imageResId) {
        // Reset visibility
        findViewById(R.id.highlight_1).setVisibility(View.GONE);
        findViewById(R.id.highlight_2).setVisibility(View.GONE);
        findViewById(R.id.card_issue_1).setVisibility(View.GONE);
        findViewById(R.id.card_issue_2).setVisibility(View.GONE);

        // Mawa, ippudu prathi X-ray ki separate logic untundhi, confusion lekunda
        if (imageResId == R.drawable.img_23) {
            // Case 1: Enamel Caries & Gingivitis
            showIssue(1, "14", "Enamel Caries", "#FF4B4B", "HIGH SEVERITY", 92, "Fluoride Treatment", R.id.highlight_1, R.id.card_issue_1);
            showIssue(2, "26", "Mild Gingivitis", "#F1C40F", "MODERATE", 75, "Professional Cleaning", R.id.highlight_2, R.id.card_issue_2);
            highlightChartTooth("14", "#FF4B4B");
            highlightChartTooth("26", "#F1C40F");
            if (tvIssueCountHeader != null) tvIssueCountHeader.setText("AI DETECTED ISSUES (2)");
        } else if (imageResId == R.drawable.img_24) {
            // Case 2: Deep Cavity & Periapical Abscess
            showIssue(1, "16", "Deep Cavity", "#FF4B4B", "CRITICAL", 96, "Root Canal sugersted", R.id.highlight_1, R.id.card_issue_1);
            showIssue(2, "17", "Abscess", "#E74C3C", "MODERATE", 88, "Endodontic Treatment", R.id.highlight_2, R.id.card_issue_2);
            highlightChartTooth("16", "#FF4B4B");
            highlightChartTooth("17", "#E74C3C");
            if (tvIssueCountHeader != null) tvIssueCountHeader.setText("AI DETECTED ISSUES (2)");
        } else if (imageResId == R.drawable.img_25) {
            // Case 3: Periodontitis
            showIssue(1, "32", "Periodontitis", "#F39C12", "MODERATE", 82, "Scaling & Root Planing", R.id.highlight_1, R.id.card_issue_1);
            highlightChartTooth("32", "#F39C12");
            if (tvIssueCountHeader != null) tvIssueCountHeader.setText("AI DETECTED ISSUES (1)");
        } else if (imageResId == R.drawable.img_26) {
            // Case 4: Impacted Wisdom Tooth
            showIssue(1, "38", "Impacted Tooth", "#9B59B6", "HIGH", 94, "Surgical Extraction", R.id.highlight_1, R.id.card_issue_1);
            highlightChartTooth("38", "#9B59B6");
            if (tvIssueCountHeader != null) tvIssueCountHeader.setText("AI DETECTED ISSUES (1)");
        } else {
            // Default for newly added/uploaded images - Mawa, ikkada predict chesthundhi AI
            showIssue(1, "11", "Incipient Caries", "#FF4B4B", "INITIAL", 85, "Remineralization", R.id.highlight_1, R.id.card_issue_1);
            showIssue(2, "46", "Calculus Deposit", "#F1C40F", "MILD", 78, "Dental Prophylaxis", R.id.highlight_2, R.id.card_issue_2);
            highlightChartTooth("11", "#FF4B4B");
            highlightChartTooth("46", "#F1C40F");
            if (tvIssueCountHeader != null) tvIssueCountHeader.setText("AI DETECTED ISSUES (2)");
        }
    }

    private void showIssue(int index, String tooth, String title, String color, String severity, int percent, String treatment, int highlightId, int cardId) {
        View highlightContainer = findViewById(highlightId);
        if (highlightContainer != null) {
            highlightContainer.setVisibility(View.VISIBLE);
            TextView tvHighlightLabel = findViewById(index == 1 ? R.id.tv_highlight_label_1 : R.id.tv_highlight_label_2);
            View viewHighlightRect = findViewById(index == 1 ? R.id.view_highlight_rect_1 : R.id.view_highlight_rect_2);
            
            if (tvHighlightLabel != null) {
                String label = title.contains("Caries") ? "CARIES" : (title.contains("Lesion") ? "LESION" : (title.contains("Cavity") ? "CAVITY" : title.toUpperCase()));
                tvHighlightLabel.setText(label);
                tvHighlightLabel.setTextColor(android.graphics.Color.parseColor(color));
            }
            if (viewHighlightRect != null) {
                viewHighlightRect.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(color)));
            }
        }

        MaterialCardView card = findViewById(cardId);
        if (card != null) {
            card.setVisibility(View.VISIBLE);
            
            TextView tvToothNum = findViewById(index == 1 ? R.id.tv_issue_1_tooth_num : R.id.tv_issue_2_tooth_num);
            TextView tvTitle = findViewById(index == 1 ? R.id.tv_issue_1_title : R.id.tv_issue_2_title);
            TextView tvSeverityLabel = findViewById(index == 1 ? R.id.tv_issue_1_severity_label : R.id.tv_issue_2_severity_label);
            ProgressBar pbIssue = findViewById(index == 1 ? R.id.pb_issue_1 : R.id.pb_issue_2);
            TextView tvPercent = findViewById(index == 1 ? R.id.tv_issue_1_percent : R.id.tv_issue_2_percent);
            TextView tvTreatment = findViewById(index == 1 ? R.id.tv_issue_1_treatment : R.id.tv_issue_2_treatment);
            
            if (tvToothNum != null) {
                tvToothNum.setText(tooth);
                tvToothNum.setTextColor(android.graphics.Color.BLACK);
                tvToothNum.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(color)));
            }
            if (tvTitle != null) tvTitle.setText(title);
            if (tvSeverityLabel != null) {
                tvSeverityLabel.setText(severity);
                tvSeverityLabel.setTextColor(android.graphics.Color.parseColor(color));
            }
            if (pbIssue != null) {
                pbIssue.setProgress(percent);
                pbIssue.setProgressTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(color)));
            }
            if (tvPercent != null) tvPercent.setText(percent + "%");
            if (tvTreatment != null) tvTreatment.setText(treatment);

            card.setOnClickListener(v -> {
                if (selectedView != null) {
                    MaterialCardView previousCard = (MaterialCardView) selectedView;
                    previousCard.setStrokeWidth(1);
                    previousCard.setStrokeColor(android.graphics.Color.parseColor("#E0E0E0"));
                    previousCard.setCardBackgroundColor(android.graphics.Color.WHITE);
                }
                
                selectedTooth = tooth;
                selectedDiagnosis = title;
                selectedView = card;
                
                card.setStrokeWidth(6);
                card.setStrokeColor(android.graphics.Color.parseColor("#E8F3F3")); 
                card.setCardBackgroundColor(android.graphics.Color.parseColor("#E8F3F3"));

                Toast.makeText(this, "Tooth " + tooth + " selected", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void highlightChartTooth(String toothNum, String color) {
        int resId = getResources().getIdentifier("tooth_" + toothNum, "id", getPackageName());
        if (resId != 0) {
            TextView tv = findViewById(resId);
            if (tv != null) {
                tv.setBackgroundResource(R.drawable.circle_outline);
                tv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(color)));
                tv.setTextColor(android.graphics.Color.BLACK);
            }
        }
    }
}
