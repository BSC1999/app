package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatientsActivity extends AppCompatActivity {

    private RecyclerView rvPatients;
    private PatientAdapter adapter;
    private List<PatientInfo> allPatients;
    private List<PatientInfo> filteredPatients;
    private EditText etSearch;
    private MaterialButton btnRecent, btnAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        rvPatients = findViewById(R.id.rv_patients);
        etSearch = findViewById(R.id.et_search);
        btnRecent = findViewById(R.id.btn_recent);
        btnAll = findViewById(R.id.btn_all);

        // Load data based on Role
        String role = UserManager.getCurrentRole();
        if ("Doctor".equalsIgnoreCase(role)) {
            // Show only this doctor's patients
            allPatients = PatientDataManager.getPatientsForDoctor(UserManager.getCurrentDoctorName());
        } else {
            // Consultant/Intern: Show all patients
            allPatients = PatientDataManager.getAllPatients();
        }

        filteredPatients = new ArrayList<>(allPatients);
        
        adapter = new PatientAdapter(filteredPatients);
        rvPatients.setLayoutManager(new LinearLayoutManager(this));
        rvPatients.setAdapter(adapter);

        // Search Logic
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filter Logic
        btnRecent.setOnClickListener(v -> {
            Collections.sort(filteredPatients, (p1, p2) -> Long.compare(p2.getAddedTimestamp(), p1.getAddedTimestamp()));
            adapter.notifyDataSetChanged();
        });

        btnAll.setOnClickListener(v -> {
            filteredPatients.clear();
            filteredPatients.addAll(allPatients);
            adapter.notifyDataSetChanged();
        });

        // FAB to add patient
        findViewById(R.id.fab_add_patient_container).setOnClickListener(v -> {
            startActivity(new Intent(PatientsActivity.this, AddPatientActivity.class));
        });

        // Back arrow
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                startActivity(new Intent(PatientsActivity.this, DashboardActivity.class));
                finish();
            });
        }

        // Navigation Logic
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
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

    private void filter(String text) {
        filteredPatients.clear();
        for (PatientInfo patient : allPatients) {
            if (patient.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredPatients.add(patient);
            }
        }
        adapter.notifyDataSetChanged();
    }
}