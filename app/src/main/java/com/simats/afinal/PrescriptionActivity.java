package com.simats.afinal;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.OutputStream;

public class PrescriptionActivity extends AppCompatActivity {

    private EditText etNotepad;
    private static String savedDraft = ""; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        etNotepad = findViewById(R.id.et_prescription_notepad);
        
        if (!savedDraft.isEmpty()) {
            etNotepad.setText(savedDraft);
        }

        findViewById(R.id.btn_close).setOnClickListener(v -> {
            saveDraft();
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        findViewById(R.id.btn_save_pdf).setOnClickListener(v -> {
            String content = etNotepad.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show();
            } else {
                generatePDF(content);
            }
        });

        // Bottom Nav Redirection
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> {
            saveDraft();
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> {
            saveDraft();
            startActivity(new Intent(this, PatientsActivity.class));
            finish();
        });

        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> {
            saveDraft();
            startActivity(new Intent(this, TodayAppointmentActivity.class));
            finish();
        });

        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> {
            saveDraft();
            startActivity(new Intent(this, UploadImagesActivity.class));
            finish();
        });

        findViewById(R.id.nav_more_custom).setOnClickListener(v -> {
            saveDraft();
            startActivity(new Intent(this, MoreActivity.class));
            finish();
        });
    }

    private void generatePDF(String text) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        paint.setTextSize(16);
        int x = 40, y = 50;

        Paint titlePaint = new Paint();
        titlePaint.setTextSize(24);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("Prescription", 220, y, titlePaint);
        y += 40;

        for (String line : text.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += 25;
            if (y > 800) break; 
        }

        pdfDocument.finishPage(myPage);

        String fileName = "Prescription_" + System.currentTimeMillis() + ".pdf";
        
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
                    etNotepad.setText("");
                    savedDraft = "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }

    private void saveDraft() {
        savedDraft = etNotepad.getText().toString();
    }

    @Override
    public void onBackPressed() {
        saveDraft();
        super.onBackPressed();
    }
}