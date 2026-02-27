package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CreateDoctorActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etDoctorId, etEmail, etPhone, etSpecialization, etPassword;
    private Spinner spinnerRole;
    private MaterialButton btnCreate, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_doctor);

        etFullName = findViewById(R.id.et_full_name);
        etDoctorId = findViewById(R.id.et_doctor_id);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etSpecialization = findViewById(R.id.et_specialization);
        etPassword = findViewById(R.id.et_password);
        spinnerRole = findViewById(R.id.spinner_role);
        btnCreate = findViewById(R.id.btn_create_account);
        btnCancel = findViewById(R.id.btn_cancel);

        // Setup Role Spinner
        String[] roles = {"Select Role *", "Dental Doctor", "Consultant", "Dental Intern"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        // Back arrow
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Cancel button
        btnCancel.setOnClickListener(v -> finish());

        // Create Account button
        btnCreate.setOnClickListener(v -> validateAndCreate());

        // Bottom Navigation
        setupBottomNav();
    }

    private void validateAndCreate() {
        String name = etFullName.getText().toString().trim();
        String id = etDoctorId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String spec = etSpecialization.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        // 1. Full Name: Mandatory, only alphabets
        if (TextUtils.isEmpty(name)) {
            etFullName.setError("Full Name is mandatory");
            return;
        }
        if (!name.matches("[a-zA-Z\\s.]+")) {
            etFullName.setError("Only alphabets allowed");
            return;
        }

        // 2. Doctor ID: Mandatory, Alphanumeric
        if (TextUtils.isEmpty(id)) {
            etDoctorId.setError("Doctor ID is mandatory");
            return;
        }

        // 3. Email: Mandatory, correct format
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is mandatory");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email format");
            return;
        }

        // 4. Phone: Mandatory, 10 digits
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone is mandatory");
            return;
        }
        if (phone.length() != 10) {
            etPhone.setError("Must be exactly 10 digits");
            return;
        }

        // 5. Specialization: Only alphabets
        if (!TextUtils.isEmpty(spec) && !spec.matches("[a-zA-Z\\s]+")) {
            etSpecialization.setError("Only alphabets allowed");
            return;
        }

        // 6. Role Selection: Mandatory
        if (spinnerRole.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
            return;
        }

        // 7. Password: Mandatory
        if (TextUtils.isEmpty(pass)) {
            etPassword.setError("Password is mandatory");
            return;
        }

        // All good, add doctor
        DoctorInfo newDoc = new DoctorInfo(name, id, email, phone, spec, role, pass, "Active");
        DoctorDataManager.addDoctor(newDoc);
        
        Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupBottomNav() {
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminDashboardActivity.class));
            finish();
        });
        findViewById(R.id.nav_staff_custom).setOnClickListener(v -> finish());
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, PatientDatabaseActivity.class));
            finish();
        });
        findViewById(R.id.nav_logs_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, SystemActivityLogsActivity.class));
            finish();
        });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> {
            // Mawa, strictly Admin flow
            startActivity(new Intent(this, AdminSettingsActivity.class));
            finish();
        });
    }
}
