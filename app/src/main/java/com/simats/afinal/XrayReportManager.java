package com.simats.afinal;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class XrayReportManager {
    private static final String PREF_NAME = "XrayPrefs";
    private static final String DELETED_IMAGES_KEY = "deleted_images_list";
    private static final String ADDED_URIS_KEY = "added_uris_set";

    public static void init(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static int getReportCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> deleted = prefs.getStringSet(DELETED_IMAGES_KEY, new HashSet<>());
        Set<String> added = prefs.getStringSet(ADDED_URIS_KEY, new HashSet<>());
        
        String[] baseNames = {"img_23", "img_24", "img_25", "img_26"};
        int visibleBaseCount = 0;
        for (String name : baseNames) {
            if (!deleted.contains(name)) {
                visibleBaseCount++;
            }
        }
        return Math.max(0, visibleBaseCount + added.size());
    }

    public static void incrementCount(Context context) {
        // Fallback for generic increment if no URI is provided
        addImageUri(context, "captured_" + System.currentTimeMillis());
    }

    public static void addImageUri(Context context, String uri) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> added = new HashSet<>(prefs.getStringSet(ADDED_URIS_KEY, new HashSet<>()));
        if (added.add(uri)) {
            prefs.edit().putStringSet(ADDED_URIS_KEY, added).commit();
        }
    }

    public static void deleteImage(Context context, int resId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> deleted = new HashSet<>(prefs.getStringSet(DELETED_IMAGES_KEY, new HashSet<>()));
        String resName = context.getResources().getResourceEntryName(resId);
        if (deleted.add(resName)) {
            prefs.edit().putStringSet(DELETED_IMAGES_KEY, deleted).commit();
        }
    }

    public static void deleteUri(Context context, String uri) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> added = new HashSet<>(prefs.getStringSet(ADDED_URIS_KEY, new HashSet<>()));
        if (added.remove(uri)) {
            prefs.edit().putStringSet(ADDED_URIS_KEY, added).commit();
        }
    }

    public static boolean isDeleted(Context context, int resId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> deleted = prefs.getStringSet(DELETED_IMAGES_KEY, new HashSet<>());
        try {
            String resName = context.getResources().getResourceEntryName(resId);
            return deleted.contains(resName);
        } catch (Exception e) {
            return false;
        }
    }

    public static Set<String> getAddedUris(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(ADDED_URIS_KEY, new HashSet<>());
    }
}