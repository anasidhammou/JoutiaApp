package com.example.joutiaapp.Vendeur;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.joutiaapp.Fragment.CategoriesFragment;
import com.example.joutiaapp.Fragment.CommandeVendeurFragment;
import com.example.joutiaapp.Fragment.HelpFragment;
import com.example.joutiaapp.Fragment.HomeFragment;
import com.example.joutiaapp.Fragment.HomeVendeurFragment;
import com.example.joutiaapp.Fragment.ProduitVendeurFragment;
import com.example.joutiaapp.Fragment.ProfileFragment;
import com.example.joutiaapp.Fragment.SettingsVendeurFragment;
import com.example.joutiaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainVendeurActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_vendeur);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new HomeVendeurFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();
        if(id == R.id.homev){
            fragment = new HomeVendeurFragment();
        }else if(id == R.id.produitsv){
            fragment = new ProduitVendeurFragment();
        }else if(id == R.id.settingsv){
            fragment = new SettingsVendeurFragment();
        }
        if (fragment != null) {
            loadFragment(fragment);
        }
        return true;
    }

}