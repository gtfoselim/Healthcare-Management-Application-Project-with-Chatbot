package com.visita.services;

import com.visita.models.Reclamation;
import com.visita.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ReclamationDAO {

    public static boolean saveReclamationToDatabase(Reclamation reclamation) {
        String sql = "INSERT INTO reclamation (nom, categorie, sujet, description, subdate, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reclamation.getNom());
            statement.setString(2, reclamation.getCategorie());
            statement.setString(3, reclamation.getSujet());
            statement.setString(4, reclamation.getDescription());
            statement.setObject(5, reclamation.getSubdate());
            statement.setString(6, reclamation.getEmail()); // Ajout de l'e-mail

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

