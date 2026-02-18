package com.simats.afinal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientDataManager {
    private static List<PatientInfo> allPatients = new ArrayList<>();

    static {
        resetToDefaultData();
    }

    public static void resetToDefaultData() {
        allPatients.clear();
        
        // Profiles assigned to "General" so they are visible to everyone regardless of doctor name changes
        allPatients.add(new PatientInfo("James Wilson", "556677", 45, "10-10-1978", "Male", "+91 9988776655", "james@example.com", "789 Blue St, NY", "Toothache", "No history", "Paid", "Just now", false, System.currentTimeMillis(), "General"));
        allPatients.add(new PatientInfo("Sarah Jenkins", "889900", 32, "15-05-1992", "Female", "+91 8877665544", "sarah@example.com", "456 Rose Ave, CA", "Cleaning", "Allergy to Penicillin", "Pending", "Just now", true, System.currentTimeMillis(), "General"));

        allPatients.add(new PatientInfo("Sophia Carter", "123456", 35, "12-05-1989", "Female", "+91 9876543210", "sophia@example.com", "123 Street, NY", "Check-up", "None", "None", "2 months ago", true, 1704067200000L, "General"));
        allPatients.add(new PatientInfo("Ethan Miller", "789012", 42, "20-11-1982", "Male", "+91 9876543211", "ethan@example.com", "456 Avenue, CA", "Cleaning", "None", "None", "1 month ago", false, 1706745600000L, "General"));
        
        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        allPatients.get(0).setNextScheduleDate(today);
        allPatients.get(0).setNextScheduleTime("10:00 AM - 11:00 AM");
    }

    public static List<PatientInfo> getAllPatients() {
        if (allPatients.isEmpty()) resetToDefaultData();
        return allPatients;
    }

    public static List<PatientInfo> getPatientsForDoctor(String doctorName) {
        List<PatientInfo> doctorPatients = new ArrayList<>();
        for (PatientInfo p : getAllPatients()) {
            // Show patients assigned to this doctor OR "General" patients
            if (p.getAssignedDoctor().equalsIgnoreCase(doctorName) || p.getAssignedDoctor().equalsIgnoreCase("General")) {
                doctorPatients.add(p);
            }
        }
        return doctorPatients;
    }

    public static List<PatientInfo> getScheduledPatientsForToday() {
        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        List<PatientInfo> scheduledToday = new ArrayList<>();
        for (PatientInfo p : getAllPatients()) {
            if (today.equals(p.getNextScheduleDate())) {
                scheduledToday.add(p);
            }
        }
        return scheduledToday;
    }

    public static List<PatientInfo> getScheduledPatientsForToday(String doctorName) {
        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        List<PatientInfo> scheduledToday = new ArrayList<>();
        for (PatientInfo p : getAllPatients()) {
            if (today.equals(p.getNextScheduleDate()) && 
               (p.getAssignedDoctor().equalsIgnoreCase(doctorName) || p.getAssignedDoctor().equalsIgnoreCase("General"))) {
                scheduledToday.add(p);
            }
        }
        return scheduledToday;
    }

    public static void addPatient(PatientInfo patient) {
        allPatients.add(0, patient);
    }

    public static void deletePatient(PatientInfo patient) {
        allPatients.remove(patient);
    }

    public static void updatePatient(PatientInfo updatedPatient) {
        for (int i = 0; i < allPatients.size(); i++) {
            if (allPatients.get(i).getId().equals(updatedPatient.getId())) {
                allPatients.set(i, updatedPatient);
                return;
            }
        }
    }

    public static boolean isIdUnique(String id) {
        for (PatientInfo p : getAllPatients()) {
            if (p.getId().equals(id)) return false;
        }
        return true;
    }

    public static boolean isSlotBooked(String date, String time) {
        for (PatientInfo p : getAllPatients()) {
            if (date.equals(p.getNextScheduleDate()) && time.equals(p.getNextScheduleTime())) {
                return true;
            }
        }
        return false;
    }
}