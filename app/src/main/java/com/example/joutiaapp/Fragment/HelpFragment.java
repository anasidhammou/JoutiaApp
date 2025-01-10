package com.example.joutiaapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.joutiaapp.AboutActivity;
import com.example.joutiaapp.CGUActivity;
import com.example.joutiaapp.Choix_langue_Activity;
import com.example.joutiaapp.LangueActivity;
import com.example.joutiaapp.NotificationsActivity;
import com.example.joutiaapp.PolitiqueActivity;
import com.example.joutiaapp.R;


public class HelpFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view= inflater.inflate(R.layout.fragment_help, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        LinearLayout cgu = view.findViewById(R.id.cgu);
        LinearLayout about = view.findViewById(R.id.about);
        LinearLayout politique = view.findViewById(R.id.politique);
        LinearLayout langueLinear = view.findViewById(R.id.linearLangue);
        LinearLayout notifLinear = view.findViewById(R.id.linearNotif);

        cgu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i =  new Intent(getContext(), CGUActivity.class);
              startActivity(i);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(getContext(), AboutActivity.class);
                startActivity(i);
            }
        });

        politique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(getContext(), PolitiqueActivity.class);
                startActivity(i);
            }
        });

        langueLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(getContext(), LangueActivity.class);
                startActivity(i);
            }
        });

        notifLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(getContext(), NotificationsActivity.class);
                startActivity(i);
            }
        });
    }
}