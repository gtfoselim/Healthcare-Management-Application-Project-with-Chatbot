package com.visita.controllers;

import com.visita.models.Reclamation;
import com.visita.utils.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteReclamationController {

    private Reclamation reclamationToDelete;

    // Set the reclamation to delete
    public void setReclamationToDelete(Reclamation reclamation) {
        this.reclamationToDelete = reclamation;
    }

    // Handle delete button action
    @FXML
    private void handleDelete() {
        if (reclamationToDelete != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirmation de la suppression");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");

            // Wait for user's response
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Delete reclamation from the database
                    if (deleteReclamationFromDatabase(reclamationToDelete)) {
                        // Show success message
                        Alert successAlert = new Alert(AlertType.INFORMATION);
                        successAlert.setTitle("Suppression réussie");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("La réclamation a été supprimée avec succès.");
                        successAlert.showAndWait();
                    } else {
                        // Show error message
                        Alert errorAlert = new Alert(AlertType.ERROR);
                        errorAlert.setTitle("Erreur");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Erreur lors de la suppression de la réclamation.");
                        errorAlert.showAndWait();
                    }
                }
            });
        }
    }

    // Method to delete reclamation from the database
    private boolean deleteReclamationFromDatabase(Reclamation reclamation) {
        String sql = "DELETE FROM reclamation WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reclamation.getId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
