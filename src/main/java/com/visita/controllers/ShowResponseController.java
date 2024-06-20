package com.visita.controllers;

import com.visita.models.Admin;
import com.visita.models.Response;
import com.visita.models.User;
import com.visita.utils.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowResponseController {

    @FXML
    private TableView<Response> responseTableView;

    @FXML
    private TableColumn<Response, Integer> idColumn;

    @FXML
    private TableColumn<Response, Integer> reclamationIdColumn;

    @FXML
    private TableColumn<Response, String> authorColumn;

    @FXML
    private TableColumn<Response, String> responseContentColumn;

    @FXML
    private TableColumn<Response, String> responseDateColumn;

    @FXML
    private TextField searchField;
    private Admin loggedInAdmin;
    @FXML
    private Button refreshButton;

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


    @FXML
    private void initialize() {
        // Configuration de la colonne ID


        // Configuration de la colonne Reclamation ID
        reclamationIdColumn.setCellValueFactory(cellData -> cellData.getValue().reclamationIdProperty().asObject());

        // Configuration de la colonne Author
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        // Configuration de la colonne Response Content
        responseContentColumn.setCellValueFactory(cellData -> cellData.getValue().responseContentProperty());

        // Configuration de la colonne Response Date
        responseDateColumn.setCellValueFactory(cellData -> cellData.getValue().responseDateProperty().asString());

        // Configuration de la colonne Action
        setupActionColumn();

        // Ajout d'un écouteur sur le champ de recherche pour filtrer les réponses
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterResponses(newValue);
        });

        // Chargement des réponses dans le TableView
        loadResponses();

        // Configuration de l'action de rafraîchissement du bouton Refresh
        refreshButton.setOnAction(event -> handleRefresh());
    }

    private void filterResponses(String searchText) {
        ObservableList<Response> filteredList = FXCollections.observableArrayList();
        for (Response response : responseTableView.getItems()) {
            if (responseContainsText(response, searchText)) {
                filteredList.add(response);
            }
        }
        responseTableView.setItems(filteredList);
    }
    private boolean responseContainsText(Response response, String searchText) {
        String author = response.getAuthor().toLowerCase();
        String content = response.getResponseContent().toLowerCase();
        return author.contains(searchText.toLowerCase()) || content.contains(searchText.toLowerCase());
    }

    private void setupActionColumn() {
        TableColumn<Response, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(param -> new TableCell<Response, Void>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");

            {
                // Define actions for the buttons
                updateButton.setOnAction(event -> {
                    Response response = getTableRow().getItem();
                    if (response != null) {
                        openUpdateView(response);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Response response = getTableRow().getItem();
                    if (response != null && confirmDelete()) {
                        deleteResponse(response);
                        getTableView().getItems().remove(response);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(updateButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        actionColumn.setPrefWidth(100); // Adjust the width as needed
        responseTableView.getColumns().add(actionColumn);
    }

    @FXML
    private void handleRefresh() {
        // Refresh the table data
        refreshTable();
    }

    private void refreshTable() {
        responseTableView.getItems().clear();
        loadResponses();
    }

    private void loadResponses() {
        ObservableList<Response> responseList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM reponse";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Response response = new Response();
                response.setId(resultSet.getInt("id"));
                response.setReclamationId(resultSet.getInt("reclamation_id"));
                response.setAuthor(resultSet.getString("author"));
                response.setResponseContent(resultSet.getString("response_content"));
                response.setResponseDate(resultSet.getTimestamp("response_date").toLocalDateTime());
                responseList.add(response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        responseTableView.setItems(responseList);
    }


    @FXML
    private void handleUpdateResponse() {
        Response selectedResponse = responseTableView.getSelectionModel().getSelectedItem();
        if (selectedResponse != null) {
            openUpdateView(selectedResponse);
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a response to update.");
        }
    }

    @FXML
    private void handleDeleteResponse() {
        Response selectedResponse = responseTableView.getSelectionModel().getSelectedItem();
        if (selectedResponse != null && confirmDelete()) {
            deleteResponse(selectedResponse);
            responseTableView.getItems().remove(selectedResponse);
        }
    }

    private void openUpdateView(Response response) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateResponseForm.fxml"));
            Parent root = loader.load();

            UpdateResponseFormController controller = loader.getController();
            controller.setResponse(response);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Response");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteResponse(Response response) {
        String sql = "DELETE FROM reponse WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, response.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Response deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete response.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting response.");
        }
    }
    private boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Are you sure you want to delete this response?");
        alert.setContentText("This action cannot be undone.");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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
    private void ShowRec(ActionEvent event) {

        redirectToRecserController(event,loggedInAdmin);
    }

    private void redirectToRecserController(ActionEvent event,User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminReclamation.fxml"));
            Parent root = loader.load();
            AdminReclamationController AdminTableViewController = loader.getController();
            AdminTableViewController.setLoggedInAdmin((Admin) user);
            // Pass the authenticated user to the controller

            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
