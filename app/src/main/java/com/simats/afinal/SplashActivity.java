package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isNavigated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View root = findViewById(R.id.splash_root);
        TextView tvHint = findViewById(R.id.tv_click_hint);

        // Show hint after 5 seconds
        handler.postDelayed(() -> {
            if (!isNavigated) {
                tvHint.setVisibility(View.VISIBLE);
            }
        }, 5000);

        // Click anywhere to navigate
        root.setOnClickListener(v -> navigateToStart());
    }

    private void navigateToStart() {
        if (!isNavigated) {
            isNavigated = true;
            handler.removeCallbacksAndMessages(null);
            Intent intent = new Intent(SplashActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    }
}