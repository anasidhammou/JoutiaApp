package com.example.joutiaapp.Models;

public class Notifications {

    public int Id;
    public String titre;
    public String description;
    public String NomVendeur;
    public String Date;
    public Boolean vu;

    public Notifications() {
    }

    public Notifications(int id, String titre, String description, String nomVendeur, String date, Boolean vu) {
        Id = id;
        this.titre = titre;
        this.description = description;
        NomVendeur = nomVendeur;
        Date = date;
        this.vu = vu;
    }
}