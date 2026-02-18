package com.simats.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NextScheduleActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private String selectedDate;
    private PatientInfo currentPatient;
    private LinearLayout llSlotsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_schedule);

        currentPatient = (PatientInfo) getIntent().getSerializableExtra("patient_data");
        
        calendarView = findViewById(R.id.calendar_view);
        llSlotsContainer = findViewById(R.id.ll_slots_container);

        // Calendar setup
        Calendar calendar = Calendar.getInstance();
        calendarView.setMinDate(calendar.getTimeInMillis());
        
        selectedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, month + 1, year);
            refreshSlots();
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        refreshSlots();

        // Navigation
        findViewById(R.id.nav_home_custom).setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        findViewById(R.id.nav_patients_custom).setOnClickListener(v -> { startActivity(new Intent(this, PatientsActivity.class)); finish(); });
        findViewById(R.id.nav_ai_custom).setOnClickListener(v -> { startActivity(new Intent(this, UploadImagesActivity.class)); finish(); });
        findViewById(R.id.nav_more_custom).setOnClickListener(v -> { startActivity(new Intent(this, MoreActivity.class)); finish(); });
    }

    private void refreshSlots() {
        llSlotsContainer.removeAllViews();
        
        // Mawa nuvvu adigina 6 slots ivey
        String[] slots = {
            "09:00 AM - 10:00 AM", 
            "10:00 AM - 11:00 AM", 
            "11:00 AM - 12:00 PM",
            "01:30 PM - 02:30 PM", 
            "02:30 PM - 03:30 PM", 
            "03:30 PM - 04:30 PM"
        };

        boolean slotsAvailable = false;
        for (String slot : slots) {
            // Check if slot is already booked for this date
            if (PatientDataManager.isSlotBooked(selectedDate, slot)) continue;

            slotsAvailable = true;
            View slotView = getLayoutInflater().inflate(R.layout.item_slot, llSlotsContainer, false);
            TextView tvTime = slotView.findViewById(R.id.tv_slot_time);
            MaterialButton btnBook = slotView.findViewById(R.id.btn_book_now);

            tvTime.setText(slot);
            btnBook.setText("Book Slot"); // Nuvvu adigina button text
            
            btnBook.setOnClickListener(v -> {
                if (currentPatient != null) {
                    currentPatient.setNextScheduleDate(selectedDate);
                    currentPatient.setNextScheduleTime(slot);
                    PatientDataManager.updatePatient(currentPatient);
                    Toast.makeText(this, "Slot Booked for " + currentPatient.getName() + " on " + selectedDate + " at " + slot, Toast.LENGTH_LONG).show();
                    finish(); // Back to profile
                } else {
                    Toast.makeText(this, "Please select a patient from profiles first", Toast.LENGTH_SHORT).show();
                }
            });
            llSlotsContainer.addView(slotView);
        }

        if (!slotsAvailable) {
            TextView tvNoSlots = new TextView(this);
            tvNoSlots.setText("No slots available for this date");
            tvNoSlots.setPadding(0, 20, 0, 0);
            tvNoSlots.setGravity(android.view.Gravity.CENTER);
            llSlotsContainer.addView(tvNoSlots);
        }
    }
}