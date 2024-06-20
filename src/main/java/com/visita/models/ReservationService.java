package com.visita.models;

public class ReservationService {
    private  int id;
    private  String nom;
    private String email;
    private int idservice_id;

    private String service_nom;

    public ReservationService(int id, String nom, String email, int idservice_id, String service_nom) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.idservice_id = idservice_id;
        this.service_nom = service_nom;
    }

    public ReservationService( int idservice_id ,String nom, String email) {
        this.idservice_id = idservice_id;
        this.nom = nom;
        this.email = email;


    }

    public ReservationService(int id, String nom, String email, String service_nom) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.service_nom = service_nom;
    }

    public ReservationService(int id, String nom, String email, int idservice_id) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.idservice_id = idservice_id;
    }

    public ReservationService(int id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdservice_id() {
        return idservice_id;
    }

    public void setIdservice_id(int idservice_id) {
        this.idservice_id = idservice_id;
    }

    public String getService_nom() {
        return service_nom;
    }

    public void setService_nom(String service_nom) {
        this.service_nom = service_nom;
    }

    @Override
    public String toString() {
        return "ReservationService{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", idservice_id=" + idservice_id +
                '}';
    }
    public void initData(Service selectedService) {
        // Initialise les données de votre service ici
        // Par exemple, vous pouvez initialiser les champs de votre classe avec les données du service sélectionné

        // Par exemple :
        this.idservice_id = selectedService.getId();
        this.nom = selectedService.getNom();
        this.email = ""; // Vous pouvez initialiser d'autres champs ici selon vos besoins
        this.service_nom = selectedService.getNom(); // Ou tout autre champ que vous devez initialiser
    }

}
