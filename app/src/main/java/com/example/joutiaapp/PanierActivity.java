package com.example.joutiaapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joutiaapp.Adapter.AdapterPanier;
import com.example.joutiaapp.Models.Commande;
import com.example.joutiaapp.Models.PanierUser;
import com.example.joutiaapp.Utils.SharedPreferencesUtils;
import com.example.joutiaapp.Vendeur.MainVendeurActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PanierActivity extends AppCompatActivity {


    ArrayList<String> amount = new ArrayList<>();

    TextView totalP;
    TextView totalTTC;

    ArrayList<PanierUser> retrievedList = new ArrayList<>();

    DatabaseReference Commanderef;

    String name, phone;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_panier);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Commanderef = FirebaseDatabase.getInstance().getReference().child("Commande");
        totalP = findViewById(R.id.totalpanier);
        totalTTC = findViewById(R.id.totalttc);

        retrievedList = SharedPreferencesUtils.getArray(this, "PanierKey");
        if (retrievedList == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.votre_liste_est_vide_merci_de_faire_une_commande);
            builder.setItems(new CharSequence[]{getString(R.string.button_ok)},
                    (dialog, which) -> {
                        if (which == 0) {
                            startActivity(new Intent(this, MainActivity.class));
                        }
                    });
            builder.show();
        } else if (retrievedList.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.votre_liste_est_vide_merci_de_faire_une_commande);
            builder.setItems(new CharSequence[]{getString(R.string.button_ok)},
                    (dialog, which) -> {
                        if (which == 0) {
                            startActivity(new Intent(this, MainActivity.class));
                        }
                    });
            builder.show();
        } else {
            initializeRecyclerview();
        }

        Button buyBtn = findViewById(R.id.btn_buy);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                Boolean isLoged = sharedPreferences.getBoolean("isLoggedIn", false);
                if (isLoged) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PanierActivity.this);
                    builder.setTitle(getString(R.string.vouler_vous_valider_votre_commande));
                    builder.setItems(new CharSequence[]{getString(R.string.oui), getString(R.string.annuler)},
                            (dialog, which) -> {
                                if (which == 0) {
                                    phone = sharedPreferences.getString("PhoneUser", "");
                                    name = sharedPreferences.getString("NomUser", "");
                                    ValideCommande();
                                }
                            });
                    builder.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PanierActivity.this);
                    builder.setTitle(getString(R.string.merci_de_vous_connecter_pour_pouvoir_valider_votre_commande));
                    builder.setItems(new CharSequence[]{getString(R.string.se_connecter), getString(R.string.annuler)},
                            (dialog, which) -> {
                                if (which == 0) {
                                    startActivity(new Intent(PanierActivity.this, ConnexionActivity.class));
                                } else {
                                    startActivity(new Intent(PanierActivity.this, PanierActivity.class));
                                }
                            });
                    builder.show();
                }
            }
        });


        // Ajouter un callback pour gérer le bouton "Retour"
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SharedPreferencesUtils.saveArray(PanierActivity.this, "PanierKey", retrievedList);
            }
        };

        // Ajouter le callback au dispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void initializeRecyclerview() {
        // Find RecyclerView in the layout
        RecyclerView recyclerView = findViewById(R.id.recycler_panier);

        // Set LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set Adapter
        AdapterPanier adapter = new AdapterPanier(retrievedList, this);
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < retrievedList.size(); i++) {
            amount.add(retrievedList.get(i).Prix.toString());
        }
        showamout(amount);

        adapter.setOnDataChangeListener(new AdapterPanier.OnDataChangeListener() {
            @Override
            public void showDetail(Object object, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PanierActivity.this);
                builder.setTitle(R.string.etes_vous_s_r_de_vouloir_supprimer_cet_article);
                builder.setItems(new CharSequence[]{getString(R.string.oui), getString(R.string.non)}, (dialog, which) -> {
                    if (which == 0 && position >= 0 && position < retrievedList.size()) {
                        retrievedList.remove(position);
                        amount.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, retrievedList.size());
                        SharedPreferencesUtils.saveArray(PanierActivity.this, "PanierKey", retrievedList);
                        showamout(amount);
                    } else {
                        Log.e("PanierActivity", "Invalid removal attempt for position: " + position);
                    }
                });
                builder.show();


            }

        });

    }

    private void ValideCommande() {
        Date now = new Date();
        String today = simpleDateFormat.format(now.getTime());
        Commande commande = new Commande(name, phone, retrievedList, "0", totalTTC.getText().toString());
        Commanderef.push().setValue(commande);
        AlertDialog.Builder builder = new AlertDialog.Builder(PanierActivity.this);
        builder.setTitle(R.string.votre_commande_a_t_bien_ajout_l_attente_d_un_retour_veuillez_profiter_de_notre_application);
        builder.setItems(new CharSequence[]{getString(R.string.button_ok)},
                (dialog, which) -> {
                    if (which == 0) {
                        SharedPreferencesUtils.saveArray(this, "PanierKey", new ArrayList<>());
                        startActivity(new Intent(PanierActivity.this, MainActivity.class));
                    }
                });
        builder.show();

    }

    private void showamout(ArrayList<String> amount) {
        double total = 0; // Use double to accommodate decimal values
        for (int i = 0; i < amount.size(); i++) {
            total += Double.parseDouble(amount.get(i)); // Parse as double
        }
        totalP.setText(total + " MAD"); // Display total with decimals
        totalTTC.setText(total + 30.00 + " MAD"); // Display total with decimals
    }

    public void goBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


}