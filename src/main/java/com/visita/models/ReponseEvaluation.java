package com.visita.models;

public class ReponseEvaluation {
    private int id;
    private int reponseId;
    private int reclamationId;
    private int evaluation;

    // Constructeur
    public ReponseEvaluation(int id, int reponseId, int reclamationId, int evaluation) {
        this.id = id;
        this.reponseId = reponseId;
        this.reclamationId = reclamationId;
        this.evaluation = evaluation;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReponseId() {
        return reponseId;
    }

    public void setReponseId(int reponseId) {
        this.reponseId = reponseId;
    }

    public int getReclamationId() {
        return reclamationId;
    }

    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }
}

