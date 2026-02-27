package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AdminSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        // Role Permissions Click
        findViewById(R.id.btn_role_permissions).setOnClickListener(v -> {
            startActivity(new Intent(this, RolePermissionsActivity.class));
        });

        // Security Settings Click
        findViewById(R.id.btn_security_settings).setOnClickListener(v -> {
            startActivity(new Intent(this, SecuritySettingsActivity.class));
        });

        // Logout Button Click
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            Intent intent = new Intent(AdminSettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Bottom Navigation
        setupBottomNav();
    }

    private void setupBottomNav() {
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminDashboardActivity.class));
            finish();
        });
        findViewById(R.id.nav_staff_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, ManageDoctorsActivity.class));
            finish();
        });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, PatientDatabaseActivity.class));
            finish();
        });
        findViewById(R.id.nav_logs_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, SystemActivityLogsActivity.class));
            finish();
        });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> {}); // Already here
    }
}
