package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class OtpActivity extends AppCompatActivity {

    private EditText[] etOtps = new EditText[6];
    private Button btnVerify;
    private ImageButton btnBack;
    private TextView tvResend, tvInstruction;
    private CountDownTimer countDownTimer;
    private boolean isResendEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        etOtps[0] = findViewById(R.id.et_otp1);
        etOtps[1] = findViewById(R.id.et_otp2);
        etOtps[2] = findViewById(R.id.et_otp3);
        etOtps[3] = findViewById(R.id.et_otp4);
        etOtps[4] = findViewById(R.id.et_otp5);
        etOtps[5] = findViewById(R.id.et_otp6);
        btnVerify = findViewById(R.id.btn_verify);
        btnBack = findViewById(R.id.btn_back);
        tvResend = findViewById(R.id.tv_resend_code);
        tvInstruction = findViewById(R.id.tv_instruction);

        String userEmail = getIntent().getStringExtra("user_email");
        if (userEmail != null && !userEmail.isEmpty()) {
            tvInstruction.setText("We have sent a 6-digit code to " + userEmail);
        }

        btnBack.setOnClickListener(v -> finish());

        startResendTimer();

        tvResend.setOnClickListener(v -> {
            if (isResendEnabled) {
                Toast.makeText(this, "New OTP Resent!", Toast.LENGTH_SHORT).show();
                startResendTimer();
            } else {
                Toast.makeText(this, "Please wait for the timer to finish", Toast.LENGTH_SHORT).show();
            }
        });

        setupOtpInputs();

        btnVerify.setOnClickListener(v -> {
            // Validation removed as per request - allow any OTP to proceed
            Toast.makeText(this, "OTP Verified!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OtpActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupOtpInputs() {
        for (int i = 0; i < 6; i++) {
            final int index = i;
            
            // Handle forward movement
            etOtps[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < 5) {
                        etOtps[index + 1].requestFocus();
                    }
                    checkOtpComplete();
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Handle backspace movement
            etOtps[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (etOtps[index].getText().toString().isEmpty() && index > 0) {
                        etOtps[index - 1].requestFocus();
                        etOtps[index - 1].setText(""); // Optional: clear previous box on backspace
                    }
                }
                return false;
            });
        }
    }

    private void startResendTimer() {
        isResendEnabled = false;
        tvResend.setTextColor(getResources().getColor(android.R.color.darker_gray));
        
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                tvResend.setText(String.format(Locale.getDefault(), "Resend Code in %02d s", seconds));
            }

            @Override
            public void onFinish() {
                isResendEnabled = true;
                tvResend.setText("Resend Code");
                tvResend.setTextColor(getResources().getColor(R.color.nav_icon_unselected));
            }
        }.start();
    }

    private void checkOtpComplete() {
        boolean allFilled = true;
        for (EditText et : etOtps) {
            if (et.getText().toString().trim().isEmpty()) {
                allFilled = false;
                break;
            }
        }
        btnVerify.setEnabled(allFilled);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}