package com.visita.models;
import java.sql.Time;
import java.util.Date;

public class Rendezvous
{
    private int Id;
    private  int medecin_id;
    private  String fullname ;
    private  int tel ;
    private  Date date ;
    private  Time time;

    private  String note ;
    private  boolean etat ;
    private  String email ;
    private  int rapport_id ;
    private String medecin_nom;

    public Rendezvous() {}

    public Rendezvous(int id, int medecin_id, String fullname, int tel, Date date, Time time, String note, boolean etat, String email, int rapport_id, String medecin_nom) {
        Id = id;
        this.medecin_id = medecin_id;
        this.fullname = fullname;
        this.tel = tel;
        this.date = date;
        this.time = time;
        this.note = note;
        this.etat = etat;
        this.email = email;
        this.rapport_id = rapport_id;
        this.medecin_nom = medecin_nom;
    }

    public Rendezvous(int id, int medecin, String fullname, int tel, Date date, Time time, String note, boolean etat, String email, int rapport_id) {
        this.Id = id;
        this.medecin_id = medecin; // Supposons que la classe Medecin a une méthode getId() pour récupérer l'ID du médecin
        this.fullname = fullname;
        this.tel = tel;
        this.date = date;
        this.time = time;
        this.note = note;
        this.etat = etat;
        this.email = email;
        this.rapport_id = rapport_id;
    }

    public Rendezvous(int medecin_id, String fullname, int tel, Date date, Time time, String note, boolean etat, String email, int rapport_id) {
        this.medecin_id = medecin_id;
        this.fullname = fullname;
        this.tel = tel;
        this.date = date;
        this.time = time;
        this.note = note;
        this.etat = etat;
        this.email = email;
        this.rapport_id = rapport_id;
    }
    public Rendezvous(int medecin_id, String fullname, Date date, Time time, int tel, boolean etat, String email, int rapport_id) {

        this.medecin_id=medecin_id;
        this.fullname=fullname;
        this.date=date;
        this.time=time;
        this.tel=tel;
        this.etat = etat;
        this.email = email;
        this.rapport_id = rapport_id;
    }


    public Rendezvous(int medecin_id, String fullname, int tel, Date date, Time time, String note, String email) {
        this.medecin_id = medecin_id;
        this.fullname = fullname;
        this.tel = tel;
        this.date = date;
        this.time = time;
        this.note = note;
        this.email = email;
    }

    public Rendezvous(int id, int medecin_id, String fullname, int tel, Date date, Time time, String note, String email) {
        this.Id = id;
        this.medecin_id = medecin_id;
        this.fullname = fullname;
        this.tel = tel;
        this.date = date;
        this.time = time;
        this.note = note;
        this.email = email;
    }

    public int getId() {
        return Id;
    }


    public  int getMedecin_id() {
        return medecin_id;
    }

    public  String getFullname() {
        return fullname;
    }

    public  int getTel() {
        return tel;
    }

    public  Date getDate() {
        return date;
    }

    public  Time getTime() {
        return time;
    }

    public  String getNote() {
        return note;
    }

    public  boolean isEtat() {
        return etat;
    }

    public  String getEmail() {
        return email;
    }

    public  int getRapport_id() {
        return rapport_id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setMedecin_id(int medecin_id) {
        this.medecin_id = medecin_id;
    }

    public void setFullname(String fullname) {
        if (isFullNameValid(fullname)) {
            this.fullname = fullname;
        } else {
            System.out.println("Le fullname ne peut contenir que des lettres et des espaces.");
        }
    }
    private boolean isFullNameValid(String fullname) {
        String regex = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$";
        return fullname.matches(regex);
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRapport_id(int rapport_id) {
        this.rapport_id = rapport_id;
    }

    public String getMedecin_nom() {
        return medecin_nom;
    }

    public void setMedecin_nom(String medecin_nom) {
        this.medecin_nom = medecin_nom;
    }

    @Override
    public String toString() {
        return "Rendezvous{" +
                "id=" + Id +
                ", medecin_id=" + medecin_id +
                ", fullname='" + fullname + '\'' +
                ", tel=" + tel +
                ", date=" + date +
                ", time=" + time +
                ", note='" + note + '\'' +
                ", etat=" + etat +
                ", email='" + email + '\'' +
                ", rapport_id=" + rapport_id +
                '}';
    }
}
