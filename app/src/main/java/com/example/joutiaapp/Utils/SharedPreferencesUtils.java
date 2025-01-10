package com.example.joutiaapp.Utils;

import android.content.SharedPreferences;
import android.content.Context;

import com.example.joutiaapp.Models.PanierUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferencesUtils {

    private static final String PREF_NAME = "MySharedPrefs";

    // Save an ArrayList as JSON
    public static void saveArray(Context context, String key, ArrayList<PanierUser> array) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(array);
        editor.putString(key, json);
        editor.apply();
    }

    // Retrieve an ArrayList from JSON
    public static ArrayList<PanierUser> getArray(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<PanierUser>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void clearAllSharedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all data
        editor.apply(); // Apply changes
    }
}
