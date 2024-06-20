package com.visita.models;


public class Rapport
{
private int id ;
private int rendzvous_id;
private String type ;
private String note ;
    private String rendezvous_nom ;

    public Rapport(int id, int rendzvous_id, String type, String note, String rendezvous_nom) {
        this.id = id;
        this.rendzvous_id = rendzvous_id;
        this.type = type;
        this.note = note;
        this.rendezvous_nom = rendezvous_nom;
    }

    public String getRendezvous_nom() {
        return rendezvous_nom;
    }

    public void setRendezvous_nom(String rendezvous_nom) {
        this.rendezvous_nom = rendezvous_nom;
    }


    public Rapport(int id, int rendzvous_id, String type, String note) {
        this.id = id;
        this.rendzvous_id = rendzvous_id;
        this.type = type;
        this.note = note;
    }

    public Rapport(int rendzvous_id, String type, String note) {
        this.rendzvous_id = rendzvous_id;
        this.type = type;
        this.note = note;
    }

    public Rapport(String type, String note) {
        this.type = type;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRendzvous_id() {
        return rendzvous_id;
    }

    public void setRendzvous_id(int rendzvous_id) {
        this.rendzvous_id = rendzvous_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
