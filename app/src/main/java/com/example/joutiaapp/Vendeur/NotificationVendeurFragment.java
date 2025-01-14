package com.example.joutiaapp.Vendeur;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joutiaapp.Adapter.NotificationAdapter;
import com.example.joutiaapp.Models.Notifications;
import com.example.joutiaapp.Models.product;
import com.example.joutiaapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationVendeurFragment extends Fragment {

    DatabaseReference allnotification;
    List<Notifications> allproductarray = new ArrayList<>();
    List<Notifications> allproductarrayfiltred = new ArrayList<>();

    TextView noproduit;

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_vendeur, container, false);
        allnotification = FirebaseDatabase.getInstance().getReference().child("notification");
        findView(view);
        return view;
    }

    private void findView(View view) {
        noproduit = view.findViewById(R.id.noNotif);
        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        verifyLogin();
    }


    private void verifyLogin() {
        if (isUserLoggedIn(getContext())) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String name = sharedPreferences.getString("NomUser", "default_username");
            getallNotif(name);
        }
    }

    private void getallNotif(String name) {
        allproductarray.clear();
        allproductarrayfiltred.clear();
        allnotification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allproductarray.add(postSnapshot.getValue(Notifications.class));
                }
                for (int i = 0; i < allproductarray.size(); i++) {
                    if(allproductarray.get(i).NomVendeur.equals(name)){
                        allproductarrayfiltred.add(allproductarray.get(i));
                    }
                }

                if(allproductarrayfiltred.size()==0){
                    noproduit.setVisibility(View.VISIBLE);
                }else{
                    noproduit.setVisibility(View.GONE);
                }

                initialiserecyclerdfilter(allproductarrayfiltred);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialiserecyclerdfilter(List<Notifications> allproductarrayfiltred) {
        NotificationAdapter adapter = new NotificationAdapter(allproductarrayfiltred);
        recyclerView.setAdapter(adapter);

        adapter.setOnDataChangeListener(new NotificationAdapter.OnDataChangeListener() {
            @Override
            public void showDetail(Object object) {
                Intent i = new Intent(getContext(),DetailsNotificationActivity.class);
                i.putExtra("Id",object.toString());
                startActivity(i);
            }
        });
    }

    private boolean isUserLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}