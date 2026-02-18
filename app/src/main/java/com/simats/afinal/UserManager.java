package com.simats.afinal;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_PROFILE_URI = "profile_uri_";
    private static final String KEY_REMINDERS = "reminders_enabled_";
    private static final String KEY_DOC_NAME = "doc_name_";
    private static final String KEY_DOC_ROLE = "doc_role_";
    private static final String KEY_CURRENT_USER_ID = "current_user_id";
    
    private static String currentRole = "Doctor"; 
    private static String currentDoctorName = "Dr. Smith"; 
    private static String currentDoctorId = "";

    public static String getCurrentRole() { return currentRole; }
    public static void setCurrentRole(String role) { currentRole = role; }

    public static String getCurrentDoctorName() { return currentDoctorName; }
    public static void setCurrentDoctorName(String name) { currentDoctorName = name; }

    public static String getCurrentDoctorId() { return currentDoctorId; }
    public static void setCurrentDoctorId(String id) { currentDoctorId = id; }

    public static void login(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_CURRENT_USER_ID, id).apply();
        currentDoctorId = id;
        loadProfileData(context);
    }

    public static void saveProfileData(Context context, String name, String id, String role) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_DOC_NAME + id, name);
        editor.putString(KEY_DOC_ROLE + id, role);
        editor.apply();
        
        currentDoctorName = name;
        currentDoctorId = id;
        currentRole = role;
    }

    public static void loadProfileData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        currentDoctorId = prefs.getString(KEY_CURRENT_USER_ID, "");
        if (!currentDoctorId.isEmpty()) {
            currentDoctorName = prefs.getString(KEY_DOC_NAME + currentDoctorId, "Dr. Smith");
            currentRole = prefs.getString(KEY_DOC_ROLE + currentDoctorId, "Doctor");
        }
    }

    public static void setProfileUri(Context context, String uri) {
        if (currentDoctorId.isEmpty()) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_PROFILE_URI + currentDoctorId, uri).apply();
    }

    public static String getProfileUri(Context context) {
        if (currentDoctorId.isEmpty()) return "";
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PROFILE_URI + currentDoctorId, "");
    }

    public static void setRemindersEnabled(Context context, boolean enabled) {
        if (currentDoctorId.isEmpty()) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_REMINDERS + currentDoctorId, enabled).apply();
    }

    public static boolean isRemindersEnabled(Context context) {
        if (currentDoctorId.isEmpty()) return true;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_REMINDERS + currentDoctorId, true);
    }
}