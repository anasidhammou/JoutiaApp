package com.example.joutiaapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {
    private final ArrayList<PanierUser> childList;
    private Context context;

    public ChildAdapter(ArrayList<PanierUser> childList, Context cnt) {
        this.childList = childList;
        this.context=cnt;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ChildViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        // Vérifiez que la liste "panierUsers" n'est pas vide
        if (!childList.isEmpty()) {
            holder.NomProd.setText(childList.get(position).NomProduit);
            holder.PrixProd.setText("Prix : " + childList.get(position).Prix + " Dhs");
            if(childList.get(position).type.equals("Base64")){
                Bitmap bitmap = base64ToBitmap(childList.get(position).ImageBase);
                if (bitmap != null) {
                    holder.imageProd.setImageBitmap(bitmap);
                } else {
                    holder.imageProd.setImageResource(R.drawable.default_img);
                }
            }else{
                Glide.with(context)
                        .load(childList.get(position).ImageBase) // Utilisez l'index correct
                        .placeholder(R.drawable.default_img)
                        .error(R.drawable.default_img)
                        .into(holder.imageProd);
            }

        } else {
            // Gérez le cas où "panierUsers" est vide
            holder.NomProd.setText("Aucun produit");
            holder.PrixProd.setText("");
            holder.imageProd.setImageResource(R.drawable.default_img);
        }
    }

    private Bitmap base64ToBitmap(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public int getItemCount() {
        return childList.size();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProd;
        TextView NomProd;
        TextView PrixProd;

        ImageView imageTrash;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProd = itemView.findViewById(R.id.img_prodpan);
            NomProd = itemView.findViewById(R.id.nom_prodPan);
            PrixProd = itemView.findViewById(R.id.prix_prodPan);
            imageTrash = itemView.findViewById(R.id.imageTrash);
            imageTrash.setVisibility(View.GONE);
        }
    }
}

