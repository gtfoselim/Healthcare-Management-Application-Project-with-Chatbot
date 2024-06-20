package com.visita.controllers;

import com.visita.models.Doctor;
import com.visita.models.Patient;
import com.visita.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainController {

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void showCreateReclamationView() {
        loadView("/FormReclamation.fxml", "Créer Réclamation");
    }

    @FXML
    private void showShowReclamationView() throws IOException {
        // Load the user input view to get name and surname first
        loadNomPrenomForm();
    }

    @FXML
    private void showDeleteReclamationView() {
        loadView("/DeleteReclamation.fxml", "Supprimer Réclamation");
    }

    @FXML
    private void showUpdateReclamationView() {
        loadView("/UpdateReclamation.fxml", "Mettre à Jour Réclamation");
    }

    @FXML
    private void showStatisticsView() {
        loadView("/StatisticsView.fxml", "Statistiques des Réclamations");
    }

    @FXML
    private void loadNomPrenomForm() throws IOException {
        // Charger le fichier FXML de NomPrenomForm.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NomPrenomForm.fxml"));
        Parent root = loader.load();

        // Créer une nouvelle instance du contrôleur NomPrenomFormController
        NomPrenomFormController controller = loader.getController();

        // Initialiser la référence à la scène dans NomPrenomFormController
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);

        // Passer la référence de la scène au contrôleur NomPrenomFormController
        controller.setStage(stage);

        // Afficher la scène
        stage.show();
    }

    void showReclamations(String nomPrenom) {
        // Load the ShowReclamation view with the given name and surname
        loadShowReclamationView(nomPrenom);
    }

    private Patient loggedInPatient;
    public void setLoggedInPatient(Patient patient) {
        this.loggedInPatient = patient;
        // After setting the patient, initialize the labels

    }

    @FXML
    public void redirecttoDoctorProfile(ActionEvent event) {
        redirectToAddAdminController(event, loggedInDoctor);
    }

    private void redirectToDoctorProfile(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherDoctor.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            AfficherDoctor AddAdminController = loader.getController();
            AddAdminController.setLoggedInDoctor((Doctor) user);


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
    private Doctor loggedInDoctor;
    public void setLoggedInDoctor(Doctor doctor) {
        this.loggedInDoctor = doctor;
        // After setting the patient, initialize the labels

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
    private void redirectToEvent(ActionEvent event) {
        redirectToEventController(event, loggedInPatient);
    }


    private void redirectToEventController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            HomeController chatbotController = loader.getController();

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


    private void loadShowReclamationView(String nomPrenom) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowReclamation.fxml"));
            Parent root = loader.load();

            // Pass the name and surname to ShowReclamationController
            ShowReclamationController controller = loader.getController();
            controller.setNomPrenom(nomPrenom);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Afficher Réclamations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadView(String fxmlFileName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            Stage stage = new Stage(); // Create a new stage for the new view
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
