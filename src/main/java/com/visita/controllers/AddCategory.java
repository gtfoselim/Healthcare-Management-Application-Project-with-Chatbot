package com.visita.controllers;
import com.visita.models.Admin;
import com.visita.models.Category;
import com.visita.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.visita.services.Categoryservice;
import javafx.stage.Window;


public class AddCategory  {

    @FXML
    private AnchorPane main_form;

    @FXML
    private TextArea addCategory_DESCRIPTION;

    @FXML
    private TextField addCategory_ICON;

    @FXML
    private TextField addCategory_NAME;

    @FXML
    private Button addCategory_addBtn;

    @FXML
    private Button addCategory_clearBtn;
    @FXML
    private TableView<Category> TableView;

    @FXML
    private TableColumn<Category, String> addCategory_col_DESCRIPTION;

    @FXML
    private TableColumn<Category, String> addCategory_col_ICON;

    @FXML
    private TableColumn<Category, Integer> addCategory_col_ID;

    @FXML
    private TableColumn<Category, String> addCategory_col_NAME;

    @FXML
    private Button addCategory_deleteBtn;

    @FXML
    private AnchorPane addCategory_form;

    @FXML
    private TextField addCategory_search;

    private Admin loggedInAdmin;
    @FXML
    private Button addCategory_updateBtn;
    @FXML
    private Button category_btn_nv;
    @FXML
    private Button service_btn_nv;

    @FXML
    private Button serviceresv_btn_nv;
    public  void close(){
        System.exit(0);
    }
    public void minimize(){

        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
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


    Categoryservice ps= new Categoryservice();
    @FXML
    void AjouterC(ActionEvent event) {
        // Obtenir les données saisies par l'utilisateur
        String categoryName = addCategory_NAME.getText();
        String categoryDescription = addCategory_DESCRIPTION.getText();
        String categoryIcon = addCategory_ICON.getText();

        // Valider les données saisies
        if (!validateInput()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Données invalides");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs requis correctement.");
            alert.showAndWait();
            return; // Interrompre l'ajout si les données sont invalides
        }

        // Vérifiez si le nom de la catégorie est unique
        if (!ps.isCategoryNameUnique(categoryName)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nom de catégorie déjà existant");
            alert.setHeaderText(null);
            alert.setContentText("Le nom de la catégorie existe déjà. Veuillez choisir un autre nom.");
            alert.showAndWait();
            return; // Interrompre l'ajout si le nom de catégorie n'est pas unique
        }

        // Créer une nouvelle catégorie à partir des données saisies
        Category newCategory = new Category(categoryName, categoryDescription, categoryIcon);

        // Ajouter la nouvelle catégorie
        ps.ajouter(newCategory);

        // Afficher un message de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Catégorie ajoutée");
        alert.setHeaderText(null);
        alert.setContentText("La catégorie a été ajoutée avec succès !");
        alert.showAndWait();

        // Rafraîchir la TableView pour afficher la nouvelle catégorie
        refreshTableView();

        // Effacer les champs de texte
        clearFields();
    }



    // Méthode pour valider les entrées de l'utilisateur
    boolean validateInput() {
        boolean isValid = true;

        // Valider le champ de texte pour le nom (uniquement des caractères alphabétiques)
        String name = addCategory_NAME.getText();
        if (name.isEmpty() || !name.matches("[a-zA-Z]+")) {
            addCategory_NAME.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            addCategory_NAME.setStyle(null);
        }

        // Valider le champ de texte pour la description (non vide)
        if (addCategory_DESCRIPTION.getText().isEmpty()) {
            addCategory_DESCRIPTION.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            addCategory_DESCRIPTION.setStyle(null);
        }
        // Valider le champ de texte pour l'icône (non vide)
        String icon = addCategory_ICON.getText();
        if (icon.isEmpty()) {
            addCategory_ICON.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            addCategory_ICON.setStyle(null);
        }

        // Ajouter d'autres contrôles de saisie ici si nécessaire

        return isValid;
    }

    // Méthode pour effacer les champs de texte






    private void refreshTableView() {
        List<Category> categories = ps.afficher();
        ObservableList<Category> observableList = FXCollections.observableList(categories);
        TableView.setItems(observableList);
        // Force the TableView to refresh
        TableView.refresh();
    }

    @FXML
    void supprimerC(ActionEvent event) {
        // Obtenir la catégorie sélectionnée dans la TableView
        Category selectedCategory = TableView.getSelectionModel().getSelectedItem();

        // Vérifier si une catégorie est sélectionnée
        if (selectedCategory != null) {
            // Demander confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer cette catégorie ?");
            alert.setContentText("Catégorie : " + selectedCategory.getNom() + "\n\nCette action est irréversible. Veuillez confirmer.");

            Optional<ButtonType> result = alert.showAndWait();

            // Si l'utilisateur confirme, supprimer la catégorie
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer la catégorie avec le service
                ps.supprimer(selectedCategory);
                refreshTableView();

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Suppression réussie");
                successAlert.setHeaderText("Catégorie supprimée !");
                successAlert.setContentText("La catégorie \"" + selectedCategory.getNom() + "\" a été supprimée avec succès.\n\n Opération réussie !");
                successAlert.showAndWait();
            }
        } else {
            // Aucune catégorie sélectionnée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sélection manquante");
            alert.setHeaderText("Erreur de sélection");
            alert.setContentText(" Il semble que vous n'ayez pas sélectionné de catégorie. Veuillez en choisir une à supprimer.");
            alert.showAndWait();
        }
        // Effacer les champs de texte après la modification
        clearFields();
    }

    @FXML
    void modifierC(ActionEvent event) {
        // Obtenir la catégorie sélectionnée dans la TableView
        Category selectedCategory = TableView.getSelectionModel().getSelectedItem();

        // Vérifier si une catégorie est sélectionnée
        if (selectedCategory != null) {
            // Mettre à jour les attributs de la catégorie avec les nouvelles valeurs entrées par l'utilisateur
            selectedCategory.setNom(addCategory_NAME.getText());
            selectedCategory.setDescription(addCategory_DESCRIPTION.getText());
            selectedCategory.setIcon(addCategory_ICON.getText());

            // Mettre à jour la catégorie avec le service
            ps.modifier(selectedCategory);

            // Mettre à jour la TableView pour refléter les modifications
            refreshTableView();

            // Afficher un message de succès
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Mise à jour réussie");
            successAlert.setHeaderText("Catégorie mise à jour");
            successAlert.setContentText("La catégorie \"" + selectedCategory.getNom() + "\" a été mise à jour avec succès.\n\nBravo pour le travail !");
            successAlert.showAndWait();
        } else {
            // Aucune catégorie sélectionnée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de sélection");
            alert.setHeaderText("Aucune catégorie sélectionnée");
            alert.setContentText("Oups ! Veuillez sélectionner une catégorie à mettre à jour.");
            alert.showAndWait();
        }
        clearFields();
    }





    @FXML
    void initialize() {
        List<Category> categories = ps.afficher();

        // Configurer la TableView pour permettre la sélection multiple
        TableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ObservableList<Category> observableList= FXCollections.observableList(categories);
        TableView.setItems(observableList);

        addCategory_col_NAME.setCellValueFactory(new PropertyValueFactory<>("nom"));
        addCategory_col_DESCRIPTION.setCellValueFactory(new PropertyValueFactory<>("description"));
        addCategory_col_ICON.setCellValueFactory(new PropertyValueFactory<>("icon"));
        //addCategory_col_ICON.setCellFactory(col -> new IconCell());

// Gestionnaire d'événements pour la sélection d'une catégorie dans la TableView
        TableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Vérifiez si une nouvelle catégorie est sélectionnée
            if (newValue != null) {
                // Mettre à jour les champs de texte avec les détails de la catégorie sélectionnée
                addCategory_NAME.setText(newValue.getNom());
                addCategory_DESCRIPTION.setText(newValue.getDescription());
                addCategory_ICON.setText(newValue.getIcon());
            }
        });

        // Activer le tri sur les colonnes

        addCategory_col_NAME.setSortable(true);
        addCategory_col_DESCRIPTION.setSortable(true);
        addCategory_col_ICON.setSortable(true);


    }

    @FXML
    void clearFields(ActionEvent event) {
        // Effacer les champs de texte
        addCategory_NAME.setText("");
        addCategory_DESCRIPTION.setText("");
        addCategory_ICON.setText("");
    }
    void clearFields() {
        // Effacer les champs de texte
        addCategory_NAME.setText("");
        addCategory_DESCRIPTION.setText("");
        addCategory_ICON.setText("");
    }

    @FXML
    void searchCategories() {
        // Obtenez les critères de recherche de l'utilisateur
        String searchText = addCategory_search.getText().toLowerCase();

        // Filtrer la liste des catégories en fonction des critères de recherche
        List<Category> allCategories = ps.afficher(); // Récupérez toutes les catégories
        List<Category> filteredCategories = new ArrayList<>();

        for (Category category : allCategories) {
            // Vérifiez si l'ID, le nom, la description ou l'icône de la catégorie correspondent aux critères de recherche
            boolean matchesId = String.valueOf(category.getId()).contains(searchText);
            boolean matchesName = category.getNom().toLowerCase().contains(searchText);
            boolean matchesDescription = category.getDescription().toLowerCase().contains(searchText);
            boolean matchesIcon = category.getIcon().toLowerCase().contains(searchText);

            // Si l'un des champs correspond aux critères de recherche, ajoutez la catégorie à la liste filtrée
            if (matchesId || matchesName || matchesDescription || matchesIcon) {
                filteredCategories.add(category);
            }
        }

        // Mettre à jour la TableView avec les catégories filtrées
        ObservableList<Category> observableList = FXCollections.observableList(filteredCategories);
        TableView.setItems(observableList);
    }

    /* @FXML
      private void handleNewServiceButtonAction(ActionEvent event) {
          try {
              // Charger le fichier FXML de la page AddService
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddService.fxml"));
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
      }*/
    @FXML
    private void handleNewServiceButtonAction(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AddService
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddService.fxml"));
            Parent root = loader.load();
            AddService controller = loader.getController();

            // Pass the logged-in admin data to the controller
            controller.setLoggedInAdmin(loggedInAdmin);
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir une référence à la scène actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();

            // Obtenir une référence à la fenêtre actuelle
            Stage currentStage = (Stage) currentScene.getWindow();

            // Fermer la fenêtre actuelle
            currentStage.close();

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
    private void  handleServiceresvButtonAction(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AddService
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backresevservice.fxml"));
            Parent root = loader.load();
            Backresevservice controller = loader.getController();

            // Pass the logged-in admin data to the controller
            controller.setLoggedInAdmin(loggedInAdmin);
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir une référence à la scène actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();

            // Obtenir une référence à la fenêtre actuelle
            Stage currentStage = (Stage) currentScene.getWindow();

            // Fermer la fenêtre actuelle
            currentStage.close();

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
    private void handleAddCategoryButtonAction(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AddService
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCategory.fxml"));
            Parent root = loader.load();
            AddCategory controller = loader.getController();

            // Pass the logged-in admin data to the controller
            controller.setLoggedInAdmin(loggedInAdmin);
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir une référence à la scène actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();

            // Obtenir une référence à la fenêtre actuelle
            Stage currentStage = (Stage) currentScene.getWindow();

            // Fermer la fenêtre actuelle
            currentStage.close();

            // Créer une nouvelle fenêtre pour afficher la scène
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Gérer les exceptions liées au chargement du fichier FXML
            e.printStackTrace();
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


}