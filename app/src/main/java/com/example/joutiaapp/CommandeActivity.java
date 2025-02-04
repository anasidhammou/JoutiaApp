package com.example.joutiaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joutiaapp.Adapter.CustomCommandeAdapter;
import com.example.joutiaapp.Adapter.CustomListStatusAdapter;
import com.example.joutiaapp.Adapter.ExpandableListAdapter;
import com.example.joutiaapp.Models.Commande;
import com.example.joutiaapp.Models.PanierUser;
import com.example.joutiaapp.Models.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommandeActivity extends AppCompatActivity {

    List<Status> liststate = new ArrayList<>();
    RecyclerView recyclerView, recyclerView_filtred;
    private CustomListStatusAdapter adapter;

    private CustomCommandeAdapter adapterfiltred;

    public ArrayList<Commande> allcommandearrayfiltred = new ArrayList<>();
    public ArrayList<Commande> allcommandearray = new ArrayList<>();

    public ArrayList<String> titleList = new ArrayList<>();

    public ArrayList<ArrayList<PanierUser>> panierUserArrayList = new ArrayList<ArrayList<PanierUser>>();

    DatabaseReference allCommande;

    TextView noCommande;

    String name;

    ExpandableListView expandableListViewExample;

    ExpandableListAdapter expandableListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_commande);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        String appLanguage = getAppLanguage();

        if(appLanguage.equals("ar")){

            Status status0 = new Status("0", "في تَقَدم");
            Status status1 = new Status("1", "كسوة");
            Status status2 = new Status("2","رفض");
            Status status3 = new Status("2","تم الإلغاء");


            liststate.add(status0);
            liststate.add(status1);
            liststate.add(status2);
            liststate.add(status3);

        }else {

            Status status0 = new Status("0", "En cours");
            Status status1 = new Status("1", "Livrée");
            Status status2 = new Status("2", "Refusée");
            Status status3 = new Status("2", "Annulée");

            liststate.add(status0);
            liststate.add(status1);
            liststate.add(status2);
            liststate.add(status3);

        }

        expandableListViewExample=findViewById(R.id.expandableListViewSample);

        allCommande = FirebaseDatabase.getInstance().getReference().child("Commande");
        noCommande = findViewById(R.id.nocommande);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView_filtred = findViewById(R.id.recycler_view_filtred);

        initialiserecycler();
        getallCommade();
    }


    public String getAppLanguage() {
        Configuration config = getResources().getConfiguration();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return config.getLocales().get(0).getLanguage(); // Pour Android 7.0+ (API 24+)
        } else {
            return config.locale.getLanguage(); // Pour Android plus ancien
        }
    }


    private void getallCommade() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("NomUser", "");
        allcommandearray.clear();
        allcommandearrayfiltred.clear();
        allCommande.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allcommandearray.add(postSnapshot.getValue(Commande.class));
                }
                for (int i = 0; i < allcommandearray.size(); i++) {
                    if(allcommandearray.get(i).NomClient.equals(name) && allcommandearray.get(i).Etat.equals("0")){
                        allcommandearrayfiltred.add(allcommandearray.get(i));
                    }
                }
                initialiserecyclerfiltred(allcommandearrayfiltred);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialiserecycler() {

        adapter = new CustomListStatusAdapter(CommandeActivity.this, liststate);
        recyclerView.setLayoutManager(new LinearLayoutManager(CommandeActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        allcommandearrayfiltred.clear();
        adapter.setOnDataChangeListener(new CustomListStatusAdapter.OnDataChangeListener() {
            @Override
            public void showDetail(Object object) {

                allcommandearrayfiltred.clear();
                for (int i = 0; i < allcommandearray.size(); i++) {
                    if(allcommandearray.get(i).NomClient.equals(name) && allcommandearray.get(i).Etat.equals(object)){
                        allcommandearrayfiltred.add(allcommandearray.get(i));
                    }
                }

                Log.d("value size", String.valueOf(allcommandearrayfiltred.size()));

                initialiserecyclerfiltred(allcommandearrayfiltred);
            }
        });
    }

    private void initialiserecyclerfiltred(ArrayList<Commande> allcommandearrayfiltred) {

        if(allcommandearrayfiltred.isEmpty()){
            noCommande.setVisibility(View.VISIBLE);
        }else {
            noCommande.setVisibility(View.GONE);
            expandableListAdapter  = new ExpandableListAdapter(CommandeActivity.this, allcommandearrayfiltred);
            expandableListViewExample.setAdapter(expandableListAdapter);
            expandableListAdapter.notifyDataSetChanged();



        }


    }
}