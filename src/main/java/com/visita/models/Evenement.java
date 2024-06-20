package com.visita.models;
import java.time.LocalDateTime;

public class Evenement {
    private int id;
    private String nom_evenement;
    private String image_evenement;
    private String lieu_evenement;
    private LocalDateTime date_debut;
    private LocalDateTime date_fin;
    private String type_evenement;
    private int nb_participants;



    private int category_id;
    public Evenement() {
    }
    public Evenement(int id) {
        this.id=id;
    }

    public Evenement(int id, String nom_evenement, String image_evenement, String lieu_evenement, LocalDateTime date_debut, LocalDateTime date_fin, String type_evenement, int nb_participants, int category_id) {
        this.id = id;
        this.nom_evenement = nom_evenement;
        this.image_evenement = image_evenement;
        this.lieu_evenement = lieu_evenement;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.type_evenement = type_evenement;
        this.nb_participants = nb_participants;
        this.category_id = category_id;
    }
    public Evenement(String nom_evenement, String image_evenement, String lieu_evenement, LocalDateTime date_debut, LocalDateTime date_fin, String type_evenement, int nb_participants, int category_id) {
        this.nom_evenement = nom_evenement;
        this.image_evenement = image_evenement;
        this.lieu_evenement = lieu_evenement;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.type_evenement = type_evenement;
        this.nb_participants = nb_participants;
        this.category_id = category_id;
    }



    public Evenement(int id, String nom_evenement, String image_evenement, LocalDateTime dateDebut, LocalDateTime dateFin, String text2, int nbParticipants, Integer value) {
        this.id = id;
        this.nom_evenement = nom_evenement;
        this.image_evenement = image_evenement;
        this.lieu_evenement = lieu_evenement;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.type_evenement = type_evenement;
        this.nb_participants = nb_participants;
        this.category_id = category_id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_evenement() {
        return nom_evenement;
    }

    public void setNom_evenement(String nom_evenement) {
        this.nom_evenement = nom_evenement;
    }

    public String getImage_evenement() {
        return image_evenement;
    }

    public void setImage_evenement(String image_evenement) {
        this.image_evenement = image_evenement;
    }

    public String getLieu_evenement() {
        return lieu_evenement;
    }

    public void setLieu_evenement(String lieu_evenement) {
        this.lieu_evenement = lieu_evenement;
    }

    public LocalDateTime getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(LocalDateTime date_debut) {
        this.date_debut = date_debut;
    }

    public LocalDateTime getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(LocalDateTime date_fin) {
        this.date_fin = date_fin;
    }

    public String getType_evenement() {
        return type_evenement;
    }

    public void setType_evenement(String type_evenement) {
        this.type_evenement = type_evenement;
    }

    public int getNb_participants() {
        return nb_participants;
    }

    public void setNb_participants(int nb_participants) {
        this.nb_participants = nb_participants;
    }


    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nom_evenement='" + nom_evenement + '\'' +
                ", image_evenement='" + image_evenement + '\'' +
                ", lieu_evenement='" + lieu_evenement + '\'' +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                ", type_evenement='" + type_evenement + '\'' +
                ", nb_participants=" + nb_participants +
                ", category_id=" + category_id +
                '}';
    }

}


