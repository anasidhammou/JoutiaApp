package com.example.joutiaapp.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joutiaapp.Adapter.CustomListStatusAdapter;
import com.example.joutiaapp.Models.Status;
import com.example.joutiaapp.R;

import java.util.ArrayList;
import java.util.List;


public class CommandeVendeurFragment extends Fragment {

    List<Status> liststate = new ArrayList<>();
    RecyclerView recyclerView;
    private CustomListStatusAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_commande_vendeur, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {

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

        recyclerView = view.findViewById(R.id.recycler_view);
        initialiserecycler();

    }

    public String getAppLanguage() {
        Configuration config = getResources().getConfiguration();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return config.getLocales().get(0).getLanguage(); // Pour Android 7.0+ (API 24+)
        } else {
            return config.locale.getLanguage(); // Pour Android plus ancien
        }
    }


    private void initialiserecycler() {
        adapter = new CustomListStatusAdapter(getContext(), liststate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnDataChangeListener(new CustomListStatusAdapter.OnDataChangeListener() {
            @Override
            public void showDetail(Object object) {

            }
        });
    }

}