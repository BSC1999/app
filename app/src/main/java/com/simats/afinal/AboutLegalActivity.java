package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AboutLegalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_legal);

        // --- Redirect to Terms of Service ---
        findViewById(R.id.ll_terms).setOnClickListener(v -> {
            startActivity(new Intent(this, TermsOfServiceActivity.class));
        });

        // --- Redirect to Privacy Policy ---
        findViewById(R.id.ll_privacy).setOnClickListener(v -> {
            startActivity(new Intent(this, PrivacyPolicyActivity.class));
        });

        // Top left back button to Dashboard
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        // Bottom Nav Redirection
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
            startActivity(new Intent(this, UploadImagesActivity.class));
            finish();
        });

        findViewById(R.id.nav_more_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, MoreActivity.class));
            finish();
        });
    }
}