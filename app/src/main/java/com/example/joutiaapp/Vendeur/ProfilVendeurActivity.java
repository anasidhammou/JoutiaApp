package com.example.joutiaapp.Vendeur;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joutiaapp.Models.User;
import com.example.joutiaapp.Models.Vendeur;
import com.example.joutiaapp.R;
import com.google.android.gms.common.util.IOUtils;
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

public class ProfilVendeurActivity extends AppCompatActivity {

    EditText edtmail, edtadresse, edtville, edtPhone, edtnomM, edtRib;

    DatabaseReference allVendeur;

    List<Vendeur> allUserarray = new ArrayList<>();

    ImageView imageView;

    private static final int PICK_IMAGE_REQUEST = 1;

    public ArrayList<String> ArrayImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil_vendeur);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtmail = findViewById(R.id.edtmail);
        edtadresse = findViewById(R.id.edtadresse);
        edtville = findViewById(R.id.edtville);
        edtPhone = findViewById(R.id.edit_text);
        edtnomM = findViewById(R.id.edtnomM);
        edtRib = findViewById(R.id.edtRib);
        imageView = findViewById(R.id.imgvendeur);

        allVendeur = FirebaseDatabase.getInstance().getReference().child("vendeur");

        ShowVariable();
        getallUser();

    }

    private void ShowVariable() {
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("mailUser", "default_username");
            String name = sharedPreferences.getString("NomUser", "default_username");
            String adresse = sharedPreferences.getString("AdresseVendeur", "default_username");
            String ville = sharedPreferences.getString("VilleVendeur", "default_username");
            String phone = sharedPreferences.getString("PhoneVendeur", "default_username");
            String rib = sharedPreferences.getString("RibVendeur", "default_username");

            edtmail.setText(username);
            edtadresse.setText(adresse);
            edtville.setText(ville);
            edtPhone.setText(phone);
            edtRib.setText(rib);
            edtnomM.setText(name);
    }

    public void miseAjour(View view) {

        String mail = edtmail.getText().toString();
        String adresse = edtadresse.getText().toString();
        String ville = edtville.getText().toString();
        String phone = edtPhone.getText().toString();
        String rib = edtRib.getText().toString();
        String nom = edtnomM.getText().toString();

        if(mail.isEmpty() || adresse.isEmpty() || ville.isEmpty() || phone.isEmpty() || rib.isEmpty() || nom.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfilVendeurActivity.this);
            builder.setTitle(R.string.veuillez_remplir_tous_les_champs);
            builder.setItems(new CharSequence[]{getString(R.string.button_ok)},
                    (dialog, which) -> {
                    });
            builder.show();
        }else{

            setadressVendeurLoggedIn(getApplicationContext(),adresse);
            setvilleVendeurLoggedIn(getApplicationContext(),ville);
            setphoneVendeurLoggedIn(getApplicationContext(),phone);
            setRibVendeurLoggedIn(getApplicationContext(),rib);

            allVendeur.orderByChild("nommagasin").equalTo(nom).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Récupérer la clé de l'utilisateur
                        String userId = userSnapshot.getKey();

                        // Créer une mise à jour
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("adressemagasin", adresse);
                        updates.put("villemagasin", ville);
                        updates.put("phone", phone);
                        updates.put("rib", rib);

                        // Appliquer la mise à jour
                        allVendeur.child(userId).updateChildren(updates)
                                .addOnSuccessListener(aVoid -> {
                                    // Mise à jour réussie
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfilVendeurActivity.this);
                                    builder.setTitle(R.string.les_informations_ont_t_bien_modifi);
                                    builder.setItems(new CharSequence[]{getString(R.string.button_ok)},
                                            (dialog, which) -> {
                                                if (which == 0) {
                                                    startActivity(new Intent(ProfilVendeurActivity.this, MainVendeurActivity.class));
                                                }
                                            });
                                    builder.show();
                                })
                                .addOnFailureListener(e -> {
                                    // Erreur lors de la mise à jour
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfilVendeurActivity.this);
                                    builder.setTitle("No modifé");
                                    builder.setItems(new CharSequence[]{getString(R.string.button_ok)},
                                            (dialog, which) -> {
                                                if (which == 0) {
                                                    startActivity(new Intent(ProfilVendeurActivity.this, MainVendeurActivity.class));
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
    }


    private void getallUser() {

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("mailUser", "default_username");

        allUserarray.clear();
        allVendeur.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allUserarray.add(postSnapshot.getValue(Vendeur.class));
                }
                for (int i = 0; i < allUserarray.size(); i++) {
                    if(allUserarray.get(i).mail.equals(username)){
                        Bitmap bitmap = base64ToBitmap(allUserarray.get(i).ArrayImage.get(0));
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


    public void setadressVendeurLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AdresseVendeur",Name);
        editor.apply();
    }


    public void setvilleVendeurLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("VilleVendeur",Name);
        editor.apply();
    }

    public void setphoneVendeurLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PhoneVendeur",Name);
        editor.apply();
    }

    public void setRibVendeurLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("RibVendeur",Name);
        editor.apply();
    }

    public void ModifyImage(View view) {
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
            ArrayImage.add(imageUrl);

            Bitmap bitmap = base64ToBitmap(imageUrl);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.default_img);
            }

            ModifyNow(ArrayImage);



        } else {
            Toast.makeText(this, "Aucune image sélectionnée", Toast.LENGTH_SHORT).show();
        }
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

    private void ModifyNow(ArrayList<String> arrayImage) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("mailUser", "default_username");

        allVendeur.orderByChild("mail").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Récupérer la clé de l'utilisateur
                    String userId = userSnapshot.getKey();

                    // Créer une mise à jour
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("ArrayImage", arrayImage);
                    // Appliquer la mise à jour
                    allVendeur.child(userId).updateChildren(updates)
                            .addOnSuccessListener(aVoid -> {
                                // Mise à jour réussie
                                Toast.makeText(ProfilVendeurActivity.this, "Image modifié", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Erreur lors de la mise à jour
                                Toast.makeText(ProfilVendeurActivity.this, "Image pas modifié", Toast.LENGTH_SHORT).show();
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
}