package com.example.joutiaapp.Vendeur;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.Manifest;

import com.example.joutiaapp.Models.Produit;
import com.example.joutiaapp.Models.Vendeur;
import com.example.joutiaapp.Models.product;
import com.example.joutiaapp.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class AddNewProductActivity extends AppCompatActivity {

    EditText nomProd, prixProd, qteProd, delaiProd;
    TextInputEditText descriptionProd;
    TextView text_no_image;
    RadioGroup radioGroup;

    ArrayList<String> ArrayBase64 = new ArrayList<>();

    String base64String;
    String Etat, cate, name, descrip, price, quantity, delay, nomMagasin, numero;
    ArrayList<String> ArrayImage = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference entrepriseRef;
    DatabaseReference Produiref, produitsref;
    private DatabaseReference reference;
    private String userId;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private LinearLayout linearImage;

    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("vendeur");
        userId = user.getUid();

        text_no_image = findViewById(R.id.text_no_image);
        linearImage = findViewById(R.id.linear_image);
        imageView = findViewById(R.id.imageupload);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Vendeur userProfile = snapshot.getValue(Vendeur.class);
                if (userProfile != null) {
                    nomMagasin = userProfile.nommagasin;
                    numero = userProfile.phone;
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.une_erreur_est_survenue_merci_de_r_essayer_plus_tard), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        Spinner spinner = findViewById(R.id.spinner);
        Spinner spinnerM = findViewById(R.id.spinner_marque);
        nomProd = findViewById(R.id.nomProd);
        descriptionProd = findViewById(R.id.descriptionProd);
        prixProd = findViewById(R.id.prixProd);
        qteProd = findViewById(R.id.qteProd);
        delaiProd = findViewById(R.id.delaiProd);
        radioGroup = findViewById(R.id.radioGroup);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        // Set a listener on the spinner to handle selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                cate = selectedItem;

                if (selectedItem.equals("Objets connectés")) {
                    ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(AddNewProductActivity.this,
                            R.array.connected, android.R.layout.simple_spinner_item);

                    // Specify the layout to use when the list of choices appears
                    adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Apply the adapter to the spinner
                    spinnerM.setAdapter(adapterM);

                } else if (selectedItem.equals("Beauté & Santé")) {
                    ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(AddNewProductActivity.this,
                            R.array.beauty, android.R.layout.simple_spinner_item);

                    // Specify the layout to use when the list of choices appears
                    adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Apply the adapter to the spinner
                    spinnerM.setAdapter(adapterM);

                } else if (selectedItem.equals("Sport & Loisir")) {
                    ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(AddNewProductActivity.this,
                            R.array.sports, android.R.layout.simple_spinner_item);

                    // Specify the layout to use when the list of choices appears
                    adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Apply the adapter to the spinner
                    spinnerM.setAdapter(adapterM);

                } else if (selectedItem.equals("Bébé & Jouets")) {
                    ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(AddNewProductActivity.this,
                            R.array.baby, android.R.layout.simple_spinner_item);

                    // Specify the layout to use when the list of choices appears
                    adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Apply the adapter to the spinner
                    spinnerM.setAdapter(adapterM);

                } else if (selectedItem.equals("Electroménager")) {
                    ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(AddNewProductActivity.this,
                            R.array.electro, android.R.layout.simple_spinner_item);

                    // Specify the layout to use when the list of choices appears
                    adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Apply the adapter to the spinner
                    spinnerM.setAdapter(adapterM);

                } else if (selectedItem.equals("Mode & Accessoires")) {
                    ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(AddNewProductActivity.this,
                            R.array.mode, android.R.layout.simple_spinner_item);

                    // Specify the layout to use when the list of choices appears
                    adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Apply the adapter to the spinner
                    spinnerM.setAdapter(adapterM);

                } else if (selectedItem.equals("Maison & Décoration")) {
                    ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(AddNewProductActivity.this,
                            R.array.maison, android.R.layout.simple_spinner_item);

                    // Specify the layout to use when the list of choices appears
                    adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Apply the adapter to the spinner
                    spinnerM.setAdapter(adapterM);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do something if nothing is selected (optional)
            }
        });

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

    }

    public void openUpload(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisir une option");
        builder.setItems(new CharSequence[]{"Prendre une photo", "Choisir depuis la galerie"},
                (dialog, which) -> {
                    if (which == 0) {
                        checkCameraPermission();
                    } else {
                        // Choisir une image depuis la galerie
                        Intent pickPhotoIntent = new Intent();
                        pickPhotoIntent.setType("image/*");
                        pickPhotoIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(Intent.createChooser(pickPhotoIntent, "Sélectionnez des images"), REQUEST_IMAGE_CAPTURE);
                        //launchSomeActivity.launch(pickPhotoIntent);
                    }
                });
        builder.show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            ArrayList<Uri> imageUris = new ArrayList<>();

            // Récupération des images sélectionnées
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri);
                }
            } else if (data.getData() != null) {
                imageUris.add(data.getData());
            }

            // Traitement des images
            for (Uri imageUri : imageUris) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    String base64String = encodeToBase64(bitmap);
                    ArrayBase64.add(base64String);
                    if(ArrayBase64.size()==3){
                        imageView.setEnabled(false);
                        imageView.setAlpha(0.5f);
                    }
                    addImageThumbnail(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addImageThumbnail(Bitmap bitmap) {
        text_no_image.setVisibility(View.GONE);
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        params.setMargins(8, 8, 8, 8);
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(bitmap);
        linearImage.addView(imageView);
    }

    private String encodeToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void checkCameraPermission() {
        // Check if Camera permission is already granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, you can open the camera
            openCamera();
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                ArrayImage.clear();
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap;
                        Bitmap selectedImage;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                            InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                            selectedImage = BitmapFactory.decodeStream(imageStream);

                            ImageView imageView = new ImageView(this);
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));
                            imageView.setImageBitmap(selectedImageBitmap);

                            // Ajouter l'ImageView au LinearLayout
                            text_no_image.setVisibility(View.GONE);
                            linearImage.addView(imageView);

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            base64String = convertBitmapToBase64(bitmap);
                            ArrayImage.add(base64String);
                            Toast.makeText(getApplicationContext(), "Votre image a été bien uploader", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void onAddProduct(View view) {
        Produiref = FirebaseDatabase.getInstance().getReference().child("Produit");
        produitsref = FirebaseDatabase.getInstance().getReference().child("product");
        name = nomProd.getText().toString().trim();
        descrip = descriptionProd.getText().toString().trim();
        price = prixProd.getText().toString().trim();
        quantity = qteProd.getText().toString().trim();
        delay = delaiProd.getText().toString().trim();
        Random random = new Random();
        // Génère un nombre entier aléatoire entre 0 et 99
        int randomId = random.nextInt(1000);
        int randomRef = random.nextInt(1000);
        int randomPos = random.nextInt(1000);

        if (name.isEmpty() || descrip.isEmpty() || price.isEmpty() || quantity.isEmpty() || delay.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.merci_de_remplir_tout_les_champs), Toast.LENGTH_SHORT).show();
        } else {
            product prod = new product(randomId, ArrayBase64, name, String.valueOf(randomRef), cate, price, price, Integer.parseInt(quantity), 0, Long.getLong(String.valueOf(randomPos)), "Base64", nomMagasin, numero, descrip,true,"0",Etat);
            produitsref.push().setValue(prod);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage(getString(R.string.produit_ajout))
                    .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(AddNewProductActivity.this, MainVendeurActivity.class));
                            finish();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            startActivity(new Intent(AddNewProductActivity.this, MainVendeurActivity.class));
                            finish();
                        }
                    })
                    .show();
        }

    }

    public void onBackvClick(View view) {
        startActivity(new Intent(AddNewProductActivity.this, MainVendeurActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayImage.clear();
        linearImage.removeAllViews();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Récupérer la photo en tant que bitmap
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");


            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            imageView.setImageBitmap(imageBitmap);

            base64String = convertBitmapToBase64(imageBitmap);
            ArrayImage.add(base64String);

            // Ajouter l'ImageView au LinearLayout
            text_no_image.setVisibility(View.GONE);
            linearImage.addView(imageView);
        }
    }*/
}