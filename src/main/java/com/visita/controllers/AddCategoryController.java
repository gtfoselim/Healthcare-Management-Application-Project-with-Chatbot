package com.visita.controllers;

import com.visita.models.Admin;
import com.visita.models.CategoryEvent;
import com.visita.models.User;
import com.visita.services.ServiceCategoryEvent;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddCategoryController implements Initializable {

    @FXML
    private TableView<CategoryEvent> TableView;

    @FXML
    private TextArea addCategoryEvenement_DESCRIPTION;

    @FXML
    private TextField addCategoryEvenement_NAME;

    @FXML
    private Button addCategoryEvenement_addBtn;

    @FXML
    private Button addCategoryEvenement_clearBtn;

    @FXML
    private TableColumn<CategoryEvent, String> addCategoryEvenement_col_DESCRIPTION;

    @FXML
    private TableColumn<CategoryEvent, String> addCategoryEvenement_col_NAME;

    @FXML
    private Button addCategoryEvenement_deleteBtn;

    @FXML
    private AnchorPane addCategoryEvenement_form;

    @FXML
    private TextField addCategoryEvenement_search;

    @FXML
    private Button addCategoryEvenement_updateBtn;

    @FXML
    private AnchorPane main_form;

    private final int rowsPerPage = 5;
    @FXML
    private Pagination paginationcnt;
    private ObservableList<CategoryEvent> categoryList = FXCollections.observableArrayList();
    private ServiceCategoryEvent serviceCategoryEvent = new ServiceCategoryEvent();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addCategoryEvenement_col_NAME.setCellValueFactory(new PropertyValueFactory<>("name"));
        addCategoryEvenement_col_DESCRIPTION.setCellValueFactory(new PropertyValueFactory<>("description"));

        try {
            List<CategoryEvent> categories = serviceCategoryEvent.getAllCategories();
            categoryList.setAll(categories);

            // Set the items directly to the TableView
            TableView.setItems(categoryList);

            // Add a listener to handle table view selection change
            TableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    // When a new row is selected, inject its data into the text fields
                    addCategoryEvenement_NAME.setText(newSelection.getName());
                    addCategoryEvenement_DESCRIPTION.setText(newSelection.getDescription());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        addCategoryEvenement_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(newValue);
        });
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, categoryList.size());
        TableView.setItems(FXCollections.observableArrayList(categoryList.subList(fromIndex, toIndex)));
        return new BorderPane(TableView);
    }

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    private void filterTable(String searchText) {
        ObservableList<CategoryEvent> filteredList = FXCollections.observableArrayList();

        for (CategoryEvent category : categoryList) {
            if (category.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(category);
            }
        }

        TableView.setItems(filteredList);
    }

    @FXML
    void ajouterService(ActionEvent event) {
        String title = addCategoryEvenement_NAME.getText();
        String description = addCategoryEvenement_DESCRIPTION.getText();

        if (title.length() < 4 || description.length() < 10) {
            // Show an alert or message indicating the minimum length requirements are not met
            showAlert("Minimum length requirements not met",
                    "Title should be at least 4 characters long\nDescription should be at least 10 characters long");
            return;
        }

        try {
            CategoryEvent categoryEvent = new CategoryEvent();
            categoryEvent.setName(title);
            categoryEvent.setDescription(description);

            serviceCategoryEvent.ajouter(categoryEvent);

            categoryList.add(categoryEvent);

            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void deleteService(ActionEvent event) {
        CategoryEvent selectedCategory = TableView.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            try {
                serviceCategoryEvent.supprimer(selectedCategory);
                categoryList.remove(selectedCategory);
                clearFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void clearbtn(ActionEvent event) {
        clearFields();
    }

    @FXML
    void updateService(ActionEvent event) {
        CategoryEvent selectedCategory = TableView.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            try {
                selectedCategory.setName(addCategoryEvenement_NAME.getText());
                selectedCategory.setDescription(addCategoryEvenement_DESCRIPTION.getText());

                serviceCategoryEvent.modifier(selectedCategory);
                clearFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        addCategoryEvenement_NAME.clear();
        addCategoryEvenement_DESCRIPTION.clear();
    }

    @FXML
    void categorypage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCategoryevent.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            // Clear the children of the addCategoryEvenement_form AnchorPane
            addCategoryEvenement_form.getChildren().clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void homepage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            // Clear the children of the addCategoryEvenement_form AnchorPane
            addCategoryEvenement_form.getChildren().clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void Eventpage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEvent.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            AddEventController controller = loader.getController();

            // Pass the logged-in admin data to the controller
            controller.setLoggedInAdmin(loggedInAdmin);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            // Clear the children of the addCategoryEvenement_form AnchorPane
            addCategoryEvenement_form.getChildren().clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Admin loggedInAdmin;



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
    public void redirecttoadminPage(ActionEvent event) {
        redirectToAddAdminController(event, loggedInAdmin);
    }

    private void redirectToAddAdminController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Afficheradmin.fxml"));
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
}
