package com.simats.afinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvPendingCount;
    private TextView tvTotalAppCount;
    private ShapeableImageView ivProfile;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        XrayReportManager.init(this);
        UserManager.loadProfileData(this);

        tvPendingCount = findViewById(R.id.tv_pending_reports_count);
        tvTotalAppCount = findViewById(R.id.tv_total_app_count);
        ivProfile = findViewById(R.id.iv_profile);
        tvWelcome = findViewById(R.id.tv_welcome);

        // Click on Profile Icon to go to DoctorProfileActivity
        ivProfile.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, DoctorProfileActivity.class));
        });

        // --- Summary Cards ---
        MaterialCardView cardTotalApp = findViewById(R.id.card_total_appointments);
        if (cardTotalApp != null) {
            cardTotalApp.setOnClickListener(v -> {
                startActivity(new Intent(DashboardActivity.this, TodayAppointmentActivity.class));
            });
        }

        MaterialCardView cardPendingReports = findViewById(R.id.card_pending_reports);
        if (cardPendingReports != null) {
            cardPendingReports.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, UploadImagesActivity.class);
                intent.putExtra("mode", "view_only");
                startActivity(intent);
            });
        }

        // --- Quick Actions ---
        findViewById(R.id.ll_add_patient).setOnClickListener(v -> startActivity(new Intent(this, AddPatientActivity.class)));
        findViewById(R.id.ll_upload_xray).setOnClickListener(v -> {
            // Mawa, checking role for patient redirection
            if ("Admin".equalsIgnoreCase(UserManager.getCurrentRole())) {
                startActivity(new Intent(this, PatientDatabaseActivity.class));
            } else {
                startActivity(new Intent(this, PatientsActivity.class));
            }
        });
        findViewById(R.id.ll_write_prescription).setOnClickListener(v -> startActivity(new Intent(this, PrescriptionActivity.class)));

        // --- Bottom Navigation ---
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> {});
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> {
            // Mawa, checking role for navigation redirection
            if ("Admin".equalsIgnoreCase(UserManager.getCurrentRole())) {
                startActivity(new Intent(this, PatientDatabaseActivity.class));
            } else {
                startActivity(new Intent(this, PatientsActivity.class));
            }
            finish();
        });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { startActivity(new Intent(this, TodayAppointmentActivity.class)); finish(); });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { startActivity(new Intent(this, UploadImagesActivity.class)); finish(); });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        // Sync Welcome Text & Profile Pic based on logged in user
        tvWelcome.setText("Welcome " + UserManager.getCurrentDoctorName());
        
        String profileUri = UserManager.getProfileUri(this);
        if (!profileUri.isEmpty()) {
            try {
                // Check if we still have permission to open this URI
                getContentResolver().takePersistableUriPermission(Uri.parse(profileUri), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                ivProfile.setImageURI(Uri.parse(profileUri));
            } catch (Exception e) {
                // Fallback to img150 if error
                ivProfile.setImageResource(R.drawable.img150);
                UserManager.setProfileUri(this, "");
            }
        } else {
            ivProfile.setImageResource(R.drawable.img150); // Default set to img150
        }

        if (tvPendingCount != null) {
            tvPendingCount.setText(String.valueOf(XrayReportManager.getReportCount(this)));
        }

        if (tvTotalAppCount != null) {
            String role = UserManager.getCurrentRole();
            int count = 0;
            if ("Doctor".equalsIgnoreCase(role)) {
                count = PatientDataManager.getScheduledPatientsForToday(UserManager.getCurrentDoctorName()).size();
            } else {
                count = PatientDataManager.getScheduledPatientsForToday().size();
            }
            tvTotalAppCount.setText(String.valueOf(count));
        }
    }
}