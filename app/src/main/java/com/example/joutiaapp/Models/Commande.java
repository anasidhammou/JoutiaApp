package com.example.joutiaapp.Models;

import java.util.ArrayList;

public class Commande {

    public  String NomClient;
    public String Phone;

    public ArrayList<PanierUser> panierUsers;
    public String Etat;

    public String prixcomplet;



    public Commande() {
    }


    public Commande(String nomClient, String phone, ArrayList<PanierUser> panierUsers, String etat, String prixcomplet) {
        NomClient = nomClient;
        Phone = phone;
        this.panierUsers = panierUsers;
        Etat = etat;
        this.prixcomplet = prixcomplet;
    }
}
