package com.example.joutiaapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    EditText mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();

        mail=findViewById(R.id.username);
    }

    public void goConnexion(View view) {
        startActivity(new Intent(ForgetPasswordActivity.this, ConnexionActivity.class));
    }

    public void goForget(View view) {
        String email = mail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ForgetPasswordActivity.this, R.string.adresse_email_obligatoire, Toast.LENGTH_SHORT).show();
            return;
        }

        // Send password reset email
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgetPasswordActivity.this, R.string.l_email_de_r_cup_ration_envoy_avec_succes_merci_de_v_rifier_votre_adresse_mail, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ForgetPasswordActivity.this, ConnexionActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}