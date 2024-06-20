package com.visita.controllers;

import com.visita.models.Response;
import com.visita.utils.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateResponseFormController {

    @FXML
    private TextArea responseContentTextArea;

    private Response response;

    public void setResponse(Response response) {
        this.response = response;
        // Pre-fill the content TextArea with the response content
        responseContentTextArea.setText(response.getResponseContent());
    }

    @FXML
    private void handleSave() {
        String responseContent = responseContentTextArea.getText().trim();
        if (!responseContent.isEmpty()) {
            if (confirmUpdate()) {
                if (updateResponseInDatabase(response.getId(), responseContent)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Response updated successfully.");
                    // Close the Update Response Form
                    closeWindow();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update response.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter response content.");
        }
    }

    @FXML
    private void handleCancel() {
        // Close the Update Response Form without saving
        closeWindow();
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
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update response. Please try again later.");
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        // Close the Update Response Form window
        responseContentTextArea.getScene().getWindow().hide();
    }
}
