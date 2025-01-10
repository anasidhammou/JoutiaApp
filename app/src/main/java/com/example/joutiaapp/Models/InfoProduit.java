package com.example.joutiaapp.Models;

public class InfoProduit {

    private String NomProduit;
    private String PhotoProduit;
    private String EtatProduit;
    private String PrixProduit;
    private String CategorieProduit;
    private String NomMagasin;
    private String TelMagasin;

    public InfoProduit(String nomProduit, String photoProduit, String etatProduit, String prixProduit, String categorieProduit, String nomMagasin, String telMagasin) {
        NomProduit = nomProduit;
        PhotoProduit = photoProduit;
        EtatProduit = etatProduit;
        PrixProduit = prixProduit;
        CategorieProduit = categorieProduit;
        NomMagasin = nomMagasin;
        TelMagasin = telMagasin;
    }


    public String getNomProduit() {
        return NomProduit;
    }

    public void setNomProduit(String nomProduit) {
        NomProduit = nomProduit;
    }

    public String getPhotoProduit() {
        return PhotoProduit;
    }

    public void setPhotoProduit(String photoProduit) {
        PhotoProduit = photoProduit;
    }

    public String getEtatProduit() {
        return EtatProduit;
    }

    public void setEtatProduit(String etatProduit) {
        EtatProduit = etatProduit;
    }

    public String getPrixProduit() {
        return PrixProduit;
    }

    public void setPrixProduit(String prixProduit) {
        PrixProduit = prixProduit;
    }

    public String getCategorieProduit() {
        return CategorieProduit;
    }

    public void setCategorieProduit(String categorieProduit) {
        CategorieProduit = categorieProduit;
    }

    public String getNomMagasin() {
        return NomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        NomMagasin = nomMagasin;
    }

    public String getTelMagasin() {
        return TelMagasin;
    }

    public void setTelMagasin(String telMagasin) {
        TelMagasin = telMagasin;
    }
}
