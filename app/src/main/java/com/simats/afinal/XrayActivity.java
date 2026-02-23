package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class XrayActivity extends AppCompatActivity {

    private int selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xray);

        ImageView ivXrayDisplay = findViewById(R.id.iv_xray_display);
        TextView tvDiag1Title = findViewById(R.id.tv_diag_1_title);
        TextView tvDiag1Desc = findViewById(R.id.tv_diag_1_desc);
        TextView tvDiag2Title = findViewById(R.id.tv_diag_2_title);
        TextView tvDiag2Desc = findViewById(R.id.tv_diag_2_desc);
        TextView tvDiag3Title = findViewById(R.id.tv_diag_3_title);
        TextView tvDiag3Desc = findViewById(R.id.tv_diag_3_desc);
        
        selectedImage = getIntent().getIntExtra("selected_image", R.drawable.img_22);
        ivXrayDisplay.setImageResource(selectedImage);

        updateDiagnosisData(selectedImage, tvDiag1Title, tvDiag1Desc, tvDiag2Title, tvDiag2Desc, tvDiag3Title, tvDiag3Desc);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        MaterialButton btnExplainable = findViewById(R.id.btn_explainable_ai);
        if (btnExplainable != null) {
            btnExplainable.setOnClickListener(v -> {
                Intent intent = new Intent(XrayActivity.this, ProblemMappingActivity.class);
                intent.putExtra("selected_image", selectedImage);
                startActivity(intent);
            });
        }

        // Bottom Nav Logic
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { startActivity(new Intent(this, TodayAppointmentActivity.class)); finish(); });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { startActivity(new Intent(this, UploadImagesActivity.class)); finish(); });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    private void updateDiagnosisData(int resId, TextView t1, TextView d1, TextView t2, TextView d2, TextView t3, TextView d3) {
        if (resId == R.drawable.img_23) {
            t1.setText("Enamel Caries"); d1.setText("92% Confidence");
            t2.setText("Mild Gingivitis"); d2.setText("75% Confidence");
            t3.setText("No Bone Loss"); d3.setText("98% Confidence");
        } else if (resId == R.drawable.img_24) {
            t1.setText("Deep Cavity"); d1.setText("96% Confidence");
            t2.setText("Periapical Abscess"); d2.setText("88% Confidence");
            t3.setText("Root Canal Suggested"); d3.setText("90% Confidence");
        } else {
            t1.setText("Deep Caries"); d1.setText("95% Confidence");
            t2.setText("Periapical Lesion"); d2.setText("80% Confidence");
            t3.setText("Enamel Caries"); d3.setText("92% Confidence");
        }
    }
}