package com.simats.afinal;

public class Patient {
    private String name;
    private String startTime; // "HH:mm" for sorting
    private String endTime;   // "HH:mm"
    private String treatment;
    private boolean isFemale;
    private boolean isCompleted;
    private String doctorName; // Added doctor name field
    private PatientInfo patientInfo; 

    public Patient(String name, String startTime, String endTime, String treatment, boolean isFemale, String doctorName) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.treatment = treatment;
        this.isFemale = isFemale;
        this.doctorName = doctorName;
        this.isCompleted = false;
        
        // Linking to a PatientInfo object - Updated to match 15 required arguments
        this.patientInfo = new PatientInfo(
            name, 
            "ID" + (int)(Math.random()*1000), 
            30, // age (Default for dummy data)
            "01-01-1994", // DOB
            isFemale ? "Female" : "Male", 
            "+91 0000000000", 
            "patient@example.com", 
            "Address", 
            "Initial Complaint", 
            "No medical history recorded", 
            "No treatment/payment info recorded",
            "Today", 
            isFemale, 
            System.currentTimeMillis(),
            doctorName
        );
    }

    public String getName() { return name; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getTreatment() { return treatment; }
    public boolean isFemale() { return isFemale; }
    
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public PatientInfo getPatientInfo() { return patientInfo; }
    public void setPatientInfo(PatientInfo patientInfo) { this.patientInfo = patientInfo; }

    public String getFullDisplayTime() {
        return formatTime(startTime) + " - " + formatTime(endTime);
    }

    private String formatTime(String time) {
        try {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            int min = Integer.parseInt(parts[1]);
            String suffix = (hour >= 12) ? "PM" : "AM";
            int displayHour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
            return String.format("%d:%02d %s", displayHour, min, suffix);
        } catch (Exception e) {
            return time;
        }
    }
}