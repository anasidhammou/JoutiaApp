package com.example.joutiaapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.joutiaapp.Adapter.ImagePagerAdapter;
import com.example.joutiaapp.Categories.DetailsProductActivity;
import com.example.joutiaapp.Models.PanierUser;
import com.example.joutiaapp.PanierActivity;
import com.example.joutiaapp.Products.BabyActivity;
import com.example.joutiaapp.Products.BeautyActivity;
import com.example.joutiaapp.Products.ConnectedObjectActivity;
import com.example.joutiaapp.Products.DetailsTypeProductActivity;
import com.example.joutiaapp.Products.ElectrikActivity;
import com.example.joutiaapp.Products.HouseActivity;
import com.example.joutiaapp.Products.ModeActivity;
import com.example.joutiaapp.Products.SportsActivity;
import com.example.joutiaapp.R;
import com.example.joutiaapp.Utils.SharedPreferencesUtils;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;

    ViewPager2 viewPager;

    ImageView image1,image2,image3,image4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {


        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        LinearLayout Line_connected = view.findViewById(R.id.ic_connected);
        LinearLayout Line_beauty = view.findViewById(R.id.ic_beauty);
        LinearLayout Line_sports = view.findViewById(R.id.ic_sports);
        LinearLayout Line_baby = view.findViewById(R.id.ic_baby);
        LinearLayout Line_accessoires = view.findViewById(R.id.ic_access);
        LinearLayout Line_decor = view.findViewById(R.id.ic_decore);
        LinearLayout Line_eletromenager = view.findViewById(R.id.ic_electromenager);
        ImageView  panier = view.findViewById(R.id.panierico);





        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PanierActivity.class);
                startActivity(i);
            }
        });


        Line_connected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ConnectedObjectActivity.class);
                startActivity(i);
            }
        });

        Line_beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), BeautyActivity.class);
                startActivity(i);
            }
        });

        Line_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SportsActivity.class);
                startActivity(i);
            }
        });

        Line_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), BabyActivity.class);
                startActivity(i);
            }
        });

        Line_accessoires.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ModeActivity.class);
                startActivity(i);
            }
        });

        Line_decor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), HouseActivity.class);
                startActivity(i);
            }
        });

        Line_eletromenager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ElectrikActivity.class);
                startActivity(i);
            }
        });


         viewPager = view.findViewById(R.id.viewPager);

        // Liste des images du dossier drawable
        int[] images = {
                R.drawable.image_home_dash,
                R.drawable.apple_logo,
                R.drawable.ic_choix_week,
                R.drawable.infinix_logo
        };

        // Configuration de l'adapter
        ImagePagerAdapter adapter = new ImagePagerAdapter(getContext(), images);
        viewPager.setAdapter(adapter);

        setupAutoScroll(images.length);

        CheckShared(view);
    }

    public void urlToBase64Async(String imageUrl, Callback callback) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                return urlToBase64(params[0]);
            }

            @Override
            protected void onPostExecute(String base64String) {
                callback.onResult(base64String);
            }
        }.execute(imageUrl);
    }

    private String urlToBase64(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    interface Callback {
        void onResult(String base64String);
    }





    private void setupAutoScroll(int itemCount) {
        autoScrollHandler = new Handler(Looper.getMainLooper());
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = (currentItem + 1) % itemCount;
                viewPager.setCurrentItem(nextItem, true); // Smooth scroll to the next item
                autoScrollHandler.postDelayed(this, 3000); // Repeat every 3 seconds
            }
        };

        // Start auto-scrolling
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Stop auto-scrolling to prevent memory leaks
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    private void CheckShared(View view) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("mes_preferences", MODE_PRIVATE);
        int valueInt = sharedPreferences.getInt("valueInt", 0);
        if(valueInt == 0){
            launchSequence(view);
        }
    }

    public void launchSequence(View view) {

        TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(
                        TapTarget.forView(view.findViewById(R.id.panierico), getString(R.string.icone_panier),getString(R.string.cette_icone_vous_permet_d_acc_der_votre_panier))
                                .outerCircleColor(R.color.color_app)
                                .targetCircleColor(R.color.white)
                                .titleTextColor(android.R.color.black)
                                .descriptionTextColor(android.R.color.black)
                                .cancelable(false)
                );

        sequence.start();
        final int totalTargets = 1;
        final int[] currentStep = {0};
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                sequence.cancel();
            }

            @Override

            public void onSequenceStep(TapTarget lastTargetClicked, boolean targetClicked) {
                currentStep[0]++;
                if (currentStep[0] >= totalTargets) {
                    sequence.cancel();
                    WriteinShared();
                } else {
                    sequence.start();
                }
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                sequence.cancel();
            }
        });
    }

    private void WriteinShared() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("mes_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("valueInt",1);
        editor.apply();
    }


    public String imageAssetToBase64(String fileName, Context context) {
        try {
            // Ouvrir l'image depuis les assets
            InputStream inputStream = context.getAssets().open(fileName);

            // Lire l'image dans un ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();

            // Convertir le tableau de bytes en Base64
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Retourner null en cas d'erreur
    }

    public String drawableToBase64(Context context, int drawableId) {
        try {
            // Convert Drawable to Bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);

            // Compress the Bitmap to ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // Use PNG or JPEG

            // Convert ByteArrayOutputStream to a byte array
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            // Encode byte array to Base64 string
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null in case of an error
        }
    }
}