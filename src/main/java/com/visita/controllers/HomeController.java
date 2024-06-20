package com.visita.controllers;

import com.visita.models.Doctor;
import com.visita.models.Evenement;
import com.visita.models.Patient;
import com.visita.models.User;
import com.visita.services.ServiceEvenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HomeController {
    @FXML
    private TextField search;
    @FXML
    private HBox content;

    @FXML
    private Button nextPageButton;

    @FXML
    private Label pageNumberLabel;

    @FXML
    private Button prevPageButton;

    private final ServiceEvenement serviceEvenement;
    private int currentPage = 1;
    private int pageSize = 2; // Adjust the page size as needed

    public HomeController() {
        this.serviceEvenement = new ServiceEvenement();
    }

    @FXML
    private void initialize() {
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() || newValue == null) {
                currentPage = 1;
                loadEventItems();
            } else {
                searchEvents(newValue);
            }
        });
        loadEventItems();
    }

    private void loadEventItems() {
        try {
            List<Evenement> evenements = serviceEvenement.getEventsForPage(currentPage, pageSize);
            content.getChildren().clear(); // Clear previous items
            for (Evenement evenement : evenements) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventFront.fxml"));
                VBox item = loader.load();
                EventFrontController eventFrontController = loader.getController();
                eventFrontController.setData(evenement);
                content.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void searchEvents(String keyword) {
        try {
            List<Evenement> evenements = serviceEvenement.searchEvents(keyword);
            content.getChildren().clear(); // Clear previous items
            for (Evenement evenement : evenements) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventFront.fxml"));
                VBox item = loader.load();
                EventFrontController eventFrontController = loader.getController();
                eventFrontController.setData(evenement);
                content.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void nextPage() {
        currentPage++;
        loadEventItems();
        updatePageNumberLabel();
    }

    @FXML
    private void prevPage() {
        if (currentPage > 1) {
            currentPage--;
            loadEventItems();
            updatePageNumberLabel();
        }
    }

    private void updatePageNumberLabel() {
        pageNumberLabel.setText(String.valueOf(currentPage));
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
    private void RedirectToAfficherPost(ActionEvent event) {
        redirectToAffcherPostController(event, loggedInPatient);
    }


    private void redirectToAffcherPostController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherpost.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            afficherpost afficherpostController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller
            afficherpostController.setLoggedInPatient((Patient) user);

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
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

    @FXML
    private void RedirectToImcPost(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AddService
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/imc.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre pour afficher la scène
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Gérer les exceptions liées au chargement du fichier FXML
            e.printStackTrace();
        }
    }


    @FXML
    private void RedirectToServicePost(ActionEvent event) {
        redirectToAffcherServController(event, loggedInPatient);
    }


    private void redirectToAffcherServController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherService.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            AffichageService afficherpostController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller
            afficherpostController.setLoggedInPatient((Patient) user);

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void redirectToRec(ActionEvent event) {
        redirectToReclaimController(event, loggedInPatient);
    }


    private void redirectToReclaimController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            MainController chatbotController = loader.getController();

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

    private Doctor loggedInDoctor;
    public void setLoggedInDoctor(Doctor doctor) {
        this.loggedInDoctor = doctor;
        // After setting the patient, initialize the labels

    }



}


