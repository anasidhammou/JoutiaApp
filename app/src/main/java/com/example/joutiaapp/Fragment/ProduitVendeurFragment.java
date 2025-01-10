package com.example.joutiaapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.joutiaapp.Adapter.CustomGridAdapter;
import com.example.joutiaapp.Adapter.CustomGridAdapterView;
import com.example.joutiaapp.Adapter.CustomListStatusAdapter;
import com.example.joutiaapp.Models.Status;
import com.example.joutiaapp.Models.product;
import com.example.joutiaapp.R;
import com.example.joutiaapp.Vendeur.DetailsProductAtivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProduitVendeurFragment extends Fragment {

    List<Status> liststate = new ArrayList<>();
    RecyclerView recyclerView;
    private CustomListStatusAdapter adapter;

    DatabaseReference allproduct;
    List<product> allproductarray = new ArrayList<>();
    List<product> allproductarrayfiltred = new ArrayList<>();
    ArrayList<ArrayList<String>> arrayList = new ArrayList<>();

    String nomMagasin;

    public GridView gridView;

    TextView noproduit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_produit_vendeur, container, false);

        findView(view);
        return view;
    }

    public String getAppLanguage() {
        Configuration config = getResources().getConfiguration();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return config.getLocales().get(0).getLanguage(); // Pour Android 7.0+ (API 24+)
        } else {
            return config.locale.getLanguage(); // Pour Android plus ancien
        }
    }

    private void findView(View view) {

        String appLanguage = getAppLanguage();

        if(appLanguage.equals("ar")){

            Status status0 = new Status("0", "في الانتظار");
            Status status1 = new Status("1", "صالح");
            Status status2 = new Status("2","مرفوض");

            liststate.add(status0);
            liststate.add(status1);
            liststate.add(status2);

        }else {

            Status status0 = new Status("0", "En attente");
            Status status1 = new Status("1", "Validé");
            Status status2 = new Status("2", "Rejeté");

            liststate.add(status0);
            liststate.add(status1);
            liststate.add(status2);

        }

        recyclerView = view.findViewById(R.id.recycler_view);
        initialiserecycler();

        noproduit = view.findViewById(R.id.noproduit);

        allproduct = FirebaseDatabase.getInstance().getReference().child("product");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        nomMagasin = sharedPreferences.getString("NomUser", "default_username");
        gridView = view.findViewById(R.id.gridView);
        getallcommandeProduct("0");

    }

    private void initialiserecycler() {
        adapter = new CustomListStatusAdapter(getContext(), liststate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnDataChangeListener(new CustomListStatusAdapter.OnDataChangeListener() {
            @Override
            public void showDetail(Object object) {
                getallcommandeProduct(object.toString());
            }
        });
    }

    private void getallcommandeProduct(String state) {
        allproductarray.clear();
        allproductarrayfiltred.clear();
        allproduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allproductarray.add(postSnapshot.getValue(product.class));
                }
                for (int i = 0; i < allproductarray.size(); i++) {
                    if(allproductarray.get(i).NomMagasin.equals(nomMagasin) && allproductarray.get(i).Approuved.equals(true)
                            && allproductarray.get(i).State.equals(state)){
                        allproductarrayfiltred.add(allproductarray.get(i));
                        arrayList.add(allproductarray.get(i).imageArrayList);
                    }
                }

                if(allproductarrayfiltred.size()==0){
                    noproduit.setVisibility(View.VISIBLE);
                }else{
                    noproduit.setVisibility(View.GONE);
                }

                initialiserecyclerdfilter(allproductarrayfiltred,arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initialiserecyclerdfilter(List<product> allproductarrayfiltred, ArrayList<ArrayList<String>> arrayList) {
        CustomGridAdapterView adapter = new CustomGridAdapterView(getContext(), allproductarrayfiltred,arrayList);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnDataChangeListener(new CustomGridAdapterView.OnDataChangeListener() {
            @Override
            public void showDetail(Object object) {
                Intent i =new Intent(getContext(), DetailsProductAtivity.class);
                i.putExtra("Nom",object.toString());
                startActivity(i);
            }
        });
    }


}