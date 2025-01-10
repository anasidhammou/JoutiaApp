package com.example.joutiaapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joutiaapp.Models.User;
import com.example.joutiaapp.Models.Vendeur;
import com.example.joutiaapp.Models.product;
import com.example.joutiaapp.Vendeur.MainVendeurActivity;
import com.example.joutiaapp.Vendeur.VendeurPrincipalActivity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ConnexionActivity extends AppCompatActivity {

    EditText edt_mail;
    TextInputEditText edt_pass;
    String mail, pass, type = "Client";
    RadioGroup radioGroup;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    List<User> allUserarray = new ArrayList<>();
    List<Vendeur> allVendeurarray = new ArrayList<>();
    String NameComplet="",adresse="",ville="",phone="";
    DatabaseReference allClient;
    DatabaseReference allVendeur;

    String  PhoneV, VilleV, AdresseV, RibV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_connexion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        allClient = FirebaseDatabase.getInstance().getReference().child("client");
        allVendeur = FirebaseDatabase.getInstance().getReference().child("vendeur");
        mAuth = FirebaseAuth.getInstance();
        radioGroup = findViewById(R.id.radioGroup);
        edt_mail = findViewById(R.id.username);
        edt_pass = findViewById(R.id.password);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // on below line we are getting radio button from our group.
                RadioButton radioButton = findViewById(checkedId);

                // on below line we are displaying a toast message.
                type = radioButton.getText().toString();
            }
        });
    }

    public void goInscription(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
        builder.setTitle(R.string.voulez_vous_vous_inscrire_autant_que);
        builder.setItems(new CharSequence[]{getString(R.string.client), getString(R.string.vendeur)},
                (dialog, which) -> {
                    if (which == 0) {
                        startActivity(new Intent(ConnexionActivity.this, InscriptionClientActivity.class));
                    } else {
                        startActivity(new Intent(ConnexionActivity.this, InscriptionVendeurActivity.class));
                    }
                });
        builder.show();
    }

    public void goHome(View view) {

        mail = edt_mail.getText().toString().trim();
        pass = edt_pass.getText().toString().trim();

        if (mail.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.les_2_champs_sont_obligatoires), Toast.LENGTH_SHORT).show();
        } else {
            if (!isValidEmailId(mail.trim())) {
                Toast.makeText(getApplicationContext(), getString(R.string.adresse_email_invalide), Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (type.equals("Client")) {
                    mAuth.signInWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        user = FirebaseAuth.getInstance().getCurrentUser();
                                        reference = FirebaseDatabase.getInstance().getReference("client");
                                        userId = user.getUid();
                                        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                User userProfile = snapshot.getValue(User.class);
                                                if (userProfile != null) {
                                                    Intent i = new Intent(ConnexionActivity.this, MainActivity.class);
                                                    setUserLoggedIn(getApplicationContext());
                                                    setMailUserLoggedIn(getApplicationContext(),mail);
                                                    setTypeUserLoggedIn(getApplicationContext(),"Client");
                                                    getallUser();
                                                    startActivity(i);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), getString(R.string.une_erreur_est_survenue_merci_de_r_essayer_plus_tard), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.une_erreur_est_survenue_merci_de_r_essayer_plus_tard), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {

                                    }
                                }
                            }).addOnCanceledListener(new OnCanceledListener() {
                                @Override
                                public void onCanceled() {
                                    Toast.makeText(getApplicationContext(), getString(R.string.une_erreur_est_survenue_merci_de_r_essayer_plus_tard), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    mAuth.signInWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        user = FirebaseAuth.getInstance().getCurrentUser();
                                        reference = FirebaseDatabase.getInstance().getReference("vendeur");
                                        userId = user.getUid();
                                        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Vendeur userProfile = snapshot.getValue(Vendeur.class);
                                                if (userProfile != null) {
                                                    Intent i = new Intent(ConnexionActivity.this, MainVendeurActivity.class);
                                                    setUserLoggedIn(getApplicationContext());
                                                    setMailUserLoggedIn(getApplicationContext(),mail);
                                                    setTypeUserLoggedIn(getApplicationContext(),"Vendeur");
                                                    getallVendeur();
                                                    startActivity(i);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), getString(R.string.une_erreur_est_survenue_merci_de_r_essayer_plus_tard), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.une_erreur_est_survenue_merci_de_r_essayer_plus_tard), Toast.LENGTH_SHORT).show();
                                            }
                                        });
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
                }
            }
        }


    }

    public void setUserLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    public void setMailUserLoggedIn(Context context,String Mail) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mailUser",Mail);
        editor.apply();
    }


    public void setTypeUserLoggedIn(Context context,String type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type",type);
        editor.apply();
    }

    public void setNomUserLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NomUser",Name);
        editor.apply();
    }

    public void setadressUserLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AdresseUser",Name);
        editor.apply();
    }


    public void setvilleUserLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("VilleUser",Name);
        editor.apply();
    }

    public void setphoneUserLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PhoneUser",Name);
        editor.apply();
    }

    public void setNomMagasinLoggedIn(Context context,String Name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NomMagasin",Name);
        editor.apply();
    }

    private void getallUser() {
        allUserarray.clear();
        allClient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allUserarray.add(postSnapshot.getValue(User.class));
                }
                for (int i = 0; i < allUserarray.size(); i++) {
                    if(allUserarray.get(i).mail.equals(mail)){
                        NameComplet=allUserarray.get(i).nomcomplet;
                        adresse=allUserarray.get(i).adresselivraison;
                        ville=allUserarray.get(i).ville;
                        phone=allUserarray.get(i).phone;
                    }
                }
                setNomUserLoggedIn(getApplicationContext(),NameComplet);
                setphoneUserLoggedIn(getApplicationContext(),phone);
                setadressUserLoggedIn(getApplicationContext(),adresse);
                setvilleUserLoggedIn(getApplicationContext(),ville);
                setNomMagasinLoggedIn(getApplicationContext(),"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getallVendeur() {
        allVendeurarray.clear();
        allVendeur.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allVendeurarray.add(postSnapshot.getValue(Vendeur.class));
                }
                for (int i = 0; i < allVendeurarray.size(); i++) {
                    if(allVendeurarray.get(i).mail.equals(mail)){
                        NameComplet=allVendeurarray.get(i).nommagasin;
                        PhoneV=allVendeurarray.get(i).phone;
                        VilleV=allVendeurarray.get(i).villemagasin;
                        AdresseV=allVendeurarray.get(i).adressemagasin;
                        RibV=allVendeurarray.get(i).rib;
                    }
                }
                setNomUserLoggedIn(getApplicationContext(),NameComplet);
                setphoneVendeurLoggedIn(getApplicationContext(),PhoneV);
                setvilleVendeurLoggedIn(getApplicationContext(),VilleV);
                setadressVendeurLoggedIn(getApplicationContext(),AdresseV);
                setRibVendeurLoggedIn(getApplicationContext(),RibV);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    private boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void goFpw(View view) {
        startActivity(new Intent(ConnexionActivity.this, ForgetPasswordActivity.class));
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
}