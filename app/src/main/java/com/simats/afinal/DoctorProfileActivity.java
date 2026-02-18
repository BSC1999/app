package com.simats.afinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.materialswitch.MaterialSwitch;

public class DoctorProfileActivity extends AppCompatActivity {

    private ShapeableImageView ivProfile;
    private EditText etName, etId, etRole;
    private MaterialSwitch switchReminders;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        ivProfile = findViewById(R.id.iv_doctor_profile);
        etName = findViewById(R.id.et_doc_name);
        etId = findViewById(R.id.et_doc_id);
        etRole = findViewById(R.id.et_doc_role);
        switchReminders = findViewById(R.id.switch_reminders);

        UserManager.loadProfileData(this);
        etName.setText(UserManager.getCurrentDoctorName());
        etId.setText(UserManager.getCurrentDoctorId());
        etRole.setText(UserManager.getCurrentRole());
        switchReminders.setChecked(UserManager.isRemindersEnabled(this));

        loadProfileImage();

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) Toast.makeText(this, "Photo Captured", Toast.LENGTH_SHORT).show();
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                // Persistent Permission logic
                try {
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ivProfile.setImageURI(uri);
                UserManager.setProfileUri(this, uri.toString());
            }
        });

        findViewById(R.id.fl_doctor_profile_pic).setOnClickListener(v -> {
            String[] options = {"Take Photo", "Gallery"};
            new android.app.AlertDialog.Builder(this).setItems(options, (d, i) -> {
                if (i == 0) cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                else galleryLauncher.launch("image/*");
            }).show();
        });

        findViewById(R.id.btn_save_profile).setOnClickListener(v -> {
            UserManager.saveProfileData(this, etName.getText().toString(), etId.getText().toString(), etRole.getText().toString());
            UserManager.setRemindersEnabled(this, switchReminders.isChecked());
            finish();
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void loadProfileImage() {
        String savedUri = UserManager.getProfileUri(this);
        if (!savedUri.isEmpty()) {
            try {
                ivProfile.setImageURI(Uri.parse(savedUri));
            } catch (Exception e) {
                ivProfile.setImageResource(R.drawable.img_21);
            }
        }
    }
}