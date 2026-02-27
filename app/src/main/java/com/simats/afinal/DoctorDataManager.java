package com.simats.afinal;

import java.util.ArrayList;
import java.util.List;

public class DoctorDataManager {
    private static List<DoctorInfo> allDoctors = new ArrayList<>();

    static {
        // Initial sample data
        allDoctors.add(new DoctorInfo("Dr. Sarah Mitchell", "DOC-8821", "sarah.m@dentalai.com", "1234567890", "Endodontics", "Dental Doctor", "password", "Active"));
        allDoctors.add(new DoctorInfo("Dr. James Rodriguez", "DOC-9012", "j.rod@dentalai.com", "9876543210", "Orthodontics", "Consultant", "password", "Active"));
        allDoctors.add(new DoctorInfo("Dr. Anna Kim", "DOC-4431", "anna.k@dentalai.com", "1122334455", "Pediatrics", "Dental Doctor", "password", "Inactive"));
        allDoctors.add(new DoctorInfo("Dr. David Lee", "DOC-1256", "d.lee@dentalai.com", "5566778899", "Surgery", "Intern", "password", "Active"));
    }

    public static List<DoctorInfo> getAllDoctors() {
        return allDoctors;
    }

    public static void addDoctor(DoctorInfo doctor) {
        allDoctors.add(0, doctor);
    }

    public static boolean isIdUnique(String id) {
        for (DoctorInfo d : allDoctors) {
            if (d.getId().equalsIgnoreCase(id)) return false;
        }
        return true;
    }
}
