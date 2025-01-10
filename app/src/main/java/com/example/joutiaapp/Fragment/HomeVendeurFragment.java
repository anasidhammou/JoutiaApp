package com.example.joutiaapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.joutiaapp.R;
import com.example.joutiaapp.Vendeur.AddNewProductActivity;
import com.example.joutiaapp.Vendeur.VendeurPrincipalActivity;


public class HomeVendeurFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home_vendeur, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        ImageView addnew = view.findViewById(R.id.goAdd);
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddNewProductActivity.class);
                startActivity(i);
            }
        });
    }
}