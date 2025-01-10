package com.example.joutiaapp.Vendeur;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.joutiaapp.Adapter.ViewPagerAdapter;
import com.example.joutiaapp.Models.product;
import com.example.joutiaapp.R;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModificationProduitActivity extends AppCompatActivity {

    EditText nomprod, prixprod, qteprod;

    TextInputEditText descriptionprod;

    DatabaseReference allproduct;
    List<product> allproductarray = new ArrayList<>();
    List<product> allproductarrayfiltred = new ArrayList<>();

    Object amounts;

    ImageView imageProd;

    String NomProduct, description, prix, qte, Etat;

    RadioGroup radioGroup;

    LinearLayout laineImage;

    RadioButton rd_neuf, rd_second;

    ViewPager2 viewPager2;

    List<String> productImages;
    ViewPagerAdapter pagerAdapter;

    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modification_produit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nomprod = findViewById(R.id.nomProd);
        descriptionprod = findViewById(R.id.DescriptionProd);
        prixprod = findViewById(R.id.PrixProd);
        qteprod = findViewById(R.id.QteProd);
        imageProd = findViewById(R.id.Image_prod);
        radioGroup = findViewById(R.id.radioGroup);
        rd_neuf = findViewById(R.id.neuf);
        rd_second = findViewById(R.id.second);
        laineImage = findViewById(R.id.linear_image);

        viewPager2 = findViewById(R.id.viewPager);

        allproduct = FirebaseDatabase.getInstance().getReference().child("product");

        Intent intent = getIntent();
        String value = intent.getStringExtra("Nom");
        if (value != null) {
            getallprod();
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.neuf){
                    Etat="neuf";
                }else if(checkedId == R.id.second) {
                    Etat="2 eme main";
                }
            }
        });




        descriptionprod.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            if ((event.getAction() & event.getActionMasked()) == android.view.MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });
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
                    if(allproductarray.get(i).Nom.equals(getIntent().getStringExtra("Nom"))
                            && allproductarray.get(i).Approuved.equals(true)){
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
                nomprod.setText((CharSequence) allproductarrayfiltred.get(i).Nom);
                amounts= allproductarrayfiltred.get(i).Montant_TTC;
                if (allproductarrayfiltred.get(i).Montant_TTC instanceof Double) {
                    Double doubleValue = (Double) allproductarrayfiltred.get(i).Montant_TTC;
                    amounts = doubleValue.longValue(); // Conversion en Long
                } else if (allproductarrayfiltred.get(i).Montant_TTC instanceof Long) {
                    amounts = allproductarrayfiltred.get(i).Montant_TTC;
                } else {
                    System.out.println("Type incompatible : " + allproductarrayfiltred.get(i).Montant_TTC.getClass().getSimpleName());
                }
                prixprod.setText(allproductarrayfiltred.get(i).Montant_TTC.toString());
                descriptionprod.setText(allproductarrayfiltred.get(i).Description.toString());
                qteprod.setText(allproductarrayfiltred.get(i).Quantité.toString());

                productImages = allproductarrayfiltred.get(i).imageArrayList;
                pagerAdapter = new ViewPagerAdapter(getApplicationContext(), productImages);
                viewPager2.setAdapter(pagerAdapter);

              /*  if(allproductarrayfiltred.get(i).type.equals("Base64")) {
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

                if(allproductarrayfiltred.get(i).Etat_prod.equals("neuf")){
                    rd_neuf.setChecked(true);
                    rd_second.setChecked(false);
                }else if(allproductarrayfiltred.get(i).Etat_prod.equals("second")) {
                    rd_neuf.setChecked(false);
                    rd_second.setChecked(true);
                }
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


    public void Modify(View view) {

        NomProduct = nomprod.getText().toString();
        description = descriptionprod.getText().toString();
        prix = prixprod.getText().toString();
        qte = qteprod.getText().toString();

        if(NomProduct.isEmpty() || description.isEmpty() || prix.isEmpty() || qte.isEmpty()){
            Toast.makeText(ModificationProduitActivity.this, getString(R.string.tout_les_champs_sont_obligatoires),Toast.LENGTH_LONG).show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ModificationProduitActivity.this);
            builder.setTitle(R.string.voulez_vous_modifier_ce_produit);
            builder.setItems(new CharSequence[]{getString(R.string.oui), getString(R.string.non)},
                    (dialog, which) -> {
                        if (which == 0) {
                            ModifyClick();
                        }
                    });
            builder.show();
        }

    }

    private void ModifyClick() {
        allproduct.orderByChild("Nom").equalTo(NomProduct).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Récupérer la clé de l'utilisateur
                    String userId = userSnapshot.getKey();

                    // Créer une mise à jour
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("Description", description);
                    updates.put("Montant_HT", prix);
                    updates.put("Montant_TTC", prix);
                    updates.put("Quantité", qte);
                    updates.put("State", "0");
                    updates.put("Etat_prod", Etat);

                    // Appliquer la mise à jour
                    allproduct.child(userId).updateChildren(updates)
                            .addOnSuccessListener(aVoid -> {
                                // Mise à jour réussie
                                AlertDialog.Builder builder = new AlertDialog.Builder(ModificationProduitActivity.this);
                                builder.setTitle("bien modifé");
                                builder.setItems(new CharSequence[]{getString(R.string.button_ok)},
                                        (dialog, which) -> {
                                            if (which == 0) {
                                                startActivity(new Intent(ModificationProduitActivity.this, MainVendeurActivity.class));
                                            }
                                        });
                                builder.show();
                            })
                            .addOnFailureListener(e -> {
                                // Erreur lors de la mise à jour
                                AlertDialog.Builder builder = new AlertDialog.Builder(ModificationProduitActivity.this);
                                builder.setTitle("No modifé");
                                builder.setItems(new CharSequence[]{getString(R.string.button_ok)},
                                        (dialog, which) -> {
                                            if (which == 0) {
                                                startActivity(new Intent(ModificationProduitActivity.this, MainVendeurActivity.class));
                                            }
                                        });
                                builder.show();
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer les erreurs
                Log.e("Firebase", "Erreur : " + databaseError.getMessage());
            }
        });

    }

    public void ModifyPictures(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ModificationProduitActivity.this);
        builder.setTitle("Voulez vous supprimer cette images ?");
        builder.setItems(new CharSequence[]{getString(R.string.oui), getString(R.string.non)},
                (dialog, which) -> {
                    if (which == 0) {
                       DeleteImage();
                    }
                });
        builder.show();
    }

    private void DeleteImage() {
        int currentPosition = viewPager2.getCurrentItem();
        if (!productImages.isEmpty() && currentPosition >= 0) {
            productImages.remove(currentPosition);
            pagerAdapter.notifyDataSetChanged();
            ModifyNow(productImages);
            Toast.makeText(this, "Image supprimée", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Aucune image à supprimer", Toast.LENGTH_SHORT).show();
        }

    }

    public void AddPictures(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ModificationProduitActivity.this);
        builder.setTitle("Voulez vous Ajouter une images ?");
        builder.setItems(new CharSequence[]{getString(R.string.oui), getString(R.string.non)},
                (dialog, which) -> {
                    if (which == 0) {
                     AddImage();
                    }
                });
        builder.show();
    }

    private void AddImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Sélectionnez une image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Obtenir l'URI de l'image sélectionnée
            Uri imageUri = data.getData();

            // Ajouter l'image à la liste `productImages`
            String imageUrl = uriToBase64(imageUri); // Convertir en chaîne
            productImages.add(imageUrl);

            ModifyNow(productImages);
            // Notifier l'adaptateur
            pagerAdapter.notifyDataSetChanged();

        } else {
            Toast.makeText(this, "Aucune image sélectionnée", Toast.LENGTH_SHORT).show();
        }
    }

    private void ModifyNow(List<String> productImages) {
        NomProduct = nomprod.getText().toString();
        allproduct.orderByChild("Nom").equalTo(NomProduct).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Récupérer la clé de l'utilisateur
                    String userId = userSnapshot.getKey();

                    // Créer une mise à jour
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("imageArrayList", productImages);
                    // Appliquer la mise à jour
                    allproduct.child(userId).updateChildren(updates)
                            .addOnSuccessListener(aVoid -> {
                                // Mise à jour réussie
                                Toast.makeText(ModificationProduitActivity.this, "Image ajoutée", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Erreur lors de la mise à jour
                                Toast.makeText(ModificationProduitActivity.this, "Image pas ajoutée", Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer les erreurs
                Log.e("Firebase", "Erreur : " + databaseError.getMessage());
            }
        });

    }

    private String uriToBase64(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}