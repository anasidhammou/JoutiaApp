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

import androidx.viewpager2.widget.ViewPager2;

import com.example.joutiaapp.Models.product;
import com.example.joutiaapp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomGridAdapterView extends BaseAdapter {

    private ArrayList<ArrayList<String>> arrayList = new ArrayList<>();
    private Context context;
    private List<product> allproductarray = new ArrayList<>();

    private ViewPagerAdapter adapter;

    public CustomGridAdapterView(Context connectedObjectActivity, List<product> allproductarrayfiltred, ArrayList<ArrayList<String>> arrayList) {
        this.context = connectedObjectActivity;
        this.allproductarray = allproductarrayfiltred;
        this.arrayList = arrayList;

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
        CustomGridAdapterView.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_view, parent, false);
            holder = new CustomGridAdapterView.ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image);
            holder.name = convertView.findViewById(R.id.name);
            holder.price = convertView.findViewById(R.id.price);
            holder.itemViewes=convertView;
            holder.BuyBtn = convertView.findViewById(R.id.btn_buy);
            holder.viewPager2 = convertView.findViewById(R.id.viewPager);
            convertView.setTag(holder);
        } else {
            holder = (CustomGridAdapterView.ViewHolder) convertView.getTag();
        }

        product currentProduct = allproductarray.get(position);
        holder.name.setText(currentProduct.Nom.toString());
        holder.price.setText(currentProduct.Montant_TTC + " Dhs");

        List<String> productImages = arrayList.get(position);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(context, productImages);
        holder.viewPager2.setAdapter(pagerAdapter);

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

        ViewPager2 viewPager2;
        private View itemViewes;
        private Button BuyBtn;
    }

    CustomGridAdapterView.OnDataChangeListener onDataChangeListener;

    public void setOnDataChangeListener(CustomGridAdapterView.OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    public interface OnDataChangeListener {

        void showDetail(Object object);
    }

}
