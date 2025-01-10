package com.example.joutiaapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joutiaapp.Models.User;
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

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.regex.Pattern;

public class InscriptionClientActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText mail, phone, pass, name, adresse;
    String categories, mailS, phoneS, passS, nameS, adresseS, villeS;
    double latitudeS, longetudeS;
    private FirebaseAuth mAuth;
    private DatabaseReference entrepriseRef;
    CountryCodePicker codePicker;

    private GoogleMap mMap;
    private Marker userMarker; // Pour afficher le marqueur sélectionné

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inscription_client);
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
        name = findViewById(R.id.nomComplet);
        adresse = findViewById(R.id.adresseLiv);

        // Get reference to the Spinner
        Spinner spinner = findViewById(R.id.spinner);
        Spinner spinner_ville = findViewById(R.id.spinner_ville);

        codePicker=findViewById(R.id.ccp);

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
                categories = selectedItem;
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
                villeS = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do something if nothing is selected (optional)
            }
        });
    }

    public void goHome(View view) {

        mailS = mail.getText().toString().trim();
        phoneS = phone.getText().toString().trim();
        passS = pass.getText().toString().trim();
        nameS = name.getText().toString().trim();
        adresseS = adresse.getText().toString().trim();

        if (mailS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.adresse_email_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else if (phoneS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.t_l_phone_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else if (passS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.mot_de_passe_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else if (nameS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.nom_complet_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else if (adresseS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.adresse_de_livraison_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else if (villeS.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.ville_obligatoire), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (!isValidEmailId(mailS.trim())) {
                Toast.makeText(getApplicationContext(), getString(R.string.adresse_email_invalide), Toast.LENGTH_SHORT).show();
                return;
            } else if (passS.length() < 8) {
                Toast.makeText(getApplicationContext(), getString(R.string.le_mot_de_passe_doit_contenir_8_caract_res), Toast.LENGTH_SHORT).show();
                return;
            } else {
                mAuth.createUserWithEmailAndPassword(mailS, passS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(mailS, "+"+codePicker.getSelectedCountryCode()+phoneS, passS, nameS, adresseS, villeS, categories,"0",0.0,0.0);
                            FirebaseDatabase.getInstance().getReference("client")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.votre_client_a_bien_t_ajouter), Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(InscriptionClientActivity.this, ConnexionActivity.class);
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
                                                startActivity(new Intent(InscriptionClientActivity.this, InscriptionClientActivity.class));
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

}