package com.visita.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainAdminController {

    @FXML
    private Button reclamationButton;

    @FXML
    private Button responseButton;

    @FXML
    private void openReclamationView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminReclamation.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Reclamations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openResponseView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowResponse.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Responses");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
