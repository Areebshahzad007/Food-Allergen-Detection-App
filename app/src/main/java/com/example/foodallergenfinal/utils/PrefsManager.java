package com.example.foodallergenfinal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class PrefsManager {
    private static final String PREF_NAME = "AllergyPrefs";
    private static final String KEY_SELECTED_ALLERGIES = "selected_allergies";

    // Retrieve saved allergies
    public static Set<String> getSavedAllergies(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return new HashSet<>(sharedPreferences.getStringSet(KEY_SELECTED_ALLERGIES, new HashSet<>()));
    }

    // Save or remove an allergic item
    public static void saveAllergicItem(Context context, String itemName, boolean isSelected) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Create a new HashSet before modifying
        Set<String> savedItems = new HashSet<>(getSavedAllergies(context));

        if (isSelected) {
            savedItems.add(itemName);
        } else {
            savedItems.remove(itemName);
        }

        editor.putStringSet(KEY_SELECTED_ALLERGIES, savedItems);
        editor.apply();
    }

    // Save a single String value
    public static void setString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Retrieve a single String value
    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }


    public static void setInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0); // Default value is 0 if key not found
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false); // Default value is false if key not found
    }


    private static final String KEY_PROFILE_IMAGE_PATH = "profile_image_path";

    public static void saveProfileImagePath(Context context, String imagePath) {
        setString(context, KEY_PROFILE_IMAGE_PATH, imagePath);
    }

    // Retrieve Image Path
    public static String getProfileImagePath(Context context) {
        return getString(context, KEY_PROFILE_IMAGE_PATH);
    }

    private static final String KEY_SELECTED_LANGUAGE = "selected_language";

    // Save selected language
    public static void saveSelectedLanguage(Context context, String languageCode, String countryCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SELECTED_LANGUAGE, languageCode + "-" + countryCode);
        editor.apply();
    }

    // Retrieve saved language
    public static String getSelectedLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SELECTED_LANGUAGE, "en-US"); // Default language (English-US)
    }


}