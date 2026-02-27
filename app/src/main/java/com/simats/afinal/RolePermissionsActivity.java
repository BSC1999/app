package com.simats.afinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;

public class RolePermissionsActivity extends AppCompatActivity {

    private String currentSelectedRole = "Admin";
    private MaterialButton btnAdmin, btnDoctor, btnConsultant, btnAssistant;
    private MaterialSwitch swAddPatient, swEditPatient, swDeletePatient, swAddDoctor, swViewAnalytics, swAuditLogs, swSettings;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_permissions);

        prefs = getSharedPreferences("RolePermissions", MODE_PRIVATE);

        // Header
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Role Buttons
        btnAdmin = findViewById(R.id.btn_role_admin);
        btnDoctor = findViewById(R.id.btn_role_doctor);
        btnConsultant = findViewById(R.id.btn_role_consultant);
        btnAssistant = findViewById(R.id.btn_role_assistant);

        // Switches
        swAddPatient = findViewById(R.id.switch_add_patient);
        swEditPatient = findViewById(R.id.switch_edit_patient);
        swDeletePatient = findViewById(R.id.switch_delete_patient);
        swAddDoctor = findViewById(R.id.switch_add_doctor);
        swViewAnalytics = findViewById(R.id.switch_view_analytics);
        swAuditLogs = findViewById(R.id.switch_audit_logs);
        swSettings = findViewById(R.id.switch_settings);

        setupRoleClickListeners();
        loadPermissionsForRole("Admin"); // Default

        findViewById(R.id.btn_save_permissions).setOnClickListener(v -> {
            savePermissionsForRole(currentSelectedRole);
            Toast.makeText(this, "Permissions saved for " + currentSelectedRole, Toast.LENGTH_SHORT).show();
        });

        setupBottomNav();
    }

    private void setupRoleClickListeners() {
        btnAdmin.setOnClickListener(v -> selectRole("Admin"));
        btnDoctor.setOnClickListener(v -> selectRole("Doctor"));
        btnConsultant.setOnClickListener(v -> selectRole("Consultant"));
        btnAssistant.setOnClickListener(v -> selectRole("Assistant"));
    }

    private void selectRole(String role) {
        currentSelectedRole = role;
        updateRoleButtonStyles();
        loadPermissionsForRole(role);
    }

    private void updateRoleButtonStyles() {
        int selectedColor = getResources().getColor(android.R.color.black);
        int unselectedColor = getResources().getColor(android.R.color.transparent);
        int selectedText = getResources().getColor(android.R.color.white);
        int unselectedText = getResources().getColor(android.R.color.black);

        btnAdmin.setBackgroundTintList(ColorStateList.valueOf(currentSelectedRole.equals("Admin") ? selectedColor : unselectedColor));
        btnAdmin.setTextColor(currentSelectedRole.equals("Admin") ? selectedText : unselectedText);
        
        btnDoctor.setBackgroundTintList(ColorStateList.valueOf(currentSelectedRole.equals("Doctor") ? selectedColor : unselectedColor));
        btnDoctor.setTextColor(currentSelectedRole.equals("Doctor") ? selectedText : unselectedText);

        btnConsultant.setBackgroundTintList(ColorStateList.valueOf(currentSelectedRole.equals("Consultant") ? selectedColor : unselectedColor));
        btnConsultant.setTextColor(currentSelectedRole.equals("Consultant") ? selectedText : unselectedText);

        btnAssistant.setBackgroundTintList(ColorStateList.valueOf(currentSelectedRole.equals("Assistant") ? selectedColor : unselectedColor));
        btnAssistant.setTextColor(currentSelectedRole.equals("Assistant") ? selectedText : unselectedText);
    }

    private void loadPermissionsForRole(String role) {
        swAddPatient.setChecked(prefs.getBoolean(role + "_add_patient", true));
        swEditPatient.setChecked(prefs.getBoolean(role + "_edit_patient", true));
        swDeletePatient.setChecked(prefs.getBoolean(role + "_delete_patient", role.equals("Admin")));
        swAddDoctor.setChecked(prefs.getBoolean(role + "_add_doctor", role.equals("Admin")));
        swViewAnalytics.setChecked(prefs.getBoolean(role + "_view_analytics", !role.equals("Assistant")));
        swAuditLogs.setChecked(prefs.getBoolean(role + "_audit_logs", role.equals("Admin")));
        swSettings.setChecked(prefs.getBoolean(role + "_settings", role.equals("Admin")));
    }

    private void savePermissionsForRole(String role) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(role + "_add_patient", swAddPatient.isChecked());
        editor.putBoolean(role + "_edit_patient", swEditPatient.isChecked());
        editor.putBoolean(role + "_delete_patient", swDeletePatient.isChecked());
        editor.putBoolean(role + "_add_doctor", swAddDoctor.isChecked());
        editor.putBoolean(role + "_view_analytics", swViewAnalytics.isChecked());
        editor.putBoolean(role + "_audit_logs", swAuditLogs.isChecked());
        editor.putBoolean(role + "_settings", swSettings.isChecked());
        editor.apply();
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
    }
}
