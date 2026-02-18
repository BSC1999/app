package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AuditLogsActivity extends AppCompatActivity {

    private RecyclerView rvLogs;
    private LogAdapter adapter;
    private List<LogEntry> allLogs;
    private List<LogEntry> filteredLogs;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_logs);

        rvLogs = findViewById(R.id.rv_audit_logs);
        etSearch = findViewById(R.id.et_search_logs);

        allLogs = LogManager.getLogs();
        filteredLogs = new ArrayList<>(allLogs);

        adapter = new LogAdapter(filteredLogs);
        rvLogs.setLayoutManager(new LinearLayoutManager(this));
        rvLogs.setAdapter(adapter);

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

        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            });
        }

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

    private void filter(String text) {
        filteredLogs.clear();
        for (LogEntry log : allLogs) {
            if (log.getUserName().toLowerCase().contains(text.toLowerCase()) || 
                log.getActivity().toLowerCase().contains(text.toLowerCase())) {
                filteredLogs.add(log);
            }
        }
        adapter.notifyDataSetChanged();
    }
}