package com.visita.models;

import java.time.LocalDate;

public class Category {
    private  int id;
    private  String nom;
    private String description;
    private String icon;


    public Category(String nom, String description, String icon) {
        this.nom = nom;
        this.description = description;
        this.icon = icon;
    }

    public Category(int id, String nom, String description, String icon) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.icon = icon;
    }

    public Category(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }
    // Ajoutez une méthode pour obtenir le style CSS complet de l'icône Font Awesome

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", nom='" + nom +
                ", description='" + description +
                ", icon='" + icon +
                '}';
    }

}
