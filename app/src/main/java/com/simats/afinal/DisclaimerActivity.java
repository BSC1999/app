package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class DisclaimerActivity extends AppCompatActivity {

    private CheckBox cbAcknowledge;
    private Button btnAgree;
    private ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        cbAcknowledge = findViewById(R.id.cb_acknowledge);
        btnAgree = findViewById(R.id.btn_agree);
        btnClose = findViewById(R.id.btn_close);

        // Close goes back to Login
        btnClose.setOnClickListener(v -> finish());

        cbAcknowledge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnAgree.setEnabled(isChecked);
        });

        btnAgree.setOnClickListener(v -> {
            String role = UserManager.getCurrentRole();
            Intent intent;

            // Mawa, redirecting to different dashboards based on role
            if ("Admin".equalsIgnoreCase(role)) {
                intent = new Intent(DisclaimerActivity.this, AdminDashboardActivity.class);
            } else {
                intent = new Intent(DisclaimerActivity.this, DashboardActivity.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}