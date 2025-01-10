package com.example.joutiaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfilActivity extends AppCompatActivity {

    EditText nom, mail, adresse, ville, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nom = findViewById(R.id.edtNom);
        mail = findViewById(R.id.edtmail);
        adresse = findViewById(R.id.edtadresse);
        ville = findViewById(R.id.edtville);
        phone = findViewById(R.id.edtphone);

        getallinfo();

    }

    private void getallinfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("mailUser", "");
        String name = sharedPreferences.getString("NomUser", "");
        String villes = sharedPreferences.getString("VilleUser", "");
        String adresses = sharedPreferences.getString("AdresseUser", "");
        String phones = sharedPreferences.getString("PhoneUser", "");

        nom.setText(name);
        mail.setText(username);
        adresse.setText(adresses);
        ville.setText(villes);
        phone.setText(phones);
    }

    public void modifyInfo(View view) {
        String username = mail.getText().toString();
        String name = nom.getText().toString();
        String villes = ville.getText().toString();
        String adresses = adresse.getText().toString();
        String phones = phone.getText().toString();
        
        if(username.isEmpty() || name.isEmpty() || villes.isEmpty() || adresses.isEmpty() || phones.isEmpty()){
            Toast.makeText(this, R.string.veuillez_remplir_tous_les_champs, Toast.LENGTH_SHORT).show();
        }else {
            setMailUserLoggedIn(getApplicationContext(),username);
            setNomUserLoggedIn(getApplicationContext(),name);
            setvilleUserLoggedIn(getApplicationContext(),villes);
            setadressUserLoggedIn(getApplicationContext(),adresses);
            setphoneUserLoggedIn(getApplicationContext(),phones);

            Toast.makeText(this, R.string.vos_informations_ont_t_modifi_es_avec_succ_s, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));


        }
        
    }

    public void setMailUserLoggedIn(Context context,String Mail) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mailUser",Mail);
        editor.apply();
    }

    public void setNomUserLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NomUser",Name);
        editor.apply();
    }

    public void setadressUserLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AdresseUser",Name);
        editor.apply();
    }


    public void setvilleUserLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("VilleUser",Name);
        editor.apply();
    }

    public void setphoneUserLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PhoneUser",Name);
        editor.apply();
    }

}