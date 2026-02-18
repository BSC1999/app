package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilNewPassword, tilRepeatPassword;
    private EditText etNewPassword, etRepeatPassword;
    private Button btnUpdatePassword;
    private ImageButton btnBack;
    private ProgressBar pbStrength;
    private TextView tvStrengthText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        tilNewPassword = findViewById(R.id.til_new_password);
        tilRepeatPassword = findViewById(R.id.til_repeat_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etRepeatPassword = findViewById(R.id.et_repeat_password);
        btnUpdatePassword = findViewById(R.id.btn_update_password);
        btnBack = findViewById(R.id.btn_back);
        pbStrength = findViewById(R.id.pb_strength);
        tvStrengthText = findViewById(R.id.tv_strength_text);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass = s.toString();
                tilRepeatPassword.setEnabled(!pass.isEmpty());
                updateStrength(pass);
                validateMatch();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etRepeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateMatch();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnUpdatePassword.setOnClickListener(v -> {
            String newPass = etNewPassword.getText().toString();
            String repeatPass = etRepeatPassword.getText().toString();

            if (newPass.equals(repeatPass)) {
                // Update the password in LoginActivity
                LoginActivity.setUpdatedPassword(newPass);
                
                Toast.makeText(this, "Password Updated Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStrength(String password) {
        int score = 0;
        if (password.length() >= 6) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[@#$%^&+=].*")) score++;

        pbStrength.setProgress(score);
        switch (score) {
            case 1: tvStrengthText.setText("Weak"); break;
            case 2: tvStrengthText.setText("Medium"); break;
            case 3: tvStrengthText.setText("Strong"); break;
            case 4: tvStrengthText.setText("Very Strong"); break;
            default: tvStrengthText.setText("Too Short"); break;
        }
    }

    private void validateMatch() {
        String pass1 = etNewPassword.getText().toString();
        String pass2 = etRepeatPassword.getText().toString();
        btnUpdatePassword.setEnabled(!pass1.isEmpty() && pass1.equals(pass2));
    }
}