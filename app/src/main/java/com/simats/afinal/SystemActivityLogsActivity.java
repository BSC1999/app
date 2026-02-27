package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.util.ArrayList;
import java.util.List;

public class SystemActivityLogsActivity extends AppCompatActivity {

    private LinearLayout llLogsContainer;
    private TextView tvPaginationInfo, tvHealthPercent, tvAlertCount, tvAlertDesc;
    private EditText etSearch;
    private Spinner spinnerRoles;
    private List<LogEntry> allLogs;
    private List<LogEntry> filteredLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_activity_logs);

        llLogsContainer = findViewById(R.id.ll_logs_list_container);
        tvPaginationInfo = findViewById(R.id.tv_log_pagination_info);
        tvHealthPercent = findViewById(R.id.tv_health_percent);
        tvAlertCount = findViewById(R.id.tv_alert_count);
        tvAlertDesc = findViewById(R.id.tv_alert_desc);
        etSearch = findViewById(R.id.et_search_logs);
        spinnerRoles = findViewById(R.id.spinner_roles);

        allLogs = LogManager.getLogs();
        filteredLogs = new ArrayList<>(allLogs);

        setupFilters();
        loadLogs();
        setupAIMonitoring();

        // Back Button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Date Range
        findViewById(R.id.btn_log_date_range).setOnClickListener(v -> {
            MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Select Activity Range")
                    .build();
            picker.show(getSupportFragmentManager(), "LOG_DATE_PICKER");
            picker.addOnPositiveButtonClickListener(selection -> {
                Toast.makeText(this, "Fetching logs for selected range...", Toast.LENGTH_SHORT).show();
            });
        });

        // Bottom Nav
        setupBottomNav();
    }

    private void setupFilters() {
        String[] roles = {"All User Roles", "Doctor", "Consultant", "Assistant"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoles.setAdapter(adapter);

        spinnerRoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterLogs();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLogs();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterLogs() {
        String query = etSearch.getText().toString().toLowerCase();
        String selectedRole = spinnerRoles.getSelectedItem().toString();

        filteredLogs.clear();
        for (LogEntry log : allLogs) {
            boolean matchesSearch = log.getUserName().toLowerCase().contains(query) || log.getActivity().toLowerCase().contains(query);
            boolean matchesRole = selectedRole.equals("All User Roles") || log.getRole().equalsIgnoreCase(selectedRole);

            if (matchesSearch && matchesRole) {
                filteredLogs.add(log);
            }
        }
        loadLogs();
    }

    private void loadLogs() {
        llLogsContainer.removeAllViews();
        int limit = Math.min(filteredLogs.size(), 50);
        for (int i = 0; i < limit; i++) {
            addLogRow(filteredLogs.get(i));
        }
        tvPaginationInfo.setText("Showing 1 to " + limit + " of " + filteredLogs.size() + " logs");
    }

    private void addLogRow(LogEntry log) {
        // Mawa, using the new horizontal item row layout
        View row = LayoutInflater.from(this).inflate(R.layout.item_system_log_row, llLogsContainer, false);

        TextView tvUser = row.findViewById(R.id.tv_log_user_name);
        TextView tvAction = row.findViewById(R.id.tv_log_action);
        TextView tvStatus = row.findViewById(R.id.tv_log_status_name);
        TextView tvDateTime = row.findViewById(R.id.tv_log_datetime);

        tvUser.setText(log.getUserName() + " (" + log.getRole() + ")");
        tvAction.setText(log.getActivity());
        tvStatus.setText("Success"); // Status simulation
        tvDateTime.setText(log.getTimestamp());

        llLogsContainer.addView(row);
    }

    private void setupAIMonitoring() {
        tvHealthPercent.setText("98%");
        int issueCount = 0;
        tvAlertCount.setText(String.valueOf(issueCount));
        if (issueCount == 0) {
            tvAlertDesc.setText("AI Monitor: No critical security violations detected in the last 24 hours.");
        } else {
            tvAlertDesc.setText("AI Monitor: " + issueCount + " unauthorized access attempts detected.");
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, AdminDashboardActivity.class)); finish(); });
        findViewById(R.id.nav_staff_custom).setOnClickListener(v -> { startActivity(new Intent(this, ManageDoctorsActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientDatabaseActivity.class)); finish(); });
        findViewById(R.id.nav_logs_custom).setOnClickListener(v -> {}); // Already here
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, AdminSettingsActivity.class)); finish(); });
    }
}
