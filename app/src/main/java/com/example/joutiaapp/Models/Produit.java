package com.example.joutiaapp.Models;

import java.util.ArrayList;

public class Produit {

    public ArrayList<String> photoproduit;
    public String nomprod;
    public String description;
    public String prix;
    public String qte;
    public String delai;
    public String categories;
    public String etat;
    public String nommagasin;

    public Produit(ArrayList<String> photoproduit, String nomprod, String description, String prix, String qte, String delai, String categories, String etat, String nommagasin) {
        this.photoproduit = photoproduit;
        this.nomprod = nomprod;
        this.description = description;
        this.prix = prix;
        this.qte = qte;
        this.delai = delai;
        this.categories = categories;
        this.etat = etat;
        this.nommagasin = nommagasin;
    }

    public Produit() {
    }
}
