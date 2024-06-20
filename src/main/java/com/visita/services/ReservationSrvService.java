package com.visita.services;

import com.visita.models.Category;
import com.visita.models.ReservationService;
import com.visita.models.Service;
import com.visita.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationSrvService implements IReservationService<ReservationService> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(ReservationService resvationsr) {
        String req = "INSERT INTO `reservationservice`( `idserivce_id`, `nom`, `email`) VALUES (?, ?, ?);";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, resvationsr.getIdservice_id());
            pst.setString(2, resvationsr.getNom());
            pst.setString(3, resvationsr.getEmail());

            pst.executeUpdate();
            System.out.println("Service ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(ReservationService resvationsr) {
        String req = "DELETE FROM `reservationservice` WHERE `Reservationservice`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1,resvationsr.getId());
            pst.executeUpdate();
            System.out.println("Reservation supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(ReservationService resvationsr) {
        String req = "UPDATE `reservationservice` SET `nom` = ?, `idserivce_id` = ?, `email` = ? WHERE `resvationsr`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, resvationsr.getIdservice_id());
            pst.setString(2, resvationsr.getService_nom());
            pst.setString(3, resvationsr.getEmail());
            pst.setInt(4, resvationsr.getId());
            pst.executeUpdate();
            System.out.println("Reservation modifié avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<ReservationService> afficher() {
        List<ReservationService> reservationServices = new ArrayList<>();

        String req = "SELECT rs.*, s.nom AS service_nom " +
                "FROM reservationservice rs " +
                "INNER JOIN service s ON rs.idserivce_id = s.id"; // Requête SQL avec la jointure

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int idservice_id = rs.getInt("idserivce_id");
                String service_nom = rs.getString("service_nom");

                String nom = rs.getString("nom");
                String email = rs.getString("email");


                // Create a Service object
                ReservationService reservationService = new ReservationService(id, nom, email, idservice_id);
                reservationService.setService_nom(service_nom);// Set the service name
                // Add the Service object to the list
                reservationServices.add( reservationService);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reservationServices;
    }






}
