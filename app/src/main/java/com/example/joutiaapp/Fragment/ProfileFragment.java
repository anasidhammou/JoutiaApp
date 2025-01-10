package com.example.joutiaapp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joutiaapp.CommandeActivity;
import com.example.joutiaapp.ConnexionActivity;
import com.example.joutiaapp.InscriptionClientActivity;
import com.example.joutiaapp.InscriptionVendeurActivity;
import com.example.joutiaapp.LangueActivity;
import com.example.joutiaapp.MainActivity;
import com.example.joutiaapp.PanierActivity;
import com.example.joutiaapp.ProfilActivity;
import com.example.joutiaapp.R;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProfileFragment extends Fragment {
    TextView mailUser;
    TextView Se_Connecter;
    TextView Se_Deconnecter;
    TextView nomComplet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        findView(v);
        return v;
    }

    private void findView(View v) {
        Se_Connecter = v.findViewById(R.id.go_sign_in);
        mailUser = v.findViewById(R.id.mailUser);
        Se_Deconnecter = v.findViewById(R.id.go_sign_out);
        nomComplet = v.findViewById(R.id.NomCUser);
        LinearLayout line_vendeur = v.findViewById(R.id.vendeur);
        LinearLayout line_profil = v.findViewById(R.id.linear_prof);
        LinearLayout profil = v.findViewById(R.id.profil);
        LinearLayout Langue = v.findViewById(R.id.langue);
        LinearLayout commande = v.findViewById(R.id.commadeC);
        LinearLayout panier = v.findViewById(R.id.panierC);
        verifyLogin();

        Se_Connecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ConnexionActivity.class);
                startActivity(i);
            }
        });

        line_vendeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), InscriptionVendeurActivity.class);
                startActivity(i);
            }
        });

        line_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("mailUser", "");
                String name = sharedPreferences.getString("NomUser", "");
                if(name.equals("") && username.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.merci_de_vous_connecter_pour_pouvoir_acc_der_au_proofil_voulez_vous_vous_connectez);
                    builder.setItems(new CharSequence[]{getString(R.string.oui), getString(R.string.non)},
                            (dialog, which) -> {
                                if (which == 0) {
                                    startActivity(new Intent(getContext(), ConnexionActivity.class));
                                } else {
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                }
                            });
                    builder.show();
                }else{
                    Intent i = new Intent(getContext(), ProfilActivity.class);
                    startActivity(i);
                }

            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("mailUser", "");
                String name = sharedPreferences.getString("NomUser", "");
                if(name.equals("") && username.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.merci_de_vous_connecter_pour_pouvoir_acc_der_au_proofil_voulez_vous_vous_connectez);
                    builder.setItems(new CharSequence[]{getString(R.string.oui), getString(R.string.non)},
                            (dialog, which) -> {
                                if (which == 0) {
                                    startActivity(new Intent(getContext(), ConnexionActivity.class));
                                } else {
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                }
                            });
                    builder.show();
                }else{
                    Intent i = new Intent(getContext(), ProfilActivity.class);
                    startActivity(i);
                }
            }
        });

        Langue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), LangueActivity.class);
                startActivity(i);
            }
        });

        commande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("mailUser", "");
                String name = sharedPreferences.getString("NomUser", "");
                if(name.equals("") && username.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.merci_de_vouloir_acc_der_pour_pouvoir_acc_der_vos_commands);
                    builder.setItems(new CharSequence[]{getString(R.string.oui), getString(R.string.non)},
                            (dialog, which) -> {
                                if (which == 0) {
                                    startActivity(new Intent(getContext(), ConnexionActivity.class));
                                } else {
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                }
                            });
                    builder.show();
                }else{
                    Intent i = new Intent(getContext(), CommandeActivity.class);
                    startActivity(i);
                }


            }
        });

        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PanierActivity.class);
                startActivity(i);
            }
        });

        Se_Deconnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.voulez_vous_vous_d_connecter));
                builder.setItems(new CharSequence[]{getString(R.string.oui), getString(R.string.non)},
                        (dialog, which) -> {
                            if (which == 0) {
                                startActivity(new Intent(getContext(), MainActivity.class));
                                setUserLoggedOut(getContext());
                            } else {
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        });
                builder.show();
            }
        });
    }

    private void verifyLogin() {
        if (isUserLoggedIn(getContext())) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("mailUser", "");
            String name = sharedPreferences.getString("NomUser", "");
            mailUser.setText(username);
            nomComplet.setText(name);
            Se_Connecter.setVisibility(View.GONE);
            Se_Deconnecter.setVisibility(View.VISIBLE);
        } else {
            mailUser.setText(getString(R.string.merci_de_vous_connecter));
            Se_Connecter.setVisibility(View.VISIBLE);
            Se_Deconnecter.setVisibility(View.GONE);
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