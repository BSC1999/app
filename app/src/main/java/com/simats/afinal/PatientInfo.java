package com.simats.afinal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PatientInfo implements Serializable {
    private String name;
    private String id;
    private int age;
    private String dob;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String complaint;
    private String medicalHistory;
    private String treatmentPaymentInfo;
    private String lastVisit;
    private boolean isFemale;
    private long addedTimestamp;
    private String assignedDoctor;
    private String nextScheduleDate;
    private String nextScheduleTime; // Added Time Field
    private List<String> uploadedXrayUris;

    public PatientInfo(String name, String id, int age, String dob, String gender, String phone, String email, String address, String complaint, String medicalHistory, String treatmentPaymentInfo, String lastVisit, boolean isFemale, long addedTimestamp, String assignedDoctor) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.complaint = complaint;
        this.medicalHistory = medicalHistory;
        this.treatmentPaymentInfo = treatmentPaymentInfo;
        this.lastVisit = lastVisit;
        this.isFemale = isFemale;
        this.addedTimestamp = addedTimestamp;
        this.assignedDoctor = assignedDoctor;
        this.nextScheduleDate = "";
        this.nextScheduleTime = "";
        this.uploadedXrayUris = new ArrayList<>();
    }

    public String getName() { return name; }
    public String getId() { return id; }
    public int getAge() { return age; }
    public String getDob() { return dob; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getComplaint() { return complaint; }
    public String getMedicalHistory() { return medicalHistory; }
    public String getTreatmentPaymentInfo() { return treatmentPaymentInfo; }
    public void setTreatmentPaymentInfo(String info) { this.treatmentPaymentInfo = info; }
    
    public String getLastVisit() {
        long currentTime = System.currentTimeMillis();
        long diffInMillis = currentTime - addedTimestamp;
        long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
        if (days < 1) return "Today";
        else if (days < 30) return days + (days == 1 ? " day ago" : " days ago");
        else return (days / 30) + " months ago";
    }

    public boolean isFemale() { return isFemale; }
    public long getAddedTimestamp() { return addedTimestamp; }
    public String getAssignedDoctor() { return assignedDoctor; }
    
    public String getNextScheduleDate() { return nextScheduleDate; }
    public void setNextScheduleDate(String date) { this.nextScheduleDate = date; }

    public String getNextScheduleTime() { return nextScheduleTime; }
    public void setNextScheduleTime(String time) { this.nextScheduleTime = time; }

    public List<String> getUploadedXrayUris() { 
        if (uploadedXrayUris == null) uploadedXrayUris = new ArrayList<>();
        return uploadedXrayUris; 
    }
    public void addXrayUri(String uri) {
        if (uploadedXrayUris == null) uploadedXrayUris = new ArrayList<>();
        uploadedXrayUris.add(uri);
    }
}