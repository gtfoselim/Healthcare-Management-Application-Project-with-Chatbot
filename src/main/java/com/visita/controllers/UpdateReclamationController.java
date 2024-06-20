package com.visita.controllers;

import com.visita.models.Reclamation;
import com.visita.utils.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateReclamationController {

    @FXML
    private TextField nomField;

    @FXML
    private ComboBox<String> categorieComboBox; // Change to ComboBox

    @FXML
    private TextField sujetField;

    @FXML
    private TextField descriptionField;

    private Reclamation reclamationToUpdate;

    public void setReclamationToUpdate(Reclamation reclamationToUpdate) {
        this.reclamationToUpdate = reclamationToUpdate;
        fillFieldsWithReclamationData();
    }

    private void fillFieldsWithReclamationData() {
        nomField.setText(reclamationToUpdate.getNom());
        categorieComboBox.setValue(reclamationToUpdate.getCategorie()); // Set ComboBox value
        sujetField.setText(reclamationToUpdate.getSujet());
        descriptionField.setText(reclamationToUpdate.getDescription());
    }

    @FXML
    private void handleUpdate() {
        if (reclamationToUpdate != null) {
            updateReclamationInDatabase();
            closeUpdateWindow();
        }
    }

    private void updateReclamationInDatabase() {
        String sql = "UPDATE reclamation SET nom=?, categorie=?, sujet=?, description=? WHERE id=?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nomField.getText());
            statement.setString(2, categorieComboBox.getValue()); // Use ComboBox value
            statement.setString(3, sujetField.getText());
            statement.setString(4, descriptionField.getText());
            statement.setInt(5, reclamationToUpdate.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeUpdateWindow() {
        nomField.getScene().getWindow().hide();
    }
}
