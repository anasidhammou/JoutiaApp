package com.example.joutiaapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joutiaapp.Models.PanierUser;
import com.example.joutiaapp.R;

import java.util.ArrayList;

public class AdapterPanier extends RecyclerView.Adapter<AdapterPanier.ViewHolder> {
    private final ArrayList<PanierUser> dataList;

    private final Context context;

    // Constructor
    public AdapterPanier(ArrayList<PanierUser> dataList,Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.NomProd.setText(dataList.get(position).NomProduit);
        holder.PrixProd.setText("Prix : "+dataList.get(position).Prix+" Dhs");
        holder.NomMag.setText("Magasin : "+dataList.get(position).nomMagasin);
        Glide.with(context)
                .load(dataList.get(position).ImageBase) // Remplacez par la méthode pour obtenir l'URL de l'image
                .placeholder(R.drawable.default_img) // Image de chargement par défaut
                .error(R.drawable.default_img) // Image en cas d'erreur
                .into(holder.imageProd);

        holder.TrashArticle.setOnClickListener(v -> {
            if (position >= 0 && position < dataList.size()) {
                onDataChangeListener.showDetail(dataList.get(position).NomProduit, position);
            } else {
                Log.e("AdapterPanier", "Invalid position: " + position);
            }
        });
    }


    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // ViewHolder class for caching views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProd;
        TextView NomProd;
        TextView PrixProd;
        TextView NomMag;



        ImageView TrashArticle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProd = itemView.findViewById(R.id.img_prodpan);
            NomProd = itemView.findViewById(R.id.nom_prodPan);
            PrixProd = itemView.findViewById(R.id.prix_prodPan);
            TrashArticle = itemView.findViewById(R.id.imageTrash);
            NomMag = itemView.findViewById(R.id.nomM_prodPan);
        }
    }

    OnDataChangeListener onDataChangeListener;

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }


    public interface OnDataChangeListener {

        void showDetail(Object object, int position);
    }
}

