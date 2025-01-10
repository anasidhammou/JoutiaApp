package com.example.joutiaapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joutiaapp.Models.Commande;
import com.example.joutiaapp.Models.PanierUser;
import com.example.joutiaapp.R;

import java.util.ArrayList;

public class CustomCommandeAdapter extends RecyclerView.Adapter<CustomCommandeAdapter.ViewHolder>{


    private final ArrayList<Commande> dataList;

    private final Context context;


    public CustomCommandeAdapter(Context context, ArrayList<Commande> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new CustomCommandeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.NomProd.setText(dataList.get(position).panierUsers.get(position).NomProduit);
        holder.PrixProd.setText("Prix : "+dataList.get(position).panierUsers.get(position).Prix+" Dhs");
        Glide.with(context)
                .load(dataList.get(position).panierUsers.get(position).ImageBase) // Remplacez par la méthode pour obtenir l'URL de l'image
                .placeholder(R.drawable.default_img) // Image de chargement par défaut
                .error(R.drawable.default_img) // Image en cas d'erreur
                .into(holder.imageProd);
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProd;
        TextView NomProd;
        TextView PrixProd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProd = itemView.findViewById(R.id.img_prodpan);
            NomProd = itemView.findViewById(R.id.nom_prodPan);
            PrixProd = itemView.findViewById(R.id.prix_prodPan);
        }
    }
}
