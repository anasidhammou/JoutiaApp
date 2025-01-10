package com.example.joutiaapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joutiaapp.R;
import com.example.joutiaapp.Utils.ZoomableImageView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

    private final Context context;
    private final ArrayList<Bitmap> pdfPages;

    public PdfAdapter(Context context, ArrayList<Bitmap> pdfPages) {
        this.context = context;
        this.pdfPages = pdfPages;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pdf_page, parent, false);
        return new PdfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        holder.pdfPageImage.setImageBitmap(pdfPages.get(position));
    }

    @Override
    public int getItemCount() {
        return pdfPages.size();
    }

    static class PdfViewHolder extends RecyclerView.ViewHolder {
        ZoomableImageView pdfPageImage;

        PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfPageImage = itemView.findViewById(R.id.pdfPageImage);
        }
    }
}

