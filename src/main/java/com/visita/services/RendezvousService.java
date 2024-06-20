package com.visita.services;

import com.visita.models.Doctor;
import com.visita.models.Rendezvous;
import com.visita.utils.DataSource;
import javafx.scene.control.Alert;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class RendezvousService implements IRendezvous <Rendezvous>
{
    private static Connection connection = DataSource.getInstance().getConnection();

    private boolean isFullNameValid(String fullname) {
        String regex = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$";
        return fullname.matches(regex);
    }
    private boolean isTelSizeValid(int tel) {
        return String.valueOf(tel).length() == 8;
    }
    private boolean isEmailValid(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }
    public void ajouter(Rendezvous rendezvous) {
        if (isFullNameValid(rendezvous.getFullname())) {
            if (isTelSizeValid(rendezvous.getTel())) {
                if (isEmailValid(rendezvous.getEmail())) {
                    String req = "INSERT INTO `rendezvous`(`medecin_id`, `fullname`, `tel`, `date`, `time`, `note`,  `email`) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement(req);
                        preparedStatement.setInt(1, rendezvous.getMedecin_id());
                        preparedStatement.setString(2, rendezvous.getFullname());
                        preparedStatement.setInt(3, rendezvous.getTel());
                        preparedStatement.setDate(4, new Date(rendezvous.getDate().getTime()));

                        // Convertir la valeur de temps en une chaîne de caractères au format HH:MM:SS
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        String formattedTime = timeFormat.format(rendezvous.getTime());

                        preparedStatement.setString(5, formattedTime);
                        preparedStatement.setString(6, rendezvous.getNote());

                        preparedStatement.setString(7, rendezvous.getEmail());

                        preparedStatement.executeUpdate();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Rendezvous ajouté", "");
                        System.out.println("Rendez-vous ajouté avec succès !");
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Avertissement", "L'adresse e-mail n'est pas valide.", "");
                    System.out.println("L'adresse e-mail n'est pas valide.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Le numéro de téléphone doit contenir 8 chiffres", "");
                System.out.println("Le numéro de téléphone doit contenir 8 chiffres.");
            }
        } else {

            showAlert(Alert.AlertType.WARNING, "Avertissement", "Le fullname ne peut contenir que des lettres et des espaces.", "");
            System.out.println("Le fullname ne peut contenir que des lettres et des espaces.");
        }
    }



    private boolean isRendezvousExists(Rendezvous rendezvous) {
        String req = "SELECT COUNT(*) AS count FROM `rendezvous` WHERE `id` = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, rendezvous.getId());
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
    public void supprimer(Rendezvous rv) {

        String req = "DELETE FROM `rendezvous` WHERE `rendezvous`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, rv.getId());
            pst.executeUpdate();
            System.out.println("Rendezvous supprimer avec succes !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void modifier(Rendezvous rendezvous) {

        String req = "UPDATE `rendezvous` SET `medecin_id` = ?, `fullname` = ? ,`tel` = ?,`date` = ?,`time` = ?,`note` = ?,`email` = ? WHERE `rendezvous`.`id` = ?;";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, rendezvous.getMedecin_id());
            preparedStatement.setString(2, rendezvous.getFullname());
            preparedStatement.setInt(3, rendezvous.getTel());
            preparedStatement.setDate(4, new Date(rendezvous.getDate().getTime()));
            preparedStatement.setTime(5, rendezvous.getTime());
            preparedStatement.setString(6, rendezvous.getNote());

            preparedStatement.setString(7, rendezvous.getEmail());

            preparedStatement.setInt(8, rendezvous.getId());
            preparedStatement.executeUpdate();
            System.out.println("Rendezvous modifiée avec succes !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public List<Rendezvous> afficher() {
        List<Rendezvous> rendezvousList = new ArrayList<>();

        String req = "SELECT s.*, c.fullname AS medecin_nom " +
                "FROM rendezvous s " +
                "INNER JOIN medecin c ON s.medecin_id = c.id"; // Correction de la jointure
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int medecin_id = rs.getInt("medecin_id");
                String fullname = rs.getString("fullname");
                int tel = rs.getInt("tel");
                Date date = rs.getDate("date");
                Time time = rs.getTime("time");
                String note = rs.getString("note");
                boolean etat = rs.getBoolean("etat");
                String email = rs.getString("email");
                int rapport_id = rs.getInt("rapport_id");
                String medecin_nom = rs.getString("medecin_nom"); // Correction du nom de la colonne

                Rendezvous rendezvous = new Rendezvous(id, medecin_id, fullname, tel, date, time, note, etat, email, rapport_id, medecin_nom);
                rendezvousList.add(rendezvous);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rendezvousList;
    }

    public static List<Doctor> getAllMedecin() {
        List<Doctor> medecins = new ArrayList<>();
        String req = "SELECT * FROM medecin where role = '[\"medecin\"]' ";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Récupérer les colonnes de la base de données
                int id = rs.getInt("id");
                String fullname = rs.getString("fullname");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                String token = rs.getString("token");
                String photo = rs.getString("photo");
                int code = rs.getInt("code");

               // String  role = rs.getString("role");
                String[] role = rs.getString("role").split(",");
                String specialite = rs.getString("specialite");
                String address = rs.getString("adress");

// Créer un objet Medecin à partir des résultats
                Doctor medecin = new Doctor(id,  username, email, phone, password, token, photo, code,role,fullname,specialite,address);

                // Ajouter la catégorie à la liste
                medecins.add(medecin);
                System.out.println("Médecin récupéré : " + medecin);
            }

            // Fermer le ResultSet et le PreparedStatement
            rs.close();
            pst.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("pas de liste");
        }

        return medecins;
    }


    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    // Méthode pour récupérer un rendez-vous par son ID
    public Rendezvous getById(int id) {
        Rendezvous rendezVous = null;
        String sql = "SELECT * FROM rendezvous WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Mapping des résultats sur un objet RendezVous
                    rendezVous = new Rendezvous();

                    rendezVous.setId(resultSet.getInt("id"));
                    rendezVous.setMedecin_id(resultSet.getInt("medecin_id"));
                    rendezVous.setFullname(resultSet.getString("fullname"));
                    rendezVous.setTel(resultSet.getInt("tel"));
                    rendezVous.setDate(resultSet.getDate("date"));
                    rendezVous.setTime(resultSet.getTime("time"));
                    rendezVous.setNote(resultSet.getString("note"));
                    rendezVous.setEtat(resultSet.getBoolean("etat"));
                    rendezVous.setEmail(resultSet.getString("email"));
                    rendezVous.setRapport_id(resultSet.getInt("rapport_id"));

                    // Mappez les autres colonnes de la table rendezvous
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
        }

        return rendezVous;
    }



    public void modifierETAT(Rendezvous rendezvous)
    {
        // Définissez la requête SQL pour mettre à jour l'état
        String req = "UPDATE rendezvous SET etat = 1 WHERE id = ?";
        try {
        // Remplacez le paramètre de la requête par l'ID passé en argument
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, rendezvous.getId());
        preparedStatement.executeUpdate();
    } catch (SQLException e) {
    System.out.println(e.getMessage());
}

    }


    // Méthode pour envoyer des notifications de rappel par SMS
    public void envoyerNotificationsSMS(List<Rendezvous> rendezVousAVenir) {
        // Parcourir les rendez-vous à venir pour envoyer les notifications
        for (Rendezvous rendezVous : rendezVousAVenir) {
            if (doitEnvoyerNotificationRappel(rendezVous)) {
                // Construire le message de notification
                String message = "N'oubliez Pas votre Rendezvous : " + rendezVous.getFullname() +"\nBe on Time ";
                // Envoyer le SMS
                String tel ="+21629304408";
                com.visita.controllers.TwilioSMS.envoyerSMS(tel,message);
            }
        }
    }

    // Méthode pour vérifier si un rendez-vous doit recevoir une notification de rappel
    private boolean doitEnvoyerNotificationRappel(Rendezvous rendezVous) {
        // Logique pour déterminer si une notification doit être envoyée
        // Par exemple, si la date du rendez-vous est dans les 24 prochaines heures
        long tempsActuel = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long differenceTemps = rendezVous.getDate().getTime() / 1000 - tempsActuel;
        // Si le rendez-vous est dans les 24 prochaines heures
        return (differenceTemps > 0 && differenceTemps <= 24 * 60 * 60);
    }

}
