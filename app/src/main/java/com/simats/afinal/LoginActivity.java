package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText etId, etPassword;
    private TextView tvForgotPassword;
    private static String currentPassword = "welcome"; // Default password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etId = findViewById(R.id.et_id);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etId.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (id.length() != 7) {
                    Toast.makeText(LoginActivity.this, "ID must be exactly 7 numbers", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(currentPassword)) {
                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                } else {
                    // Initialize user session with ID
                    UserManager.login(LoginActivity.this, id);

                    // Log the login event
                    LogManager.addLog("Doctor", UserManager.getCurrentRole(), "Logged into the system", "192.168.1.1");
                    
                    // Redirect to DisclaimerActivity
                    Intent intent = new Intent(LoginActivity.this, DisclaimerActivity.class);
                    startActivity(intent);
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to update password after reset
    public static void setUpdatedPassword(String newPassword) {
        currentPassword = newPassword;
    }
}