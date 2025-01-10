package com.example.joutiaapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joutiaapp.Models.product;
import com.example.joutiaapp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomGridAdapter extends BaseAdapter {
    private  Context context;
    private List<product> allproductarray = new ArrayList<>();

        public CustomGridAdapter(Context connectedObjectActivity, List<product> allproductarrayfiltred) {
        this.context = connectedObjectActivity;
        this.allproductarray = allproductarrayfiltred;

    }


    @Override
    public int getCount() {
        return allproductarray.size();
    }

    @Override
    public Object getItem(int position) {
        return allproductarray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image);
            holder.name = convertView.findViewById(R.id.name);
            holder.price = convertView.findViewById(R.id.price);
            holder.itemViewes=convertView;
            holder.BuyBtn = convertView.findViewById(R.id.btn_buy);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText((CharSequence) allproductarray.get(position).Nom);
        holder.price.setText(allproductarray.get(position).Montant_TTC+" Dhs");

       /* if(allproductarray.get(position).type.equals("Base64")){
            Bitmap bitmap = base64ToBitmap(allproductarray.get(position).Image.toString());
            if (bitmap != null) {
                holder.imageView.setImageBitmap(bitmap);
            } else {
                holder.imageView.setImageResource(R.drawable.default_img);
            }
        }else {
            Glide.with(context)
                    .load(allproductarray.get(position).Image) // Remplacez par la méthode pour obtenir l'URL de l'image
                    .placeholder(R.drawable.default_img) // Image de chargement par défaut
                    .error(R.drawable.default_img) // Image en cas d'erreur
                    .into(holder.imageView);
        }*/


        holder.BuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataChangeListener.showDetail(allproductarray.get(position).Nom);
                notifyDataSetChanged();
            }
        });


        return convertView;
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

    // Classe ViewHolder pour améliorer les performances
    private static class ViewHolder {
        ImageView imageView;
        TextView name;
        TextView price;
        private View itemViewes;
        private Button BuyBtn;
    }

    OnDataChangeListener onDataChangeListener;

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }


    public interface OnDataChangeListener {

        void showDetail(Object object);
    }
}

