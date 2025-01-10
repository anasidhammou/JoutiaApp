package com.example.joutiaapp.Models;

public class User {

    public String mail;
    public String phone;
    public String pass;
    public String nomcomplet;
    public String adresselivraison;
    public String ville;
    public String categories;
    public String type;

    public double longe;
    public double lati;


    public User() {
    }

    public User(String mail, String phone, String pass, String nomcomplet, String adresselivraison, String ville, String categories, String type, double longe, double lati) {
        this.mail = mail;
        this.phone = phone;
        this.pass = pass;
        this.nomcomplet = nomcomplet;
        this.adresselivraison = adresselivraison;
        this.ville = ville;
        this.categories = categories;
        this.type = type;
        this.longe = longe;
        this.lati = lati;
    }
}
