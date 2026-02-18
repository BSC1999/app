package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        // --- Profile Settings Option ---
        findViewById(R.id.btn_profile_settings).setOnClickListener(v -> {
            startActivity(new Intent(MoreActivity.this, DoctorProfileActivity.class));
        });

        findViewById(R.id.btn_audit_logs).setOnClickListener(v -> {
            startActivity(new Intent(MoreActivity.this, AuditLogsActivity.class));
        });

        findViewById(R.id.btn_about_legal).setOnClickListener(v -> {
            startActivity(new Intent(MoreActivity.this, AboutLegalActivity.class));
        });

        findViewById(R.id.btn_logout_screen).setOnClickListener(v -> {
            startActivity(new Intent(MoreActivity.this, LogoutActivity.class));
        });

        // Bottom Nav
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { startActivity(new Intent(this, TodayAppointmentActivity.class)); finish(); });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { startActivity(new Intent(this, UploadImagesActivity.class)); finish(); });
    }
}