package com.visita.controllers;
import com.visita.models.Admin;
import com.visita.models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import javafx.util.StringConverter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.Node;
import com.visita.models.Category;
import com.visita.models.Service;
import com.visita.services.ServiceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.TableCell;


public class AddService {

    @FXML
    private AnchorPane addService_form;

    @FXML
    private TableView<Service> TableView;

    @FXML
    private TextField addService_SEARCH;

    @FXML
    private CheckBox addService_ACTIVE;

    @FXML
    private ChoiceBox<Category> addService_CATEGORY;

    @FXML
    private DatePicker addService_DATE;

    @FXML
    private TextArea addService_DESCRIPTION;

    @FXML
    private ImageView addService_IMAGE;

    @FXML
    private TextField addService_NAME;

    @FXML
    private Button addService_addBtn;

    @FXML
    private Button addService_clearBtn;

    @FXML
    private TableColumn<Service, Date> addService_col_DATE;

    @FXML
    private TableColumn<Service, String> addService_col_CATEGORY;

    @FXML
    private TableColumn<Service, Boolean> addService_col_ACTIVE;

    @FXML
    private TableColumn<Service, String> addService_col_DESCRIPTION;

    @FXML
    private TableColumn<Service, Integer> addService_col_ID;

    @FXML
    private TableColumn<Service, String> addService_col_NAME;
    @FXML
    private TableColumn<Service, String> addService_col_IMAGE;


    @FXML
    private Button addService_deleteBtn;

    @FXML
    private Button addService_updateBtn;

    @FXML
    private Button export_EXL;
    @FXML
    private Button category_btn_nv;
    @FXML
    private Button service_btn_nv;

    @FXML
    private Button serviceresv_btn_nv;


    @FXML
    private Button event_btn;
    @FXML
    private Button CategoryeventPage;
    @FXML
    private AnchorPane main_form;
    private Admin loggedInAdmin;
    private ServiceService serviceService;
    ServiceService ps =  new ServiceService();

    private String imagePath= "C:\\Users\\lenovo\\CompleteProject\\public\\uploads\\"  ;

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
    public void initialize() {
        serviceService = new ServiceService();

        // Configurer la TableView pour permettre la sélection multiple
        TableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Configurer l'écouteur de changement de sélection pour la TableView
        TableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Vérifier si un nouveau service est sélectionné
            if (newValue != null) {
                // Remplir les champs de texte avec les propriétés du service sélectionné
                addService_NAME.setText(newValue.getNom());
                addService_DESCRIPTION.setText(newValue.getDescription());
                addService_DATE.setValue(newValue.getDateCreation().toLocalDate());
                addService_ACTIVE.setSelected(newValue.isActive());

                // Chargez l'image spécifique du service sélectionné
                String photoUrl = newValue.getImage();

                if (photoUrl != null && !photoUrl.isEmpty()) {
                    String imagePath = "C:\\Users\\lenovo\\CompleteProject\\public\\uploads\\" + photoUrl;
                    Image image = new Image("file:" + imagePath);
                    addService_IMAGE.setImage(image);
                } else {
                    addService_IMAGE.setImage(null);
                }

                // Définir la catégorie sélectionnée
                Category selectedCategory = findCategoryByName(newValue.getCategory_nom());
                addService_CATEGORY.setValue(selectedCategory);
            } else {
                // Effacer les champs de texte si aucun service n'est sélectionné
                clearFields();
            }
        });

        // Chargez les catégories pour le ChoiceBox
        loadCategories();

        // Configurez les colonnes de tableView
        addService_col_NAME.setCellValueFactory(new PropertyValueFactory<Service, String>("nom"));
        addService_col_DESCRIPTION.setCellValueFactory(new PropertyValueFactory<Service, String>("description"));
        addService_col_DATE.setCellValueFactory(new PropertyValueFactory<Service, Date>("dateCreation"));
        // Configurez la colonne pour afficher l'image
        addService_col_IMAGE.setCellValueFactory(new PropertyValueFactory<>("image"));
        // Configurez la colonne pour afficher l'image
        addService_col_IMAGE.setCellFactory(column -> {
            return new TableCell<Service, String>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitWidth(30);
                    imageView.setFitHeight(30);
                }

                @Override
                protected void updateItem(String photoUrl, boolean empty) {
                    super.updateItem(photoUrl, empty);
                    if (empty || photoUrl == null || photoUrl.isEmpty()) {
                        imageView.setImage(null);
                    } else {
                        String imagePath = "C:\\Users\\lenovo\\CompleteProject\\public\\uploads\\" + photoUrl;

                        Image image = new Image("file:" + imagePath);
                        imageView.setImage(image);
                    }
                    setGraphic(imageView);
                }
            };
        });


        addService_col_ACTIVE.setCellValueFactory(new PropertyValueFactory<Service, Boolean>("active"));
        addService_col_CATEGORY.setCellValueFactory(new PropertyValueFactory<Service, String>("category_nom"));

        // Chargez les services depuis la base de données
        refreshTableView();
    }

    // Méthode pour rafraîchir la TableView




    @FXML
    void close() {
        System.exit(0);
    }

    @FXML
    void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }


    private Category findCategoryByName(String categoryName) {
        // Obtenir la liste des catégories disponibles
        ObservableList<Category> categories = addService_CATEGORY.getItems();

        // Parcourir les catégories et trouver celle dont le nom correspond au nom fourni
        for (Category category : categories) {
            if (category.getNom().equals(categoryName)) {
                return category;
            }
        }

        // Retourner null si aucune catégorie ne correspond au nom fourni
        return null;
    }

    @FXML
    void ajouterService(ActionEvent event) {
        // Valider les données saisies
        if (!validateInput()) {
            // Si la validation échoue, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Données invalides");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs requis correctement.");
            alert.showAndWait();
            return; // Interrompre l'ajout si les données sont invalides
        }

        // Obtenir l'ID de la catégorie sélectionnée
        Category selectedCategory = addService_CATEGORY.getValue();
        int categoryId = selectedCategory != null ? selectedCategory.getId() : -1; // -1 si la catégorie n'est pas sélectionnée
        String imageName = getImageNameFromPath(imagePath);

        // Créer un nouveau Service à partir des données saisies
        Service newService = new Service(
                addService_NAME.getText(),
                addService_DESCRIPTION.getText(),
                Date.valueOf(addService_DATE.getValue()),
                imageName,
                categoryId, // Utilisez categoryId ici
                addService_ACTIVE.isSelected()
        );

        // Ajouter le nouveau service
        ps.ajouter(newService);

        // Afficher un message de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Service ajouté");
        alert.setHeaderText(null);
        alert.setContentText("Le service a été ajouté avec succès !");
        alert.showAndWait();

        // Rafraîchir la TableView pour afficher le nouveau service
        refreshTableView();

        // Effacer les champs de texte
        clearFields();
    }


    boolean validateInput() {
        boolean isValid = true;

        // Valider le champ de texte pour le nom (non vide et ne contenant que des lettres)
        String nom = addService_NAME.getText();
        if (nom.isEmpty()) {
            addService_NAME.setStyle("-fx-border-color: red;");
            isValid = false;
        } else if (!nom.matches("[a-zA-Z]+")) {
            // Si le nom contient autre chose que des lettres, affichez une erreur et marquez le champ en rouge
            addService_NAME.setStyle("-fx-border-color: red;");
            isValid = false;
            // Afficher un message d'erreur personnalisé
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nom invalide");
            alert.setHeaderText(null);
            alert.setContentText("Le nom ne doit contenir que des lettres.");
            alert.showAndWait();
        } else {
            // Si le nom est valide, réinitialisez le style du champ
            addService_NAME.setStyle(null);
        }

        // Valider le champ de texte pour la description (non vide)
        if (addService_DESCRIPTION.getText().isEmpty()) {
            addService_DESCRIPTION.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            addService_DESCRIPTION.setStyle(null);
        }

        // Valider le champ de choix pour la catégorie (non vide)
        if (addService_CATEGORY.getValue() == null) {
            addService_CATEGORY.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            addService_CATEGORY.setStyle(null);
        }

        // Valider le DatePicker pour la date (non vide et date >= date actuelle)
        LocalDate selectedDate = addService_DATE.getValue();
        LocalDate currentDate = LocalDate.now();
        if (selectedDate == null) {
            // Si la date n'est pas sélectionnée, affichez une erreur et marquez le champ en rouge
            addService_DATE.setStyle("-fx-border-color: red;");
            isValid = false;
        } else if (selectedDate.isBefore(currentDate)) {
            // Si la date sélectionnée est antérieure à la date actuelle, affichez une erreur et marquez le champ en rouge
            addService_DATE.setStyle("-fx-border-color: red;");
            isValid = false;
            // Afficher un message d'erreur personnalisé
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Date invalide");
            alert.setHeaderText(null);
            alert.setContentText("La date sélectionnée ne peut pas être antérieure à la date d'aujourd'hui.");
            alert.showAndWait();
        } else {
            // Si la date est valide, réinitialisez le style du champ
            addService_DATE.setStyle(null);
        }

        return isValid;
    }


    @FXML
    void supprimerService(ActionEvent event) {
        // Obtenir le service sélectionné dans la TableView
        Service selectedService = TableView.getSelectionModel().getSelectedItem();

        // Vérifier si un service est sélectionné
        if (selectedService != null) {
            // Demander confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer ce service ?");
            alert.setContentText("Service : " + selectedService.getNom() + "\n\nCette action est irréversible. Veuillez confirmer.");

            Optional<ButtonType> result = alert.showAndWait();

            // Si l'utilisateur confirme, supprimer le service
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer le service avec le service
                serviceService.supprimer(selectedService);
                refreshTableView(); // Rafraîchir la TableView

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Suppression réussie");
                successAlert.setHeaderText("Service supprimé !");
                successAlert.setContentText("Le service \"" + selectedService.getNom() + "\" a été supprimé avec succès.");
                successAlert.showAndWait();
            }
        } else {
            // Aucun service sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sélection manquante");
            alert.setHeaderText("Erreur de sélection");
            alert.setContentText("Il semble que vous n'ayez pas sélectionné de service. Veuillez en choisir un à supprimer.");
            alert.showAndWait();
        }
        // Effacer les champs de texte après la modification
        clearFields();
    }





    @FXML
    void modifierService(ActionEvent event) {
        // Obtenir le service actuellement sélectionné dans la TableView
        Service selectedService = TableView.getSelectionModel().getSelectedItem();

        // Vérifier si un service est sélectionné
        if (selectedService == null) {
            // Afficher un message d'erreur si aucun service n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aucun service sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un service à modifier.");
            alert.showAndWait();
            return;
        }

        // Valider les données saisies
        if (!validateInput()) {
            // Si la validation échoue, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Données invalides");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs requis correctement.");
            alert.showAndWait();
            return;
        }
        String newImageName = getImageNameFromPath(imagePath);
        // Mettre à jour les propriétés du service sélectionné avec les données modifiées
        selectedService.setNom(addService_NAME.getText());
        selectedService.setDescription(addService_DESCRIPTION.getText());
        selectedService.setDateCreation(Date.valueOf(addService_DATE.getValue()));
        selectedService.setActive(addService_ACTIVE.isSelected());

        // Si l'image a été mise à jour, assurez-vous de mettre à jour l'imagePath
        if (imagePath != null && !imagePath.isEmpty()) {
            selectedService.setImage(newImageName);
        }

        // Mettre à jour la catégorie si elle a été modifiée
        Category selectedCategory = addService_CATEGORY.getValue();
        if (selectedCategory != null) {
            selectedService.setCategory_id(selectedCategory.getId());
        }

        // Appeler la méthode de mise à jour du service
        ps.modifier(selectedService, newImageName);

        // Afficher un message de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Service modifié");
        alert.setHeaderText(null);
        alert.setContentText("Le service a été modifié avec succès !");
        alert.showAndWait();

        // Rafraîchir la TableView pour afficher les modifications
        refreshTableView();

        // Effacer les champs de texte après la modification
        clearFields();
    }
    private String getImageNameFromPath(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            return imageFile.getName();
        }
        return null;
    }

    @FXML
    void clearFields() {
        addService_NAME.setText("");
        addService_DESCRIPTION.setText("");
        addService_CATEGORY.setValue(null);
        addService_DATE.setValue(null);
        addService_ACTIVE.setSelected(false);
        addService_IMAGE.setImage(null);
        imagePath = null;
    }
    @FXML
    void clearFields(ActionEvent event) {
        addService_NAME.setText("");
        addService_DESCRIPTION.setText("");
        addService_CATEGORY.setValue(null);
        addService_DATE.setValue(null);
        addService_ACTIVE.setSelected(false);
        addService_IMAGE.setImage(null);
        imagePath = null;
    }

    @FXML
    void handleUploadImageClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        Stage stage = (Stage) main_form.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            URI fileUri = file.toURI();
            String uriString = fileUri.toString();
            Image image = new Image(uriString, true);
            // Ajuster la taille de l'ImageView
            addService_IMAGE.setFitWidth(106);
            addService_IMAGE.setFitHeight(148);
            addService_IMAGE.setImage(image);
            imagePath = uriString;
        }

    }

    // Méthode pour rafraîchir la TableView
    private void refreshTableView() {
        List<Service> services = ps.afficher();
        ObservableList<Service> observableList = FXCollections.observableList(services);
        TableView.setItems(observableList);
        // Force the TableView to refresh
        TableView.refresh();
    }



    private void loadCategories() {
        // Récupérer la liste des catégories depuis le service
        List<Category> categories = serviceService.getAllCategories();

        // Convertir la liste en ObservableList
        ObservableList<Category> observableCategories = FXCollections.observableArrayList(categories);

        // Assigner la liste observable au ChoiceBox
        addService_CATEGORY.setItems(observableCategories);

        // Définir un convertisseur de chaîne pour afficher les noms de catégorie
        addService_CATEGORY.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category == null ? "" : category.getNom();
            }

            @Override
            public Category fromString(String string) {
                // Si vous souhaitez permettre à l'utilisateur d'ajouter de nouvelles catégories en saisissant du texte,
                // vous pouvez ajouter une logique ici pour créer une nouvelle catégorie à partir de la chaîne de texte.
                // Sinon, vous pouvez simplement retourner null ici.
                return null;
            }
        });
    }



    @FXML
    void searchServices() {
        // Obtenir les critères de recherche de l'utilisateur
        String searchText = addService_SEARCH.getText().toLowerCase();

        // Récupérer tous les services depuis le service
        List<Service> allServices = serviceService.afficher(); // Récupérez tous les services

        // Filtrer les services en fonction des critères de recherche
        List<Service> filteredServices = new ArrayList<>();

        for (Service service : allServices) {
            // Vérifiez si le nom, la description, la date ou la catégorie du service correspondent aux critères de recherche
            boolean matchesName = service.getNom().toLowerCase().contains(searchText);
            boolean matchesDescription = service.getDescription().toLowerCase().contains(searchText);
            boolean matchesDate = service.getDateCreation() != null && service.getDateCreation().toString().contains(searchText);
            boolean matchesCategory = service.getCategory_nom().toLowerCase().contains(searchText);

            // Si l'un des attributs (sauf l'ID) correspond aux critères de recherche, ajoutez le service à la liste filtrée
            if (matchesName || matchesDescription || matchesDate || matchesCategory) {
                filteredServices.add(service);
            }
        }

        // Mettre à jour la TableView avec les services filtrés
        ObservableList<Service> observableList = FXCollections.observableList(filteredServices);
        TableView.setItems(observableList);
    }

    @FXML
    void exportToExcel() {
        // Utilisez FileChooser pour permettre à l'utilisateur de choisir l'emplacement du fichier Excel
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export to Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(new Stage());

        // Si un fichier est sélectionné
        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                // Créez une feuille de calcul dans le classeur
                Sheet sheet = workbook.createSheet("Services");

                // Créez une ligne d'en-tête dans la feuille
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Nom");
                headerRow.createCell(1).setCellValue("Description");
                headerRow.createCell(2).setCellValue("Date");
                headerRow.createCell(3).setCellValue("Catégorie");

                // Récupérez les services de votre TableView
                List<Service> servicesList = new ArrayList<>(TableView.getItems());

                // Remplissez la feuille de calcul avec les données des services
                for (int i = 0; i < servicesList.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    Service service = servicesList.get(i);

                    // Remplissez les cellules avec les données du service
                    row.createCell(0).setCellValue(service.getNom());
                    row.createCell(1).setCellValue(service.getDescription());
                    row.createCell(2).setCellValue(service.getDateCreation().toString());
                    row.createCell(3).setCellValue(service.getCategory_nom());
                }

                // Écrire le classeur dans le fichier
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    workbook.write(fileOutputStream);
                }

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Exportation réussie");
                successAlert.setHeaderText("Exportation Excel");
                successAlert.setContentText("Les données ont été exportées avec succès !");
                successAlert.showAndWait();
            } catch (IOException e) {
                // Gérer les exceptions d'E/S
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'exportation");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite lors de l'exportation des données.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleNewServiceButtonAction(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AddService
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddService.fxml"));
            Parent root = loader.load();

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

    @FXML
    void eventButtonAction(ActionEvent event) {

    }
    @FXML
    void CategoryeventButtonAction(ActionEvent event) {

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