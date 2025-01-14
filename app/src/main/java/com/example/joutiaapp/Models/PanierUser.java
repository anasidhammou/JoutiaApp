package com.example.joutiaapp.Models;

public class PanierUser {

    public String ImageBase;
    public String NomProduit;
    public Object Prix;

    public String type;

    public String nomMagasin;


    public PanierUser() {
    }

    public PanierUser(String imageBase, String nomProduit, Object prix, String type, String nomMagasin) {
        ImageBase = imageBase;
        NomProduit = nomProduit;
        Prix = prix;
        this.type = type;
        this.nomMagasin = nomMagasin;
    }
}

