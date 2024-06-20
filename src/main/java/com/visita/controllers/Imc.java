package com.visita.controllers;

import com.visita.models.Patient;
import com.visita.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class Imc {

    @FXML
    private TextField weightTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private Label resultLabel;

    @FXML
    void calculateIMC() {
        try {
            double weight = Double.parseDouble(weightTextField.getText());
            double height = Double.parseDouble(heightTextField.getText());

            // Calculate BMI
            double bmi = weight / (height * height);

            // Determine BMI category
            String category;
            if (bmi < 18.5) {
                category = "Sous-poids";
                setCategoryStyle(category, bmi, "/imcR1.fxml");
            } else if (bmi >= 18.5 && bmi < 25) {
                category = "Poids normal";
                setCategoryStyle(category, bmi, "/imcR2.fxml");
            } else {
                category = "Surpoids";
                setCategoryStyle(category, bmi, "/imcR3.fxml");
            }

            // Display the result with appropriate color
            resultLabel.setText(String.format("Votre IMC est %.2f (%s)", bmi, category));

        } catch (NumberFormatException e) {
            resultLabel.setText("Veuillez entrer des valeurs valides pour le poids et la taille.");
            resultLabel.getStyleClass().clear(); // Clear previous styles
        }
    }

    private void setCategoryStyle(String category, double bmi, String fxmlFile) {
        resultLabel.getStyleClass().clear(); // Clear previous styles

        String idealWeightRange = "";
        if (category.equals("Sous-poids")) {
            resultLabel.getStyleClass().add("underweight");
            idealWeightRange = "Poids idéal: 18.5 - 24.9 kg/m²";
        } else if (category.equals("Poids normal")) {
            resultLabel.getStyleClass().add("normal-weight");
            idealWeightRange = "Poids idéal: 18.5 - 24.9 kg/m²";
        } else {
            resultLabel.getStyleClass().add("overweight");
            idealWeightRange = "Poids idéal: 18.5 - 24.9 kg/m²";
        }

        // Load the appropriate FXML
        loadFXML(fxmlFile);

        resultLabel.setText(String.format("Votre IMC est %.2f (%s)\n%s", bmi, category, idealWeightRange));
    }

    private void loadFXML(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Patient loggedInPatient;
    public void setLoggedInPatient(Patient patient) {
        this.loggedInPatient = patient;
        // After setting the patient, initialize the labels

    }
    @FXML
    public void redirecttoadminPage(ActionEvent event) {
        redirectToAddAdminController(event, loggedInPatient);
    }

    private void redirectToAddAdminController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            AfficherUser AddAdminController = loader.getController();
            AddAdminController.setLoggedInPatient((Patient) user);


            // Get the current stage
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
            // Traverse up the scene graph until an AnchorPane is found


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleLogoutButton(ActionEvent event) {
        // Code to handle the logout action goes here
        logout(event);
    }

    private void logout(ActionEvent event) {
        loggedInPatient = null;

        // Redirect the user to the login page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void RedirectToDoctorProfile(ActionEvent event) throws IOException {
        if (loggedInPatient != null) {

            RedirectToTableViewAdminUser(event, loggedInPatient);
        } else {

        }
    }


    private void RedirectToTableViewAdminUser(ActionEvent event, User user) throws IOException {
        if (user instanceof Patient) {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowDoctor.fxml"));
            Parent root = loader.load();

            ShowDoctor adminTableViewController = loader.getController();
            adminTableViewController.setLoggedInPatient((Patient) user);

            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } else {

        }
    }
    @FXML
    private void redirectToChatBot(ActionEvent event) {
        redirectToChatVSTController(event, loggedInPatient);
    }


    private void redirectToChatVSTController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatbot.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            Chatbot chatbotController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller
            chatbotController.setLoggedInPatient((Patient) user);

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
