package com.simats.afinal;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.imageview.ShapeableImageView;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Locale;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class PatientProfileActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private EditText etTreatmentNotepad;
    private PatientInfo currentPatient;
    private TextView tvNextScheduleDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        // --- Views ---
        ShapeableImageView ivProfilePic = findViewById(R.id.iv_profile_pic);
        TextView tvName = findViewById(R.id.tv_profile_name);
        TextView tvId = findViewById(R.id.tv_profile_id);
        TextView tvAgeGender = findViewById(R.id.tv_profile_age_gender);
        
        TextView tvPhone = findViewById(R.id.tv_display_phone);
        TextView tvAddress = findViewById(R.id.tv_display_address);
        TextView tvInitialComplaint = findViewById(R.id.tv_display_initial_complaint);
        TextView tvMedicalHistory = findViewById(R.id.tv_display_medical_history);
        etTreatmentNotepad = findViewById(R.id.et_treatment_payment_notepad);
        tvNextScheduleDate = findViewById(R.id.tv_display_next_schedule);

        // --- Get Patient Data ---
        currentPatient = (PatientInfo) getIntent().getSerializableExtra("patient_data");

        if (currentPatient != null) {
            updateUI();
        }

        // --- Back Button Redirects to PatientsActivity ---
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            startActivity(new Intent(this, PatientsActivity.class));
            finish();
        });

        // --- Fix Date Button Redirects to NextScheduleActivity ---
        findViewById(R.id.btn_fix_schedule).setOnClickListener(v -> {
            Intent intent = new Intent(this, NextScheduleActivity.class);
            intent.putExtra("patient_data", currentPatient);
            startActivity(intent);
        });

        // Auto-save logic for notepad
        etTreatmentNotepad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (currentPatient != null) {
                    currentPatient.setTreatmentPaymentInfo(s.toString());
                    PatientDataManager.updatePatient(currentPatient);
                }
            }
        });

        // Save as PDF Logic
        findViewById(R.id.btn_profile_save_pdf).setOnClickListener(v -> {
            if (currentPatient != null) generatePDF(currentPatient.getName(), etTreatmentNotepad.getText().toString().trim());
        });

        // X-ray Manager
        XrayReportManager.init(this);
        syncXrayVisibility();

        // Camera & Gallery Launchers
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                String dummyUri = "captured_" + System.currentTimeMillis();
                XrayReportManager.addImageReport(this, dummyUri);
                Toast.makeText(this, "X-ray captured & report pending", Toast.LENGTH_SHORT).show();
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                XrayReportManager.addImageReport(this, uri.toString());
                Toast.makeText(this, "X-ray uploaded & report pending", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_profile_camera).setOnClickListener(v -> cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)));
        findViewById(R.id.btn_profile_upload).setOnClickListener(v -> galleryLauncher.launch("image/*"));

        setupXrayMenu(R.id.btn_menu_xray_1, R.id.card_xray_1, R.drawable.img_23);
        setupXrayMenu(R.id.btn_menu_xray_2, R.id.card_xray_2, R.drawable.img_24);
        setupXrayClick(R.id.iv_xray_item_1, R.drawable.img_23);
        setupXrayClick(R.id.iv_xray_item_2, R.drawable.img_24);

        // Navigation
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { startActivity(new Intent(this, TodayAppointmentActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { startActivity(new Intent(this, UploadImagesActivity.class)); finish(); });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentPatient != null) {
            for (PatientInfo p : PatientDataManager.getAllPatients()) {
                if (p.getId().equals(currentPatient.getId())) {
                    currentPatient = p;
                    break;
                }
            }
            updateUI();
        }
    }

    private void updateUI() {
        TextView tvName = findViewById(R.id.tv_profile_name);
        TextView tvId = findViewById(R.id.tv_profile_id);
        TextView tvAgeGender = findViewById(R.id.tv_profile_age_gender);
        TextView tvPhone = findViewById(R.id.tv_display_phone);
        TextView tvAddress = findViewById(R.id.tv_display_address);
        TextView tvInitialComplaint = findViewById(R.id.tv_display_initial_complaint);
        TextView tvMedicalHistory = findViewById(R.id.tv_display_medical_history);
        ShapeableImageView ivProfilePic = findViewById(R.id.iv_profile_pic);

        tvName.setText(currentPatient.getName());
        tvId.setText("ID: " + currentPatient.getId());
        tvAgeGender.setText("Age: " + currentPatient.getAge() + " | Gender: " + (currentPatient.getGender() != null ? currentPatient.getGender() : "N/A"));
        
        if (currentPatient.isFemale()) {
            ivProfilePic.setImageResource(R.drawable.img1001);
        } else {
            ivProfilePic.setImageResource(R.drawable.img1000);
        }
        
        tvPhone.setText(currentPatient.getPhone() != null ? currentPatient.getPhone() : "N/A");
        tvAddress.setText(currentPatient.getAddress() != null && !currentPatient.getAddress().isEmpty() ? currentPatient.getAddress() : "No address provided");
        tvInitialComplaint.setText(currentPatient.getComplaint() != null && !currentPatient.getComplaint().isEmpty() ? currentPatient.getComplaint() : "No complaint recorded");
        tvMedicalHistory.setText(currentPatient.getMedicalHistory() != null && !currentPatient.getMedicalHistory().isEmpty() ? currentPatient.getMedicalHistory() : "No medical history recorded");
        
        if (currentPatient.getNextScheduleDate() != null && !currentPatient.getNextScheduleDate().isEmpty()) {
            String scheduleInfo = currentPatient.getNextScheduleDate();
            if (currentPatient.getNextScheduleTime() != null && !currentPatient.getNextScheduleTime().isEmpty()) {
                scheduleInfo += " | " + currentPatient.getNextScheduleTime();
            }
            tvNextScheduleDate.setText(scheduleInfo);
        } else {
            tvNextScheduleDate.setText("No appointment fixed");
        }

        if (currentPatient.getTreatmentPaymentInfo() != null) {
            etTreatmentNotepad.setText(currentPatient.getTreatmentPaymentInfo());
        }
    }

    private void generatePDF(String patientName, String text) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();
        paint.setTextSize(16);
        int x = 40, y = 50;
        Paint titlePaint = new Paint();
        titlePaint.setTextSize(22);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("Treatment & Payment: " + patientName, 40, y, titlePaint);
        y += 40;
        for (String line : text.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += 25;
            if (y > 800) break;
        }
        pdfDocument.finishPage(myPage);
        String fileName = "Patient_" + patientName.replace(" ", "_") + "_Report.pdf";
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
        Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
        try {
            if (uri != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    pdfDocument.writeTo(outputStream);
                    outputStream.close();
                    Toast.makeText(this, "PDF Saved to Downloads!", Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        pdfDocument.close();
    }

    private void syncXrayVisibility() {
        if (XrayReportManager.isDeleted(this, R.drawable.img_23)) findViewById(R.id.card_xray_1).setVisibility(View.GONE);
        if (XrayReportManager.isDeleted(this, R.drawable.img_24)) findViewById(R.id.card_xray_2).setVisibility(View.GONE);
    }

    private void setupXrayMenu(int buttonId, int cardId, int resId) {
        ImageButton btnMenu = findViewById(buttonId);
        View card = findViewById(cardId);
        if (btnMenu != null && card != null) {
            btnMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(this, v);
                popup.getMenu().add("Delete");
                popup.setOnMenuItemClickListener(item -> {
                    if ("Delete".equals(item.getTitle())) {
                        card.setVisibility(View.GONE);
                        XrayReportManager.deleteImage(this, resId);
                        Toast.makeText(this, "X-ray deleted", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                });
                popup.show();
            });
        }
    }

    private void setupXrayClick(int viewId, int resId) {
        View iv = findViewById(viewId);
        if (iv != null) {
            iv.setOnClickListener(v -> {
                Intent intent = new Intent(this, XrayActivity.class);
                intent.putExtra("selected_image", resId);
                intent.putExtra("from_patient_profile", true);
                intent.putExtra("patient_data", currentPatient);
                startActivity(intent);
            });
        }
    }
}
