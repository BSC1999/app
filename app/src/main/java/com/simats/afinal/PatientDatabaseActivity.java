package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientDatabaseActivity extends AppCompatActivity {

    private LinearLayout llPatientsContainer;
    private TextView tvPaginationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_database);

        llPatientsContainer = findViewById(R.id.ll_patients_list_container);
        tvPaginationInfo = findViewById(R.id.tv_pagination_info);

        // Header Back Button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Date Range Picker
        findViewById(R.id.btn_date_range).setOnClickListener(v -> {
            MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Select Date Range")
                    .build();
            dateRangePicker.show(getSupportFragmentManager(), "DATE_RANGE_PICKER");
            dateRangePicker.addOnPositiveButtonClickListener(selection -> {
                Toast.makeText(this, "Filtering by date range...", Toast.LENGTH_SHORT).show();
            });
        });

        loadPatientData();
        setupBottomNav();
    }

    private void loadPatientData() {
        llPatientsContainer.removeAllViews();
        List<PatientInfo> patients = PatientDataManager.getAllPatients();

        for (PatientInfo p : patients) {
            addPatientRow(p);
        }

        tvPaginationInfo.setText("Showing 1 to " + patients.size() + " of " + patients.size() + " patients");
    }

    private void addPatientRow(PatientInfo patient) {
        View row = LayoutInflater.from(this).inflate(R.layout.item_patient_row, llPatientsContainer, false);
        
        TextView tvName = row.findViewById(R.id.tv_row_patient_name);
        TextView tvId = row.findViewById(R.id.tv_row_patient_id);
        TextView tvAge = row.findViewById(R.id.tv_row_patient_age);

        tvName.setText(patient.getName());
        tvId.setText("#PT-" + patient.getId());
        tvAge.setText(String.valueOf(patient.getAge()));

        row.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminPatientProfileActivity.class);
            intent.putExtra("patient_data", patient);
            startActivity(intent);
        });

        llPatientsContainer.addView(row);
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
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> {}); // Already here
        findViewById(R.id.nav_logs_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, SystemActivityLogsActivity.class));
            finish();
        });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> {
            // Mawa, strictly Admin Settings
            startActivity(new Intent(this, AdminSettingsActivity.class));
            finish();
        });
    }
}
