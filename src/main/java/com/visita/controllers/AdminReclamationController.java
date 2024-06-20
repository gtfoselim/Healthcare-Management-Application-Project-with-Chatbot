package com.visita.controllers;

import com.visita.models.Admin;
import com.visita.models.Reclamation;
import com.visita.models.User;
import com.visita.utils.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

public class AdminReclamationController implements Initializable {

    @FXML
    private TableView<Reclamation> reclamationTableView;
    private Admin loggedInAdmin;
    @FXML
    private TableColumn<Reclamation, Integer> idColumn;

    @FXML
    private TableColumn<Reclamation, String> nomColumn;

    @FXML
    private TableColumn<Reclamation, String> categorieColumn;

    @FXML
    private TableColumn<Reclamation, String> sujetColumn;

    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;

    @FXML
    private TableColumn<Reclamation, String> emailColumn; // Nouvelle colonne pour l'email

    @FXML
    private TableColumn<Reclamation, String> subdateColumn;

    @FXML
    private TableColumn<Reclamation, Void> responseColumn;

    @FXML
    private TextField searchField;

    private ObservableList<Reclamation> sortedReclamations;

    @FXML
    private void showStatisticsView() {
        loadView("/StatisticsView.fxml", "Statistiques des Réclamations");
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

    public void initData(Admin admin, User loggedInUser) {
        // Check if the provided user is an instance of Patient
        if (admin != null && loggedInUser != null && loggedInUser instanceof Admin) {
            // If both the patient and loggedInUser are not null, cast loggedInUser to Patient
            // and initialize labels with patient data
            loggedInAdmin = (Admin) loggedInUser;

        } else {
            // Handle invalid data or show an error message

        }
    }
    public void setLoggedInAdmin(Admin admin) {
        this.loggedInAdmin = admin;
        // After setting the patient, initialize the labels

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser les colonnes

        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        categorieColumn.setCellValueFactory(cellData -> cellData.getValue().categorieProperty());
        sujetColumn.setCellValueFactory(cellData -> cellData.getValue().sujetProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty()); // Liaison de la colonne email
        subdateColumn.setCellValueFactory(cellData -> cellData.getValue().subdateProperty().asString());

        // Définir la méthode de cellule personnalisée pour la colonne de bouton de réponse
        setupResponseButtonColumn();

        // Charger les réclamations
        loadReclamations();

        // Trier les réclamations
        sortedReclamations = FXCollections.observableArrayList(reclamationTableView.getItems());
        Collections.sort(sortedReclamations, Comparator.comparing(Reclamation::getNom));

        // Listener pour le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                // Si le champ de recherche n'est pas vide, effectuer une recherche
                searchReclamation(newValue);
            } else {
                // Si le champ de recherche est vide, recharger toutes les réclamations
                loadReclamations();
            }
        });
    }

    private void setupResponseButtonColumn() {
        responseColumn.setCellFactory(param -> new TableCell<>() {
            private final Button responseButton = new Button("Répondre");

            {
                responseButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    openResponseForm(reclamation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(responseButton);
                }
            }
        });
    }

    private void loadReclamations() {
        ObservableList<Reclamation> reclamationList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM reclamation";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(resultSet.getInt("id"));
                reclamation.setNom(resultSet.getString("nom"));
                reclamation.setCategorie(resultSet.getString("categorie"));
                reclamation.setSujet(resultSet.getString("sujet"));
                reclamation.setDescription(resultSet.getString("description"));
                reclamation.setEmail(resultSet.getString("email")); // Récupérer l'email depuis la base de données
                reclamation.setSubdate(resultSet.getTimestamp("subdate").toLocalDateTime());
                reclamationList.add(reclamation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        reclamationTableView.setItems(reclamationList);
    }

    private void openResponseForm(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResponseForm.fxml"));
            Parent root = loader.load();

            ResponseFormController controller = loader.getController();
            controller.setReclamation(reclamation);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Respond to Reclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void searchReclamation(String targetNom) {
        reclamationTableView.getItems().clear();

        for (Reclamation reclamation : sortedReclamations) {
            if (reclamation.getNom().toLowerCase().startsWith(targetNom.toLowerCase())) {
                reclamationTableView.getItems().add(reclamation);
            }
        }
    }

    @FXML
    private void respondToReclamation(ActionEvent event) {
        Reclamation selectedReclamation = reclamationTableView.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            openResponseForm(selectedReclamation);
        } else {
            // Gérer le cas où aucune réclamation n'est sélectionnée
        }
    }

    @FXML
    private void openResponseView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminReclamation.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Reclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void redirecttoadminPage(ActionEvent event) {
        redirectToAddAdminController(event, loggedInAdmin);
    }

    private void redirectToAddAdminController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAdmin.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            AfficherAdmin AddAdminController = loader.getController();
            AddAdminController.setLoggedInAdmin((Admin) user);


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
    public void ShowRes(ActionEvent event) {
        redirectToResAdminController(event, loggedInAdmin);
    }

    private void redirectToResAdminController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowResponse.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            ShowResponseController AddAdminController = loader.getController();
            AddAdminController.setLoggedInAdmin((Admin) user);


            // Get the current stage
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
            // Traverse up the scene graph until an AnchorPane is found


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
