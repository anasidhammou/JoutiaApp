package com.example.joutiaapp.Models;

import java.util.ArrayList;

public class Vendeur {

    public String mail;
    public String phone;
    public String pass;
    public String nommagasin;
    public String adressemagasin;
    public String villemagasin;
    public String catmagasin;
    public String rib;
    public String type;

    public double Langetude;
    public double Latitude;

    public ArrayList<String> ArrayImage = new ArrayList<>();

    public Vendeur() {
    }


    public Vendeur(String mail, String phone, String pass, String nommagasin, String adressemagasin, String villemagasin, String catmagasin, String rib, String type, double langetude, double latitude, ArrayList<String> arrayImage) {
        this.mail = mail;
        this.phone = phone;
        this.pass = pass;
        this.nommagasin = nommagasin;
        this.adressemagasin = adressemagasin;
        this.villemagasin = villemagasin;
        this.catmagasin = catmagasin;
        this.rib = rib;
        this.type = type;
        Langetude = langetude;
        Latitude = latitude;
        ArrayImage = arrayImage;
    }
}
