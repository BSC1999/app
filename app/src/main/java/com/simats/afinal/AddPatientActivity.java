package com.simats.afinal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddPatientActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etAge, etGender, etPhone, etEmail, etAddress, etComplaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etAge = findViewById(R.id.et_age);
        etGender = findViewById(R.id.et_gender);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        etComplaint = findViewById(R.id.et_complaint);

        // Gender Picker
        etGender.setFocusable(false);
        etGender.setOnClickListener(v -> {
            String[] genders = {"Male", "Female", "Others"};
            new AlertDialog.Builder(this)
                    .setTitle("Select Gender")
                    .setItems(genders, (dialog, which) -> etGender.setText(genders[which]))
                    .show();
        });

        findViewById(R.id.btn_close).setOnClickListener(v -> {
            startActivity(new Intent(AddPatientActivity.this, DashboardActivity.class));
            finish();
        });

        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (validateForm()) {
                // Pass data to MedicalDentalHistoryActivity
                Intent intent = new Intent(AddPatientActivity.this, MedicalDentalHistoryActivity.class);
                intent.putExtra("firstName", etFirstName.getText().toString().trim());
                intent.putExtra("lastName", etLastName.getText().toString().trim());
                intent.putExtra("age_input", etAge.getText().toString().trim());
                intent.putExtra("gender", etGender.getText().toString().trim());
                intent.putExtra("phone", etPhone.getText().toString().trim());
                intent.putExtra("email", etEmail.getText().toString().trim());
                intent.putExtra("address", etAddress.getText().toString().trim());
                intent.putExtra("complaint", etComplaint.getText().toString().trim());
                startActivity(intent);
            }
        });

        findViewById(R.id.nav_home_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
        
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, PatientsActivity.class));
            finish();
        });
    }

    private boolean validateForm() {
        if (etFirstName.getText().toString().trim().isEmpty() ||
            etLastName.getText().toString().trim().isEmpty() ||
            etAge.getText().toString().trim().isEmpty() ||
            etGender.getText().toString().trim().isEmpty() ||
            etPhone.getText().toString().trim().length() < 13 ||
            etAddress.getText().toString().trim().isEmpty() ||
            etComplaint.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please fill all mandatory (*) fields correctly", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        String email = etEmail.getText().toString().trim();
        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}