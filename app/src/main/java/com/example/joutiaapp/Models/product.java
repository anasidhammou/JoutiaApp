package com.example.joutiaapp.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class product implements Serializable {

    @SerializedName("Product ID")
    public Object Product_ID;


    public ArrayList<String> imageArrayList;

    @SerializedName("Nom")
    public Object Nom;

    @SerializedName("Référence")
    public Object Référence;

    @SerializedName("Catégorie")
    public Object Catégorie;

    @SerializedName("Montant_HT")
    public Object Montant_HT;

    @SerializedName("Montant_TTC")
    public Object Montant_TTC;

    @SerializedName("Quantité")
    public Object Quantité;

    @SerializedName("État")
    public Object État;

    @SerializedName("Position")
    public Object Position;

    @SerializedName("type")
    public Object type;

    @SerializedName("NomMagasin")
    public Object NomMagasin;

    @SerializedName("phone")
    public Object phone;

    @SerializedName("Description")
    public Object Description;

    @SerializedName("Approuved")
    public Object Approuved;

    @SerializedName("State")
    public String State;
    @SerializedName("Etat_prod")
    public Object Etat_prod;


    public product() {
    }

    public product(Object product_ID, ArrayList<String> imageArrayList, Object nom, Object référence, Object catégorie, Object montant_HT, Object montant_TTC, Object quantité, Object état, Object position, Object type, Object nomMagasin, Object phone, Object description, Object approuved, String state, Object etat_prod) {
        Product_ID = product_ID;
        this.imageArrayList = imageArrayList;
        Nom = nom;
        Référence = référence;
        Catégorie = catégorie;
        Montant_HT = montant_HT;
        Montant_TTC = montant_TTC;
        Quantité = quantité;
        État = état;
        Position = position;
        this.type = type;
        NomMagasin = nomMagasin;
        this.phone = phone;
        Description = description;
        Approuved = approuved;
        State = state;
        Etat_prod = etat_prod;
    }
}
