package com.example.foodallergenfinal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class PrefsManager{
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

}