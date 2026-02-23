package com.simats.afinal;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import java.util.Set;

public class UploadImagesActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private GridLayout glImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);

        glImages = findViewById(R.id.gl_images_container); // I'll update the XML to add this ID
        MaterialButton btnCamera = findViewById(R.id.btn_camera);
        MaterialButton btnUpload = findViewById(R.id.btn_upload);

        // Mode-based visibility
        String mode = getIntent().getStringExtra("mode");
        if ("view_only".equals(mode)) {
            if (btnCamera != null) btnCamera.setVisibility(View.GONE);
            if (btnUpload != null) btnUpload.setVisibility(View.GONE);
        }

        // Camera & Gallery Launchers
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // For Camera, we usually get a Bitmap or need a File URI.
                // Simplified: Using timestamp as dummy ID for count and reload.
                String dummyUri = "captured_" + System.currentTimeMillis();
                XrayReportManager.addImageReport(this, dummyUri);
                loadAllImages(); // Reload UI
                Toast.makeText(this, "New AI Report added", Toast.LENGTH_SHORT).show();
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                // Persist permission for local URI if possible
                try {
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception e) {}
                
                XrayReportManager.addImageReport(this, uri.toString());
                loadAllImages(); // Reload UI
                Toast.makeText(this, "New AI Report added", Toast.LENGTH_SHORT).show();
            }
        });

        if (btnCamera != null) btnCamera.setOnClickListener(v -> cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)));
        if (btnUpload != null) btnUpload.setOnClickListener(v -> galleryLauncher.launch("image/*"));

        loadAllImages();

        // Navigation
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { startActivity(new Intent(this, TodayAppointmentActivity.class)); finish(); });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    private void loadAllImages() {
        if (glImages == null) return;
        glImages.removeAllViews();

        // 1. Load Standard Images (if not deleted)
        int[] baseRes = {R.drawable.img_23, R.drawable.img_24, R.drawable.img_25, R.drawable.img_26};
        for (int res : baseRes) {
            if (!XrayReportManager.isDeleted(this, res)) {
                addImageToGrid(null, res);
            }
        }

        // 2. Load Dynamically Added Images
        Set<String> addedUris = XrayReportManager.getAddedUris(this);
        for (String uriStr : addedUris) {
            addImageToGrid(uriStr, 0);
        }
    }

    private void addImageToGrid(String uriStr, int resId) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_upload_card, glImages, false);
        ImageView iv = cardView.findViewById(R.id.iv_item_upload);
        ImageButton btnMenu = cardView.findViewById(R.id.btn_item_menu);

        if (uriStr != null) {
            if (uriStr.startsWith("captured_")) {
                iv.setImageResource(R.drawable.img_22); // Dummy for captured
            } else {
                iv.setImageURI(Uri.parse(uriStr));
            }
        } else {
            iv.setImageResource(resId);
        }

        iv.setOnClickListener(v -> {
            Intent intent = new Intent(this, XrayActivity.class);
            if (uriStr != null) intent.putExtra("selected_uri", uriStr);
            else intent.putExtra("selected_image", resId);
            startActivity(intent);
        });

        btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add("Delete");
            popup.setOnMenuItemClickListener(item -> {
                if (uriStr != null) XrayReportManager.deleteUri(this, uriStr);
                else XrayReportManager.deleteImage(this, resId);
                loadAllImages();
                return true;
            });
            popup.show();
        });

        glImages.addView(cardView);
    }
}