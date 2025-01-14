package com.example.joutiaapp.Vendeur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joutiaapp.Models.Notifications;
import com.example.joutiaapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsNotificationActivity extends AppCompatActivity {

    DatabaseReference allnotification;
    List<Notifications> allproductarray = new ArrayList<>();
    List<Notifications> allproductarrayfiltred = new ArrayList<>();

    String value;

    TextView date,titre,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        allnotification = FirebaseDatabase.getInstance().getReference().child("notification");

        date = findViewById(R.id.date);
        titre = findViewById(R.id.titre);
        description = findViewById(R.id.description);

        Intent intent = getIntent();
        value = intent.getStringExtra("Id");
        if (value != null) {
            getallnotif(value);
        }
    }

    private void getallnotif(String value) {
        allproductarray.clear();
        allproductarrayfiltred.clear();
        allnotification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allproductarray.add(postSnapshot.getValue(Notifications.class));
                }
                for (int i = 0; i < allproductarray.size(); i++) {
                    if(allproductarray.get(i).Id == Integer.parseInt(value)){
                        allproductarrayfiltred.add(allproductarray.get(i));
                    }
                }

                initialiseData(allproductarrayfiltred);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialiseData(List<Notifications> allproductarrayfiltred) {
        date.setText(allproductarrayfiltred.get(0).Date);
        titre.setText(allproductarrayfiltred.get(0).titre);
        description.setText(allproductarrayfiltred.get(0).description);
    }
}