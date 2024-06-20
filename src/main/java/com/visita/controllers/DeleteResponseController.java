package com.visita.controllers;

import com.visita.utils.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteResponseController {

    @FXML
    private TextField responseIdField;

    @FXML
    private void deleteResponse() {
        String responseIdText = responseIdField.getText().trim();
        if (!responseIdText.isEmpty()) {
            int responseId = Integer.parseInt(responseIdText);
            if (confirmDeletion()) {
                if (deleteResponseFromDatabase(responseId)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Response deleted successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete response.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a response ID.");
        }
    }

    private boolean confirmDeletion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this response?");
        alert.setContentText("This action cannot be undone.");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private boolean deleteResponseFromDatabase(int responseId) {
        String sql = "DELETE FROM reponse WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, responseId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
