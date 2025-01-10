package com.example.joutiaapp.Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joutiaapp.Adapter.CustomGridAdapter;
import com.example.joutiaapp.Adapter.CustomGridAdapterView;
import com.example.joutiaapp.MainActivity;
import com.example.joutiaapp.Models.product;
import com.example.joutiaapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ModeActivity extends AppCompatActivity {

    DatabaseReference allproduct;
    List<product> allproductarray = new ArrayList<>();
    List<product> allproductarrayfiltred = new ArrayList<>();

    public GridView gridView;
    ArrayList<ArrayList<String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mode);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        allproduct = FirebaseDatabase.getInstance().getReference().child("product");
        gridView = findViewById(R.id.gridView);
        getallcommandeProduct();
    }

    private void getallcommandeProduct() {
        allproductarray.clear();
        allproductarrayfiltred.clear();
        allproduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allproductarray.add(postSnapshot.getValue(product.class));
                }
                for (int i = 0; i < allproductarray.size(); i++) {
                    if(allproductarray.get(i).Catégorie.equals("Mode & Accessoires")&& allproductarray.get(i).Approuved.equals(true)&& allproductarray.get(i).State.equals("1")){
                        allproductarrayfiltred.add(allproductarray.get(i));
                        arrayList.add(allproductarray.get(i).imageArrayList);
                    }
                }
                initialiserecyclerdfilter(allproductarrayfiltred,arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initialiserecyclerdfilter(List<product> allproductarrayfiltred,ArrayList<ArrayList<String>> arrayList) {
        CustomGridAdapterView adapter = new CustomGridAdapterView(this, allproductarrayfiltred,arrayList);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnDataChangeListener(new CustomGridAdapterView.OnDataChangeListener() {
            @Override
            public void showDetail(Object object) {
                Intent i =new Intent(ModeActivity.this, DetailsTypeProductActivity.class);
                i.putExtra("Nom",object.toString());
                i.putExtra("type", "Mode & Accessoires");
                startActivity(i);
            }
        });
    }

    public void goBack(View view) {
        Intent i =new Intent(this, MainActivity.class);
        startActivity(i);
    }

}