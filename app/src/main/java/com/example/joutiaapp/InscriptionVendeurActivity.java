package com.example.joutiaapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joutiaapp.Models.User;
import com.example.joutiaapp.Models.Vendeur;
import com.example.joutiaapp.Vendeur.VendeurPrincipalActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class InscriptionVendeurActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText mail, phone, nomag, adressemag, rib, pass;
    String MailS, phoneS, nomagS, adressemagS, villemagS, catmagS, ribS, passS;

    ImageView imageprofil;
    private FirebaseAuth mAuth;

    double latitudeS, longetudeS;
    private DatabaseReference entrepriseRef;
    CountryCodePicker codePicker;

    private GoogleMap mMap;
    private Marker userMarker; // Pour afficher le marqueur sélectionné

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_REQUEST_CODE = 100;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    ArrayList<String> ArrayImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inscription_vendeur);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        mAuth = FirebaseAuth.getInstance();
        entrepriseRef = FirebaseDatabase.getInstance().getReference().child("Client");

        mail = findViewById(R.id.username);
        phone = findViewById(R.id.edit_text);
        pass = findViewById(R.id.password);
        nomag = findViewById(R.id.nomMagasin);
        adressemag = findViewById(R.id.adresseMagasin);
        rib = findViewById(R.id.rib);
        imageprofil = findViewById(R.id.imgvendeur);

        codePicker=findViewById(R.id.ccp);

        Spinner spinner = findViewById(R.id.spinner);
        Spinner spinner_ville = findViewById(R.id.spinner_ville);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(this,
                R.array.Ville, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner_ville.setAdapter(adapters);

        // Set a listener on the spinner to handle selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                catmagS = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do something if nothing is selected (optional)
            }
        });

        spinner_ville.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                villemagS = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do something if nothing is selected (optional)
            }
        });

    }

    public void goHome(View view) {


        MailS = mail.getText().toString().trim();
        phoneS = phone.getText().toString().trim();
        nomagS = nomag.getText().toString().trim();
        passS = pass.getText().toString().trim();
        adressemagS = adressemag.getText().toString().trim();
        ribS = rib.getText().toString().trim();


        if (MailS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.adresse_email_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else if (phoneS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.t_l_phone_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else if (passS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.mot_de_passe_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else if (nomagS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.nom_du_magasin_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else if (adressemagS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.adresse_du_magasin_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else if (villemagS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.ville_du_magasin_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else {

            if (!isValidEmailId(MailS.trim())) {
                Toast.makeText(getApplicationContext(), getString(R.string.adresse_email_invalide), Toast.LENGTH_SHORT).show();
                return;
            } else if (passS.length() < 8) {
                Toast.makeText(getApplicationContext(), getString(R.string.le_mot_de_passe_doit_contenir_8_caract_res), Toast.LENGTH_SHORT).show();
                return;
            } else {
                mAuth.createUserWithEmailAndPassword(MailS, passS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Vendeur vendeur = new Vendeur(MailS, "+"+codePicker.getSelectedCountryCode()+phoneS, passS,nomagS,adressemagS,villemagS,catmagS, ribS,"1",0.0,0.0,ArrayImage);
                            FirebaseDatabase.getInstance().getReference("vendeur")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(vendeur).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.votre_client_a_bien_t_ajouter), Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(InscriptionVendeurActivity.this, ConnexionActivity.class);
                                                startActivity(i);
                                            } else {
                                                Toast.makeText(getApplicationContext(), getString(R.string.une_erreur_est_survenue_merci_de_r_essayer_plus_tard), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnCanceledListener(new OnCanceledListener() {
                                        @Override
                                        public void onCanceled() {
                                            Toast.makeText(getApplicationContext(), getString(R.string.une_erreur_est_survenue_merci_de_r_essayer_plus_tard), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {

                            // Gestion des erreurs
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                builder.setTitle(R.string.cet_e_mail_est_d_j_utilis_merci_d_utiliser_un_autre_e_mai);
                                builder.setItems(new CharSequence[]{getString(R.string.button_ok)},
                                        (dialog, which) -> {
                                            if (which == 0) {
                                                startActivity(new Intent(InscriptionVendeurActivity.this, InscriptionVendeurActivity.class));
                                            }
                                        });
                                builder.show();
                            } else {
                                // Autre erreur
                                Toast.makeText(getApplicationContext(), getString(R.string.une_erreur_est_survenue_merci_de_r_essayer_plus_tard), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(getApplicationContext(), getString(R.string.une_erreur_est_survenue_merci_de_r_essayer_plus_tard), Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }




    }

    private boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Centrer la carte sur une position par défaut
        LatLng defaultLocation = new LatLng(31.7917, -7.0926); // Maroc

        float zoomLevel = 6.0f; // Ajustez en fonction du niveau de détail souhaité

        // Déplacer et zoomer la caméra sur le Maroc
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, zoomLevel));


        getLastLocation();

        // Ajouter un événement de clic pour que l'utilisateur choisisse une position
        mMap.setOnMapClickListener(latLng -> {
            // Supprimer l'ancien marqueur, s'il existe
            if (userMarker != null) {
                userMarker.remove();
            }



            // Ajouter un nouveau marqueur à l'endroit cliqué
            userMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Position sélectionnée"));

            // Zoomer sur le marqueur
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            // Afficher les coordonnées sélectionnées
            double latitude = latLng.latitude;
            double longitude = latLng.longitude;

            latitudeS = latitude;
            longetudeS = longitude;

            // Afficher les coordonnées dans la console
            System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);
        });

    }

    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            latitudeS = latitude;
                            longetudeS = longitude;

                            LatLng defaultLocation = new LatLng(latitude, longitude);

                            // Ajouter un nouveau marqueur à l'endroit cliqué
                            userMarker = mMap.addMarker(new MarkerOptions()
                                    .position(defaultLocation)
                                    .title("Position sélectionnée"));

                            // Zoomer sur le marqueur
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    public void onuploadimage(View view) {
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
                        pickPhotoIntent.setAction(Intent.ACTION_GET_CONTENT);
                        launchSomeActivity.launch(pickPhotoIntent);
                    }
                });
        builder.show();
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

                            imageprofil.setImageBitmap(selectedImageBitmap);


                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            String base64String = convertBitmapToBase64(bitmap);
                            ArrayImage.add(base64String);
                            Toast.makeText(getApplicationContext(), "Votre image a été bien uploader", Toast.LENGTH_SHORT).show();
                        }
                        catch (IOException e) {
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

    private void checkCameraPermission() {
        // Check if Camera permission is already granted
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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

}
