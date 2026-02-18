package com.simats.afinal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MedicalAICertificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_ai_certification);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}