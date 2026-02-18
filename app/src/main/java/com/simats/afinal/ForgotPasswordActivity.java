package com.simats.afinal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.util.Patterns;
import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnGetOtp;
    private ImageButton btnBack;
    private static final String CHANNEL_ID = "OTP_CHANNEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.et_email);
        btnGetOtp = findViewById(R.id.btn_get_otp);
        btnBack = findViewById(R.id.btn_back);

        createNotificationChannel();

        btnBack.setOnClickListener(v -> finish());

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnGetOtp.setEnabled(isValidEmail(s.toString().trim()));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnGetOtp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (isValidEmail(email)) {
                // Generate a random 6-digit OTP
                String generatedOtp = String.valueOf(100000 + new Random().nextInt(900000));
                
                // Show as a real system notification
                showOtpNotification(generatedOtp);

                Intent intent = new Intent(ForgotPasswordActivity.this, OtpActivity.class);
                intent.putExtra("user_email", email);
                intent.putExtra("generated_otp", generatedOtp);
                startActivity(intent);
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "OTP Notification";
            String description = "Channel for OTP verification codes";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showOtpNotification(String otp) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Email Verification")
                .setContentText("Your 6-digit verification code is: " + otp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private boolean isValidEmail(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}