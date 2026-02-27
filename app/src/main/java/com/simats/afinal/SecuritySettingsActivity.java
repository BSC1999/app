package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SecuritySettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_settings);

        // Header Back Button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Password Policies Click
        findViewById(R.id.btn_password_policies).setOnClickListener(v -> {
            Toast.makeText(this, "Managing Password Policies...", Toast.LENGTH_SHORT).show();
        });

        // Audit Logs Click
        findViewById(R.id.btn_view_audit_logs).setOnClickListener(v -> {
            startActivity(new Intent(this, SystemActivityLogsActivity.class));
        });

        // Auth Methods Click
        findViewById(R.id.btn_auth_methods).setOnClickListener(v -> {
            Toast.makeText(this, "Managing Authentication Methods...", Toast.LENGTH_SHORT).show();
        });

        // 2FA Switch Logic
        MaterialSwitch switch2fa = findViewById(R.id.switch_2fa);
        switch2fa.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "AI: 2FA Enabled for increased security.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "AI Warning: 2FA Disabled. Admin account at risk.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
