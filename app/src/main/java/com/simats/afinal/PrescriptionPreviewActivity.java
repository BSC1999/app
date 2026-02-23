package com.simats.afinal;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PrescriptionPreviewActivity extends AppCompatActivity {

    private String selectedPlan = "";
    private String selectedTooth = "";
    private String diagnosis = "";
    private PatientInfo patientData;
    private ArrayList<DrugInfo> drugList = new ArrayList<>();
    private SignatureView signatureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_preview);

        signatureView = findViewById(R.id.signature_view);

        // Get data from Intent
        selectedPlan = getIntent().getStringExtra("selected_plan");
        selectedTooth = getIntent().getStringExtra("selected_tooth");
        diagnosis = getIntent().getStringExtra("selected_diagnosis");
        patientData = (PatientInfo) getIntent().getSerializableExtra("patient_data");
        
        // Simulating the drug list from previous screen
        setupDrugData();
        setupUI();

        // Top Left Back Button -> AIDrugRecommendationActivity
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Clear Signature
        findViewById(R.id.btn_clear_signature).setOnClickListener(v -> {
            if (signatureView != null) signatureView.clear();
        });

        // Download Button
        findViewById(R.id.btn_download_prescription).setOnClickListener(v -> {
            generatePrescriptionPDF();
        });

        // Bottom Navigation
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { startActivity(new Intent(this, TodayAppointmentActivity.class)); finish(); });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { /* Already here */ });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    private void setupDrugData() {
        if ("plan1".equals(selectedPlan)) {
            drugList.add(new DrugInfo("ANTIBIOTIC", "Amoxicillin 500mg", "3 times daily for 5 days"));
            drugList.add(new DrugInfo("PAINKILLER", "Ibuprofen 400mg", "Every 6 hours as needed"));
            drugList.add(new DrugInfo("MOUTH RINSE", "Chlorhexidine 0.12%", "Twice daily rinse"));
        } else {
            drugList.add(new DrugInfo("ANTIBIOTIC", "Augmentin 625mg", "Twice daily for 7 days"));
            drugList.add(new DrugInfo("PAINKILLER", "Ketorolac 10mg", "Every 8 hours for 3 days"));
            drugList.add(new DrugInfo("ANTI-INFLAMMATORY", "Dexamethasone 4mg", "Once daily for 2 days"));
        }
    }

    private void setupUI() {
        // Doctor Name from Login
        TextView tvDoctorName = findViewById(R.id.tv_doctor_name_disp);
        tvDoctorName.setText("Dr. " + UserManager.getCurrentDoctorName());

        // Patient Details
        if (patientData != null) {
            TextView tvPatient = findViewById(R.id.tv_patient_name_preview);
            tvPatient.setText(patientData.getName() + ", " + patientData.getAge());
        }

        // Current Date
        TextView tvDate = findViewById(R.id.tv_current_date);
        String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        tvDate.setText(date);

        // Diagnosis & Treatment
        TextView tvDiag = findViewById(R.id.tv_diagnosis_summary);
        tvDiag.setText(diagnosis != null ? diagnosis : "General Examination");

        TextView tvTreat = findViewById(R.id.tv_treatment_summary);
        if ("plan1".equals(selectedPlan)) {
            tvTreat.setText("Root Canal Therapy, Crown Placement");
        } else {
            tvTreat.setText("Surgical Extraction, Dental Implant");
        }

        // Medications
        LinearLayout container = findViewById(R.id.ll_prescribed_meds_container);
        container.removeAllViews();
        for (DrugInfo drug : drugList) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_drug_recommendation, container, false);
            ((TextView) view.findViewById(R.id.tv_drug_type)).setText(drug.type);
            ((TextView) view.findViewById(R.id.tv_drug_name)).setText(drug.name);
            ((TextView) view.findViewById(R.id.tv_drug_dosage)).setText(drug.dosage);
            container.addView(view);
        }
    }

    private void generatePrescriptionPDF() {
        View content = findViewById(R.id.prescription_content);
        
        if (content == null || content.getWidth() <= 0 || content.getHeight() <= 0) {
            Toast.makeText(this, "Wait for screen to load fully", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap = Bitmap.createBitmap(content.getWidth(), content.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        content.draw(canvas);

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        
        page.getCanvas().drawBitmap(bitmap, 0, 0, null);
        pdfDocument.finishPage(page);

        String fileName = "Prescription_" + (patientData != null ? patientData.getName().replace(" ", "_") : "Patient") + ".pdf";
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
                    Toast.makeText(this, "Prescription saved to Downloads!", Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }

    private static class DrugInfo {
        String type, name, dosage;
        DrugInfo(String t, String n, String d) { type = t; name = n; dosage = d; }
    }
}
