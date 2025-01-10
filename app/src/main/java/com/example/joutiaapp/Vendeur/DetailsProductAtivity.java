package com.example.joutiaapp.Vendeur;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.joutiaapp.Adapter.ViewPagerAdapter;
import com.example.joutiaapp.ConnexionActivity;
import com.example.joutiaapp.InscriptionClientActivity;
import com.example.joutiaapp.InscriptionVendeurActivity;
import com.example.joutiaapp.Models.product;
import com.example.joutiaapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsProductAtivity extends AppCompatActivity {

    ImageView imageProd;
    TextView NomPord;

    TextView prixProd;

    TextView description, etat;

    TextView categoris;

    Object amounts;

    DatabaseReference allproduct;
    List<product> allproductarray = new ArrayList<>();
    List<product> allproductarrayfiltred = new ArrayList<>();

    String NomProduct;

    ViewPager2 viewPager2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_product_ativity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageProd = findViewById(R.id.Image_prod);
        NomPord = findViewById(R.id.nom_Prod);
        prixProd = findViewById(R.id.prix_prod);
        categoris = findViewById(R.id.categories);
        description = findViewById(R.id.description);
        etat = findViewById(R.id.etatprod);

        viewPager2= findViewById(R.id.viewPager);

        allproduct = FirebaseDatabase.getInstance().getReference().child("product");


        Intent intent = getIntent();
        String value = intent.getStringExtra("Nom");
        if (value != null) {
            getallprod();
        }


    }

    private void getallprod() {
        allproductarray.clear();
        allproductarrayfiltred.clear();
        allproduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allproductarray.add(postSnapshot.getValue(product.class));
                }
                for (int i = 0; i < allproductarray.size(); i++) {
                    if(allproductarray.get(i).Nom.equals(getIntent().getStringExtra("Nom"))){
                        allproductarrayfiltred.add(allproductarray.get(i));
                    }
                }
                ShowInfo(allproductarrayfiltred);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ShowInfo(List<product> allproductarrayfiltred) {
        for(int i = 0; i < allproductarrayfiltred.size(); i++){
            if(allproductarrayfiltred.get(i).Nom.equals(getIntent().getStringExtra("Nom"))){
                NomPord.setText((CharSequence) allproductarrayfiltred.get(i).Nom);
                NomProduct=allproductarrayfiltred.get(i).Nom.toString();
                amounts= allproductarrayfiltred.get(i).Montant_TTC;
                if (allproductarrayfiltred.get(i).Montant_TTC instanceof Double) {
                    Double doubleValue = (Double) allproductarrayfiltred.get(i).Montant_TTC;
                    amounts = doubleValue.longValue(); // Conversion en Long
                } else if (allproductarrayfiltred.get(i).Montant_TTC instanceof Long) {
                    amounts = allproductarrayfiltred.get(i).Montant_TTC;
                } else {
                    System.out.println("Type incompatible : " + allproductarrayfiltred.get(i).Montant_TTC.getClass().getSimpleName());
                }

                if(allproductarrayfiltred.get(i).Etat_prod.equals("neuf")) {
                    etat.setText(R.string.neuf);
                } else{
                    etat.setText(R.string._2_me_main);
                }

                prixProd.setText(allproductarrayfiltred.get(i).Montant_TTC+" "+"MAD");
                categoris.setText(allproductarrayfiltred.get(i).Catégorie.toString());
                description.setText(allproductarrayfiltred.get(i).Description.toString());


                List<String> productImages = allproductarrayfiltred.get(i).imageArrayList;
                ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getApplicationContext(), productImages);
                viewPager2.setAdapter(pagerAdapter);

             /*   if(allproductarrayfiltred.get(i).type.equals("Base64")) {
                    // Convert Base64 to Bitmap
                    Bitmap bitmap = base64ToBitmap(allproductarrayfiltred.get(i).Image.toString());
                    // Display the Bitmap in ImageView
                    if (bitmap != null) {
                        imageProd.setImageBitmap(bitmap);
                    } else {
                        imageProd.setImageResource(R.drawable.default_img);
                    }
                }else{
                    Glide.with(this)
                            .load(allproductarrayfiltred.get(i).Image) // Remplacez par la méthode pour obtenir l'URL de l'image
                            .placeholder(R.drawable.default_img) // Image de chargement par défaut
                            .error(R.drawable.default_img) // Image en cas d'erreur
                            .into(imageProd);
                }*/
            }
        }

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


    public void deleteprod(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsProductAtivity.this);
        builder.setTitle(R.string.voulez_vous_supprimer_ce_produit);
        builder.setItems(new CharSequence[]{getString(R.string.oui), getString(R.string.non)},
                (dialog, which) -> {
                    if (which == 0) {
                        removeClick();
                    }
                });
        builder.show();
    }

    public void removeClick() {
        String nomProduit = getIntent().getStringExtra("Nom"); // Nom du produit à supprimer

        if (nomProduit != null) {
            // Supprimer l'élément de Firebase
            allproduct.orderByChild("Nom").equalTo(nomProduit).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Supprimer chaque occurrence du produit avec ce nom
                        dataSnapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Suppression réussie dans Firebase
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsProductAtivity.this);
                                builder.setTitle("Suppression réussie");
                                builder.setItems(new CharSequence[]{getString(R.string.button_ok)},
                                        (dialog, which) -> {
                                            if (which == 0) {
                                                startActivity(new Intent(DetailsProductAtivity.this, MainVendeurActivity.class));
                                            }
                                        });
                                builder.show();
                            } else {
                                System.out.println("Erreur lors de la suppression du produit dans Firebase.");
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("Échec de la récupération des données Firebase : " + error.getMessage());
                }
            });
        } else {
            System.out.println("Aucun nom de produit spécifié pour la suppression.");
        }
    }

    public void Modifyprod(View view) {
        Intent i =new Intent(DetailsProductAtivity.this, ModificationProduitActivity.class);
        i.putExtra("Nom",NomProduct);
        startActivity(i);
    }
}