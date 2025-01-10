package com.example.joutiaapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class LangueActivity extends AppCompatActivity {

    private RadioGroup languageRadioGroup;
    private String language="fr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_langue);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        languageRadioGroup = findViewById(R.id.language_radio_group);
        languageRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton_ar) {
                    language="ar";
                } else if (checkedId == R.id.radioButton_fr) {
                    language="fr";
                }
            }
        });
    }

    public void onChangeLanguageClick(View view) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        // Définir la direction RTL pour les langues RTL comme l'arabe
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (language.equals("ar")) {
                config.setLayoutDirection(locale);
            } else {
                config.setLayoutDirection(Locale.FRANCE); // LTR pour les autres langues
            }
        }

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());


        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }
}