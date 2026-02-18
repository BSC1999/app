package com.simats.afinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodayAppointmentActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private AppointmentAdapter adapter;
    private List<PatientInfo> patientList;
    private TextView tvCurrentDate;
    private DateChangeReceiver dateChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_appointment);

        tvCurrentDate = findViewById(R.id.tv_current_date);
        updateDateDisplay();

        rvAppointments = findViewById(R.id.rv_appointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));

        loadScheduledPatients();

        // --- Bottom Navigation ---
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> {
            startActivity(new Intent(this, PatientsActivity.class));
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

    private void loadScheduledPatients() {
        String role = UserManager.getCurrentRole();
        String doctorName = UserManager.getCurrentDoctorName();
        
        if ("Doctor".equalsIgnoreCase(role)) {
            patientList = PatientDataManager.getScheduledPatientsForToday(doctorName);
        } else {
            patientList = PatientDataManager.getScheduledPatientsForToday();
        }

        // Updated to use the new constructor: AppointmentAdapter(Context, List<PatientInfo>)
        adapter = new AppointmentAdapter(this, patientList);
        rvAppointments.setAdapter(adapter);
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
        tvCurrentDate.setText(sdf.format(new Date()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        dateChangeReceiver = new DateChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        registerReceiver(dateChangeReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dateChangeReceiver != null) {
            unregisterReceiver(dateChangeReceiver);
        }
    }

    private class DateChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateDateDisplay();
            loadScheduledPatients(); // Refresh list automatically at 12 AM
        }
    }
}