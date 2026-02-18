package com.simats.afinal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class TermsOfServiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}