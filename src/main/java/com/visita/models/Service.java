package com.visita.models;
import java.sql.Date;

public class Service {
    private int id;
    private String nom;
    private String description;
    private String image;
    private Date dateCreation; // Changement de LocalDate à Date
    private boolean active;
    private int category_id;
    private String imageName;
    private String category_nom;
    public Service(int id, String nom, String description, Date dateCreation, String image ,int category_id, boolean active ) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateCreation = dateCreation;
        this.image = image;
        this.active = active;
        this.category_id = category_id;

    }



    public Service(String nom, String description, Date dateCreation, String image, int  category_id, boolean active ) {
        this.nom = nom;
        this.description = description;
        this.dateCreation = dateCreation;
        this.image = image;
        this.active = active;
        this.category_id = category_id;
    }

    public Service(int id, String nom, String description, Date dateCreation, boolean active, int category_id, String category_nom) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateCreation = dateCreation;
        this.active = active;
        this.category_id = category_id;
        this.category_nom = category_nom;
    }

    public Service(String text, String addServiceDescriptionText, java.sql.Date dateCreation, String imagePath, Category value, boolean selected) {
        this.active = false; // Par défaut, active est faux
    }

    public Service(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDateCreation() {
        return  this.dateCreation;
    }

    public void setDateCreation(Date dateCreation) {

        this.dateCreation = dateCreation;

    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {

        this.nom = nom;

    }

    public Service(int id, String nom, String description, String image, Date dateCreation, boolean active, int category_id, String imageName, String category_nom) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.dateCreation = dateCreation;
        this.active = active;
        this.category_id = category_id;
        this.imageName = imageName;
        this.category_nom = category_nom;
    }

    public Service(String nom, String description, String image, Date dateCreation, boolean active, int category_id, String imageName, String category_nom) {
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.dateCreation = dateCreation;
        this.active = active;
        this.category_id = category_id;
        this.imageName = imageName;
        this.category_nom = category_nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

        this.description = description;

    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public boolean isActive() {
        return active;
    }

    public String getCategory_nom() {
        return category_nom;
    }

    public void setCategory_nom(String category_nom) {
        this.category_nom = category_nom;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", dateCreation=" + dateCreation +
                ", image='" + image + '\'' +
                ", category_id='" + category_id + '\'' +
                ", active=" + active +

                '}';
    }
}