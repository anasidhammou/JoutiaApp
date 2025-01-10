package com.example.joutiaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joutiaapp.Vendeur.MainVendeurActivity;

import java.util.Locale;

public class Choix_langue_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_choix_langue);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }


    public void goHomeFr(View view) {
        setLocale("fr");
    }

    public void goHomeAr(View view) {
        setLocale("ar");
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        // Définir la direction RTL pour les langues RTL comme l'arabe
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (languageCode.equals("ar")) {
                config.setLayoutDirection(locale);
            } else {
                config.setLayoutDirection(Locale.FRANCE); // LTR pour les autres langues
            }
        }

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());


        if(isUserLoggedIn(getApplicationContext())){
            if(getUsertype(getApplicationContext()).equals("Client") || getUsertype(getApplicationContext()).equals("")){
                startActivity(new Intent(Choix_langue_Activity.this, MainActivity.class));
                finish();
            }else {
                startActivity(new Intent(Choix_langue_Activity.this, MainVendeurActivity.class));
                finish();
            }
        }else {
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            finish();
        }




    }

    private boolean isUserLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private String getUsertype(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("type", "");
    }

}