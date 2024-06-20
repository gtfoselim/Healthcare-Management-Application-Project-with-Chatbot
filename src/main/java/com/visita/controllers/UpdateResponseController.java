package com.visita.controllers;

import com.visita.utils.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateResponseController {

    @FXML
    private TextField responseIdField;

    @FXML
    private TextArea responseContentTextArea;

    @FXML
    private void handleSave() {
        updateResponse();
    }

    @FXML
    private void handleCancel() {
        // Close the update response form
        Stage stage = (Stage) responseIdField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void updateResponse() {
        String responseIdText = responseIdField.getText().trim();
        String responseContent = responseContentTextArea.getText().trim();
        if (!responseIdText.isEmpty() && !responseContent.isEmpty()) {
            int responseId = Integer.parseInt(responseIdText);
            if (confirmUpdate()) {
                if (updateResponseInDatabase(responseId, responseContent)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Response updated successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update response.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter both response ID and content.");
        }
    }

    private boolean confirmUpdate() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Update");
        alert.setHeaderText("Are you sure you want to update this response?");
        alert.setContentText("This action cannot be undone.");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private boolean updateResponseInDatabase(int responseId, String responseContent) {
        String sql = "UPDATE reponse SET response_content = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, responseContent);
            statement.setInt(2, responseId);
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
