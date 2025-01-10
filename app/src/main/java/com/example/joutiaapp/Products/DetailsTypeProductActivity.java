package com.example.joutiaapp.Products;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import android.Manifest;

import com.example.joutiaapp.Adapter.ViewPagerAdapter;
import com.example.joutiaapp.Models.PanierUser;
import com.example.joutiaapp.Models.Vendeur;
import com.example.joutiaapp.Models.product;
import com.example.joutiaapp.PanierActivity;
import com.example.joutiaapp.R;
import com.example.joutiaapp.Utils.SharedPreferencesUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class DetailsTypeProductActivity extends AppCompatActivity {

    ImageView imageProd;
    TextView NomPord;
    TextView NomMagasin;
    TextView PhoneMagasin;
    TextView prixProd;

    TextView description, etatprod;

    Object amounts;

    String urls;

    DatabaseReference allproduct;
    List<product> allproductarray = new ArrayList<>();
    List<product> allproductarrayfiltred = new ArrayList<>();

    ArrayList<PanierUser> panierUsers= new ArrayList<>();

    String PhoeVendeur;

    String stype;

    ImageView pictures;

    ViewPager2 viewPager2;

    List<Vendeur> allVendeurarray = new ArrayList<>();

    DatabaseReference allVendeur;

    private static final int REQUEST_CALL_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_type_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        panierUsers=SharedPreferencesUtils.getArray(this, "PanierKey");
        allVendeur = FirebaseDatabase.getInstance().getReference().child("vendeur");
        pictures = findViewById(R.id.pictures);
        viewPager2= findViewById(R.id.viewPager);
        if(panierUsers == null){
            panierUsers = new ArrayList<>();
        }
        imageProd = findViewById(R.id.Image_prod);
        NomPord = findViewById(R.id.nom_Prod);
        prixProd = findViewById(R.id.prix_prod);
        NomMagasin = findViewById(R.id.nommagasin);
        PhoneMagasin = findViewById(R.id.phone);
        description = findViewById(R.id.description);
        etatprod = findViewById(R.id.etatprod);

        allproduct = FirebaseDatabase.getInstance().getReference().child("product");

        Intent intent = getIntent();
        String value = intent.getStringExtra("Nom");
        String value2 = intent.getStringExtra("type");
        if (value != null || value2 != null) {
           getallprod();
        } else {

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
                    if(allproductarray.get(i).Catégorie.equals(getIntent().getStringExtra("type"))
                            && allproductarray.get(i).Approuved.equals(true) ){
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
                amounts= allproductarrayfiltred.get(i).Montant_TTC;
                if (allproductarrayfiltred.get(i).Montant_TTC instanceof Double) {
                    Double doubleValue = (Double) allproductarrayfiltred.get(i).Montant_TTC;
                    amounts = doubleValue.longValue(); // Conversion en Long
                } else if (allproductarrayfiltred.get(i).Montant_TTC instanceof Long) {
                    amounts = allproductarrayfiltred.get(i).Montant_TTC;
                } else {
                    System.out.println("Type incompatible : " + allproductarrayfiltred.get(i).Montant_TTC.getClass().getSimpleName());
                }

                prixProd.setText(allproductarrayfiltred.get(i).Montant_TTC+" "+"MAD");
                NomMagasin.setText(allproductarrayfiltred.get(i).NomMagasin.toString());
                getallVendeur(allproductarrayfiltred.get(i).NomMagasin.toString());
                PhoneMagasin.setText(allproductarrayfiltred.get(i).phone.toString());
                PhoeVendeur=allproductarrayfiltred.get(i).phone.toString();
                description.setText(allproductarrayfiltred.get(i).Description.toString());
                stype=allproductarrayfiltred.get(i).type.toString();

                if(allproductarrayfiltred.get(i).Etat_prod.equals("neuf")) {
                    etatprod.setText(R.string.neuf);
                } else{
                    etatprod.setText(R.string._2_me_main);
                }

                List<String> productImages = allproductarrayfiltred.get(i).imageArrayList;
                ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getApplicationContext(), productImages);
                viewPager2.setAdapter(pagerAdapter);

               // urls = (String) allproductarrayfiltred.get(i).Image;
               /*if(allproductarrayfiltred.get(i).type.equals("Base64")) {
                   // Convert Base64 to Bitmap
                   Bitmap bitmap = base64ToBitmap(urls);
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

    private void getallVendeur(String username) {
        allVendeurarray.clear();
        allVendeur.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allVendeurarray.add(postSnapshot.getValue(Vendeur.class));
                }
                for (int i = 0; i < allVendeurarray.size(); i++) {
                    if(allVendeurarray.get(i).nommagasin.equals(username)){
                        Bitmap bitmap = base64ToBitmap(allVendeurarray.get(i).ArrayImage.get(0));
                        if (bitmap != null) {
                            pictures.setImageBitmap(bitmap);
                        } else {
                            pictures.setImageResource(R.drawable.default_img);
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

    public void goAdd(View view) {
            PanierUser panierUser = new PanierUser(urls,NomPord.getText().toString(),amounts.toString(),stype);
            panierUsers.add(panierUser);
            SharedPreferencesUtils.saveArray(this, "PanierKey", panierUsers);
            Toast.makeText(this, "Produit ajouté au panier", Toast.LENGTH_SHORT).show();
    }

    public String urlToBase64(String imageUrl) {
        try {
            // Step 1: Open a connection to the URL
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            // Step 2: Download the image as an InputStream
            InputStream inputStream = connection.getInputStream();

            // Step 3: Convert InputStream to Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Step 4: Convert Bitmap to Base64
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // Use JPEG or PNG
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null in case of an error
        }
    }

    public void goPanier(View view) {
        Intent intent = new Intent(this, PanierActivity.class);
        startActivity(intent);
    }

    public void callVendeur(View view) {
        if(PhoeVendeur == null){
            Toast.makeText(this, R.string.le_num_ro_de_t_l_phone_est_vide,Toast.LENGTH_LONG).show();
        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PERMISSION_GRANTED) {
                // Demander la permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            } else {
                // Lancer l'appel
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + PhoeVendeur));
                startActivity(callIntent);
            }
        }
    }

    private void makePhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PERMISSION_GRANTED) {
            // Demander la permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            // Lancer l'appel
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                // Permission accordée
                makePhoneCall(PhoeVendeur);
            } else {
                // Permission refusée
                Toast.makeText(this, "Permission d'appel refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}