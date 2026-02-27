package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class SelectRoleActivity extends AppCompatActivity {

    private MaterialCardView cardAdmin, cardDoctor, cardConsultant, cardAssistant;
    private Button btnConfirm;
    private ImageButton btnBack;
    private MaterialCardView selectedSection = null;
    private String selectedRole = "Doctor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);

        cardAdmin = findViewById(R.id.card_admin);
        cardDoctor = findViewById(R.id.card_doctor);
        cardConsultant = findViewById(R.id.card_consultant);
        cardAssistant = findViewById(R.id.card_assistant);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        View.OnClickListener roleSelectListener = v -> {
            resetCards();
            selectedSection = (MaterialCardView) v;
            selectedSection.setStrokeColor(getResources().getColor(android.R.color.holo_blue_light));
            selectedSection.setStrokeWidth(6);
            btnConfirm.setEnabled(true);

            if (v.getId() == R.id.card_admin) selectedRole = "Admin";
            else if (v.getId() == R.id.card_doctor) selectedRole = "Dental Doctor";
            else if (v.getId() == R.id.card_consultant) selectedRole = "Consultant";
            else if (v.getId() == R.id.card_assistant) selectedRole = "Dental Intern / Assistant";
        };

        cardAdmin.setOnClickListener(roleSelectListener);
        cardDoctor.setOnClickListener(roleSelectListener);
        cardConsultant.setOnClickListener(roleSelectListener);
        cardAssistant.setOnClickListener(roleSelectListener);

        btnConfirm.setOnClickListener(v -> {
            UserManager.setCurrentRole(selectedRole);
            Intent intent = new Intent(SelectRoleActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void resetCards() {
        int defaultStrokeColor = getResources().getColor(android.R.color.darker_gray);
        if (cardAdmin != null) {
            cardAdmin.setStrokeWidth(2);
            cardAdmin.setStrokeColor(defaultStrokeColor);
        }
        if (cardDoctor != null) {
            cardDoctor.setStrokeWidth(2);
            cardDoctor.setStrokeColor(defaultStrokeColor);
        }
        if (cardConsultant != null) {
            cardConsultant.setStrokeWidth(2);
            cardConsultant.setStrokeColor(defaultStrokeColor);
        }
        if (cardAssistant != null) {
            cardAssistant.setStrokeWidth(2);
            cardAssistant.setStrokeColor(defaultStrokeColor);
        }
    }
}