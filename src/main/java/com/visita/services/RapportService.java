package com.visita.services;

import com.visita.models.Rapport;
import com.visita.models.Rendezvous;
import com.visita.utils.DataSource;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RapportService
{
    private static Connection connection = DataSource.getInstance().getConnection();


    private boolean isRendezvousExists(int rendezvousId) {
        String req = "SELECT COUNT(*) AS count FROM `rendezvous` WHERE `id` = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, rendezvousId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'existence du rendez-vous : " + e.getMessage());
        }
        return false;
    }
    private boolean isRapportExistsForRendezvous(int rendezvousId) {
        String req = "SELECT COUNT(*) AS count FROM `rapport` WHERE `rendezvous_id` = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, rendezvousId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'existence du rapport pour le rendez-vous : " + e.getMessage());
        }
        return false;
    }
    public void ajouter(Rapport r) {
        // Vérifier si l'identifiant du rendez-vous existe dans la base de données
        if (isRendezvousExists(r.getRendzvous_id())) {
            if (!isRapportExistsForRendezvous(r.getRendzvous_id())) {
            String reqInsertRapport = "INSERT INTO `rapport`(`rendezvous_id`, `type`, `note`) VALUES (?, ?, ?)";
            String reqUpdateRendezvous = "UPDATE `rendezvous` SET `rapport_id` = ? WHERE `id` = ?";
            try {
                // Insertion du rapport
                PreparedStatement pstInsertRapport = connection.prepareStatement(reqInsertRapport, Statement.RETURN_GENERATED_KEYS);
                pstInsertRapport.setInt(1, r.getRendzvous_id());
                pstInsertRapport.setString(2, r.getType());
                pstInsertRapport.setString(3, r.getNote());
                int insertedRows = pstInsertRapport.executeUpdate();

                // Récupération de l'identifiant auto-incrémenté du rapport
                if (insertedRows > 0) {
                    ResultSet generatedKeys = pstInsertRapport.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int rapportId = generatedKeys.getInt(1);

                        // Mise à jour du rendez-vous avec l'identifiant du rapport
                        PreparedStatement pstUpdateRendezvous = connection.prepareStatement(reqUpdateRendezvous);
                        pstUpdateRendezvous.setInt(1, rapportId);
                        pstUpdateRendezvous.setInt(2, r.getRendzvous_id());
                        pstUpdateRendezvous.executeUpdate();

                        showAlert(Alert.AlertType.INFORMATION, "Succes", "Rapport ajouté", "");

                        System.out.println("Rapport ajouté avec succès !");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout du rapport : " + e.getMessage());
            }
        } else {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Un rapport existe déjà pour ce rendez-vous. Impossible d'ajouter un nouveau rapport.", "");

                System.out.println("Un rapport existe déjà pour ce rendez-vous. Impossible d'ajouter un nouveau rapport.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le rendez-vous avec l'identifiant " + r.getRendzvous_id() + " n'existe pas dans la base de données. Impossible d'ajouter le rapport.", "");

            System.out.println("Le rendez-vous avec l'identifiant " + r.getRendzvous_id() + " n'existe pas dans la base de données. Impossible d'ajouter le rapport.");
        }
    }


    private boolean isRapportExists(int rapportId) {
        String req = "SELECT COUNT(*) AS count FROM `rapport` WHERE `id` = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, rapportId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'existence du rapport : " + e.getMessage());
        }
        return false;
    }
    public void supprimer (Rapport r)
    {
        if (isRapportExists(r.getId())) {
            String req = "DELETE FROM `rapport` WHERE `rapport`.`id` = ?";
            try {
                PreparedStatement pst = connection.prepareStatement(req);
                pst.setInt(1, r.getId());
                pst.executeUpdate();
                System.out.println("Rapport supprimé avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la suppression du rapport : " + e.getMessage());
            }
        } else {
            System.out.println("Le rapport avec l'identifiant " + r.getId() + " n'existe pas dans la base de données.");
        }
    }


    public void modifier( Rapport r) {

        String req = "UPDATE `rapport` SET `rendezvous_id` = ?, `type` = ? ,`note` = ? WHERE `rapport`.`id` = ?;";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, r.getRendzvous_id());
            preparedStatement.setString(2, r.getType());
            preparedStatement.setString(3, r.getNote());
            preparedStatement.setInt(4, r.getId());
            preparedStatement.executeUpdate();


            showAlert(Alert.AlertType.INFORMATION, "Succes", "Rapport modifié", "");
            System.out.println("Rapport modifiée avec succes !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "FAIL", "on peut pas modifier rapport ", "");
            System.out.println(e.getMessage());
        }

    }
        public static List<Rendezvous> getAllrv() {
        List<Rendezvous> rvs = new ArrayList<>();
        String req1 = "SELECT * FROM rendezvous;";

        try {
            PreparedStatement pst = connection.prepareStatement(req1);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Récupérer les colonnes de la base de données
                int id = rs.getInt("id");
                int idM = rs.getInt("medecin_id");
                String fullname = rs.getString("fullname");
                // Récupérer la colonne de type date
                Date date = rs.getDate("date"); // Si la colonne est de type DATE

                // Récupérer la colonne de type datetime
                Time time = rs.getTime("time"); //
                int tel = rs.getInt("tel");
                String note = rs.getString("note");
                boolean etat = rs.getBoolean("etat");
                String email = rs.getString("email");
                int rapportid = rs.getInt("rapport_id");

                // Créer un objet Category à partir des résultats
                Rendezvous rv = new Rendezvous(id,idM, fullname, tel, date,time,note,etat,email,rapportid);

                // Ajouter la catégorie à la liste
                rvs.add(rv);
            }

            // Fermer le ResultSet et le PreparedStatement
            rs.close();
            pst.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rvs;
    }





    public List<Rapport> afficher ()
    {
        List<Rapport> Rapports = new ArrayList();

        String req = "SELECT s.*, c.fullname AS rendezvous_nom " +
                "FROM rapport s " +
                "INNER JOIN rendezvous c ON s.rendezvous_id = c.id"; // Correction de la jointure
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Rapports.add(new Rapport(rs.getInt("id"),rs.getInt("rendezvous_id"), rs.getString("type"), rs.getString("note"), rs.getString("rendezvous_nom")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Rapports;
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }



    public Rapport getById(int rapportId) {
        Rapport rapport = null;
        try {
            // Créer la connexion à la base de données
            Connection connection = DataSource.getInstance().getConnection();

            // Préparer la requête SQL
            String sql = "SELECT * FROM rapport WHERE id = ?";

            // Créer l'objet PreparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, rapportId);

            // Exécuter la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            // Vérifier si le rapport existe
            if (resultSet.next()) {
                // Créer un nouvel objet Rapport avec les données récupérées de la base de données

                String type = resultSet.getString("type");
                String note = resultSet.getString("note");
                rapport = new Rapport(type, note);
                // Ajouter d'autres attributs si nécessaire
            }


        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions
        }
        return rapport;
    }

}
