package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MedicalDentalHistoryActivity extends AppCompatActivity {

    private EditText etNotes;
    private String firstName, lastName, ageInput, gender, phone, email, address, complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_dental_history);

        // Receive data from AddPatientActivity
        Intent intent = getIntent();
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        ageInput = intent.getStringExtra("age_input"); // Retrieve age
        gender = intent.getStringExtra("gender");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");
        address = intent.getStringExtra("address");
        complaint = intent.getStringExtra("complaint");

        etNotes = findViewById(R.id.et_notes);

        // Back to Add Patient
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            finish(); 
        });

        // Save Button - Finalizing Patient Profile
        findViewById(R.id.btn_save).setOnClickListener(v -> {
            String fullName = firstName + " " + lastName;
            String patientId = String.valueOf((int)(Math.random() * 900000) + 100000);
            
            boolean isFemale = "Female".equalsIgnoreCase(gender);
            
            // Parse age from input
            int age = 0;
            try {
                age = Integer.parseInt(ageInput);
            } catch (Exception e) {
                age = 0;
            }
            
            String historyNotes = etNotes.getText().toString().trim();

            // Fixed: Providing all 15 required arguments to PatientInfo constructor
            // Order: name, id, age, dob, gender, phone, email, address, complaint, medicalHistory, treatmentPaymentInfo, lastVisit, isFemale, addedTimestamp, assignedDoctor
            PatientInfo newPatient = new PatientInfo(
                    fullName, 
                    patientId, 
                    age,            // Direct age from input
                    "N/A",          // DOB placeholder (removed from form)
                    gender, 
                    phone, 
                    email, 
                    address, 
                    complaint,      
                    historyNotes,   
                    "No treatment/payment info recorded",
                    "Just now", 
                    isFemale, 
                    System.currentTimeMillis(),
                    UserManager.getCurrentDoctorName()
            );
            
            PatientDataManager.addPatient(newPatient);

            Toast.makeText(this, "Patient Profile Created Successfully!", Toast.LENGTH_LONG).show();
            
            Intent patientsIntent = new Intent(MedicalDentalHistoryActivity.this, PatientsActivity.class);
            patientsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(patientsIntent);
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
    }
}