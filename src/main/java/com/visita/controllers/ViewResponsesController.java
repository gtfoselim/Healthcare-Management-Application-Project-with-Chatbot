package com.visita.controllers;

import com.visita.models.Reclamation;
import com.visita.utils.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewResponsesController {

    @FXML
    private TextArea responsesTextArea;
    @FXML
    private Button sendRatingButton;
    @FXML
    private Slider ratingSlider;
    @FXML
    private Label averageRatingLabel;

    private Reclamation reclamation;

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        loadResponses();
        calculateAverageRating();
    }

    private void loadResponses() {
        StringBuilder responsesText = new StringBuilder();
        String sql = "SELECT * FROM reponse WHERE reclamation_id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reclamation.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    responsesText.append("Response ID: ").append(resultSet.getInt("id")).append("\n");
                    responsesText.append("Author: ").append(resultSet.getString("author")).append("\n");
                    responsesText.append("Content: ").append(resultSet.getString("response_content")).append("\n\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        responsesTextArea.setText(responsesText.toString());
    }

    private void calculateAverageRating() {
        String sql = "SELECT AVG(evaluation) AS average_rating FROM reponse_evaluation WHERE reclamation_id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reclamation.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double averageRating = resultSet.getDouble("average_rating");
                    averageRatingLabel.setText("Average Rating: " + String.format("%.2f", averageRating));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendRating() {
        int rating = (int) ratingSlider.getValue();
        insertRating(reclamation.getId(), rating);
        calculateAverageRating();
        sendRatingButton.setDisable(true);
    }

    private void insertRating(int reclamationId, int rating) {
        String sql = "INSERT INTO reponse_evaluation (reclamation_id, evaluation) VALUES (?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reclamationId);
            statement.setInt(2, rating);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
