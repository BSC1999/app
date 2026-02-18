package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        MaterialButton btnLogout = findViewById(R.id.btn_logout);
        MaterialButton btnCancel = findViewById(R.id.btn_cancel);

        btnLogout.setOnClickListener(v -> {
            // Log the logout event
            LogManager.addLog("User", UserManager.getCurrentRole(), "Logged out", "192.168.1.1");
            
            Intent intent = new Intent(LogoutActivity.this, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}