package com.example.joutiaapp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joutiaapp.MainActivity;
import com.example.joutiaapp.Models.Vendeur;
import com.example.joutiaapp.R;
import com.example.joutiaapp.Vendeur.MainVendeurActivity;
import com.example.joutiaapp.Vendeur.ProfilVendeurActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SettingsVendeurFragment extends Fragment {

    TextView mailV, nomM;

    List<Vendeur> allVendeurarray = new ArrayList<>();

    DatabaseReference allVendeur;

    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_settings_vendeur, container, false);
        findView(view);
        return view;
    }
    private void findView(View view) {
        allVendeur = FirebaseDatabase.getInstance().getReference().child("vendeur");
        TextView go_sign_in=view.findViewById(R.id.go_sign_in);
        mailV = view.findViewById(R.id.MailVendeur);
        nomM = view.findViewById(R.id.nomMg);
        imageView = view.findViewById(R.id.img_pro);
        verifyLogin();
        go_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.tes_vous_sur_de_vouloir_retourner_la_partie_acheteur);
                builder.setItems(new CharSequence[]{getString(R.string.oui), getString(R.string.non)},
                        (dialog, which) -> {
                            if (which == 0) {
                                startActivity(new Intent(getContext(), MainActivity.class));
                                setUserLoggedOut(getContext());
                            } else {
                                startActivity(new Intent(getContext(), MainVendeurActivity.class));
                            }
                        });
                builder.show();
            }
        });

        LinearLayout Langue = view.findViewById(R.id.langue);
        Langue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.choisir_une_langue);
                builder.setItems(new CharSequence[]{getString(R.string.fran_ais), getString(R.string.arabe)},
                        (dialog, which) -> {
                            if (which == 0) {
                                startActivity(new Intent(getContext(), MainVendeurActivity.class));
                                setLocale("fr");
                            } else {
                                startActivity(new Intent(getContext(), MainVendeurActivity.class));
                                setLocale("ar");
                            }
                        });
                builder.show();
            }
        });

        LinearLayout Profil = view.findViewById(R.id.profil);
        Profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfilVendeurActivity.class));
            }
        });

        LinearLayout Linear_Profil = view.findViewById(R.id.linear_prof);
        Linear_Profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfilVendeurActivity.class));
            }
        });

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


        startActivity(new Intent(getContext(), MainVendeurActivity.class));
    }


    private void verifyLogin() {
        if (isUserLoggedIn(getContext())) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("mailUser", "default_username");
            String name = sharedPreferences.getString("NomUser", "default_username");
            mailV.setText(username);
            nomM.setText(name);
            getporfilepic(username);
        } else {
            mailV.setText(getString(R.string.merci_de_vous_connecter));
        }
    }

    private void getporfilepic(String username) {
        allVendeurarray.clear();
        allVendeur.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allVendeurarray.add(postSnapshot.getValue(Vendeur.class));
                }
                for (int i = 0; i < allVendeurarray.size(); i++) {
                    if(allVendeurarray.get(i).mail.equals(username)){
                        Bitmap bitmap = base64ToBitmap(allVendeurarray.get(i).ArrayImage.get(0));
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            imageView.setImageResource(R.drawable.default_img);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Bitmap base64ToBitmap(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isUserLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    public void setUserLoggedOut(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.putString("mailUser", "");
        editor.putString("NomUser", "");
        editor.putString("type", "");
        editor.apply();
    }
}