package com.simats.afinal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class UploadImagesActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);

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
                XrayReportManager.incrementCount(this);
                Toast.makeText(this, "Image captured", Toast.LENGTH_SHORT).show();
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                XrayReportManager.incrementCount(this);
                Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show();
            }
        });

        if (btnCamera != null) btnCamera.setOnClickListener(v -> cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)));
        if (btnUpload != null) btnUpload.setOnClickListener(v -> galleryLauncher.launch("image/*"));

        // Sync visibility based on deleted images
        syncImageVisibility();

        // Setup menus for each card with their respective resource IDs
        setupXrayMenu(R.id.btn_menu_upload_1, R.id.card_upload_1, R.drawable.img_23);
        setupXrayMenu(R.id.btn_menu_upload_2, R.id.card_upload_2, R.drawable.img_24);
        setupXrayMenu(R.id.btn_menu_upload_3, R.id.card_upload_3, R.drawable.img_25);
        setupXrayMenu(R.id.btn_menu_upload_4, R.id.card_upload_4, R.drawable.img_26);

        // Click Logic
        setupImageClick(R.id.img_click_1, R.drawable.img_23);
        setupImageClick(R.id.img_click_2, R.drawable.img_24);
        setupImageClick(R.id.img_click_3, R.drawable.img_25);
        setupImageClick(R.id.img_click_4, R.drawable.img_26);

        // Navigation
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_schedule_custom).setOnClickListener(v -> { startActivity(new Intent(this, TodayAppointmentActivity.class)); finish(); });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    private void syncImageVisibility() {
        if (XrayReportManager.isDeleted(this, R.drawable.img_23)) findViewById(R.id.card_upload_1).setVisibility(View.GONE);
        if (XrayReportManager.isDeleted(this, R.drawable.img_24)) findViewById(R.id.card_upload_2).setVisibility(View.GONE);
        if (XrayReportManager.isDeleted(this, R.drawable.img_25)) findViewById(R.id.card_upload_3).setVisibility(View.GONE);
        if (XrayReportManager.isDeleted(this, R.drawable.img_26)) findViewById(R.id.card_upload_4).setVisibility(View.GONE);
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
                        Toast.makeText(this, "Image deleted permanently", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                });
                popup.show();
            });
        }
    }

    private void setupImageClick(int viewId, int resId) {
        ImageView iv = findViewById(viewId);
        if (iv != null) {
            iv.setOnClickListener(v -> {
                Intent intent = new Intent(this, XrayActivity.class);
                intent.putExtra("selected_image", resId);
                startActivity(intent);
            });
        }
    }
}
