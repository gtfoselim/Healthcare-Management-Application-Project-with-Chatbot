package com.visita.models;

import java.time.LocalDateTime;

public class Participation {
    private int id;
    private LocalDateTime date_participation;
    private int evenement_id;
    private String description;
    private int medecin_id;

    public Participation() {
    }

    public Participation(int id, LocalDateTime date_participation, int evenement_id, String description, int medecin_id) {
        this.id = id;
        this.date_participation = date_participation;
        this.evenement_id = evenement_id;
        this.description = description;
        this.medecin_id = medecin_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate_participation() {
        return date_participation;
    }

    public void setDate_participation(LocalDateTime date_participation) {
        this.date_participation = date_participation;
    }

    public int getEvenement_id() {
        return evenement_id;
    }

    public void setEvenement_id(int evenement_id) {
        this.evenement_id = evenement_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMedecin_id() {
        return medecin_id;
    }

    public void setMedecin_id(int medecin_id) {
        this.medecin_id = medecin_id;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "id=" + id +
                ", date_participation=" + date_participation +
                ", evenement_id=" + evenement_id +
                ", description='" + description + '\'' +
                ", medecin_id=" + medecin_id +
                '}';
    }
}
