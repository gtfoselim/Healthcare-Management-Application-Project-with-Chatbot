package com.visita.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NomPrenomFormController {

    @FXML
    private TextField nomPrenomField;

    private Stage stage; // référence à la scène actuelle

    // Méthode pour définir la scène actuelle
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private MainController mainController; // Référence au contrôleur principal

    // Méthode pour définir le contrôleur principal
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    // Méthode appelée lorsque l'utilisateur clique sur le bouton Valider
    @FXML
    private void valider() throws IOException {
        // Récupérer le nom complet saisi par l'utilisateur
        String nomPrenom = nomPrenomField.getText();

        // Passer les données saisies à la page ShowReclamation
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowReclamation.fxml"));
        Parent root = loader.load();
        ShowReclamationController controller = loader.getController();
        controller.setNomPrenom(nomPrenom);

        // Afficher la page ShowReclamation
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
