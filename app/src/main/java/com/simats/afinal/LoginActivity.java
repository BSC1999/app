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
    private static String currentPassword = "Saveetha_Dental"; // Updated password

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

                if (id.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "ID cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(currentPassword)) {
                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                } else {
                    // Mawa, setting role based on ID for validation
                    String role = validateRoleById(id);
                    
                    if (role != null) {
                        UserManager.setCurrentRole(role);
                        UserManager.login(LoginActivity.this, id);

                        // Log the login event
                        LogManager.addLog(role, role, "Logged into the system", "192.168.1.1");
                        
                        Intent intent;
                        if ("Admin".equalsIgnoreCase(role)) {
                            // Mawa, skipping disclaimer for Admin and going straight to dashboard
                            intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        } else {
                            // Redirect to Disclaimer for other roles
                            intent = new Intent(LoginActivity.this, DisclaimerActivity.class);
                            intent.putExtra("target_role", role);
                        }
                        startActivity(intent);
                        if ("Admin".equalsIgnoreCase(role)) finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid ID or Role not found", Toast.LENGTH_SHORT).show();
                    }
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

    private String validateRoleById(String id) {
        // Mawa, specific admin ID check
        if ("ADMIN_777".equals(id)) return "Admin";
        
        // Prefix based logic
        if (id.startsWith("111")) return "Dental Doctor";
        if (id.startsWith("222")) return "Consultant";
        if (id.startsWith("333")) return "Dental Intern / Assistant";
        if (id.startsWith("000")) return "Admin";
        
        return "Dental Doctor"; // Default fallback
    }

    // Method to update password after reset
    public static void setUpdatedPassword(String newPassword) {
        currentPassword = newPassword;
    }
}