package com.example.joutiaapp.Models;

public class PanierUser {

    public String ImageBase;
    public String NomProduit;
    public Object Prix;

    public String type;


    public PanierUser() {
    }


    public PanierUser(String imageBase, String nomProduit, Object prix, String type) {
        ImageBase = imageBase;
        NomProduit = nomProduit;
        Prix = prix;
        this.type = type;
    }
}
