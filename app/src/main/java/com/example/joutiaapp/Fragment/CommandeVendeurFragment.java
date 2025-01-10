package com.example.joutiaapp.Fragment;

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

        Status status1 = new Status("0", getString(R.string.en_cours));
        Status status2 = new Status("1", getString(R.string.livr_e));
        Status status3 = new Status("2", getString(R.string.refus_e));
        Status status4 = new Status("3", getString(R.string.annul_e));

        liststate.add(status1);
        liststate.add(status2);
        liststate.add(status3);
        liststate.add(status4);

        recyclerView = view.findViewById(R.id.recycler_view);
        initialiserecycler();

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