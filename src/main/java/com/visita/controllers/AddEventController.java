package com.visita.controllers;

import com.visita.models.Admin;
import com.visita.models.CategoryEvent;
import com.visita.models.Evenement;
import com.visita.models.User;
import com.visita.services.ServiceCategoryEvent;
import com.visita.services.ServiceEvenement;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class AddEventController {

    @FXML
    private TableView<Evenement> TableView;

    @FXML
    private TextField addCategory_search;

    @FXML
    private ChoiceBox<String> addEvent_CATEGORY;

    @FXML
    private DatePicker addEvent_DATE;

    @FXML
    private DatePicker addEvent_DATE1;

    @FXML
    private ImageView addEvent_IMAGE;

    @FXML
    private TextField addEvent_NAME;

    @FXML
    private Button addEvent_addBtn;

    @FXML
    private Button addEvent_clearBtn;

    @FXML
    private TableColumn<Evenement, String> addEvent_col_;

    @FXML
    private TableColumn<Evenement, String> addEvent_col_CATEGORY1;

    @FXML
    private TableColumn<Evenement, String> addEvent_col_DATEdebut;

    @FXML
    private TableColumn<Evenement, String> addEvent_col_DATEfin;

    @FXML
    private TableColumn<Evenement, String>  addEvent_col_Lieu;

    @FXML
    private TableColumn<Evenement, String>  addEvent_col_nom;

    @FXML
    private TableColumn<Evenement, String>  addEvent_col_type;

    @FXML
    private Button addEvent_deleteBtn;

    @FXML
    private TextField addEvent_lieu;

    @FXML
    private TextField addEvent_nbrParticipant;

    @FXML
    private TextField addEvent_type;

    @FXML
    private Button addEvent_updateBtn;

    @FXML
    private AnchorPane addService_form;

    @FXML
    private AnchorPane main_form;

    private String imagePath;
    private Evenement evenement = new Evenement();
    private ServiceCategoryEvent serviceCategoryEvent = new ServiceCategoryEvent();

    @FXML
    private TextField addEvent_LOCATION;

    @FXML
    private DatePicker addEvent_START_DATE;

    @FXML
    private DatePicker addEvent_END_DATE;

    @FXML
    private TextField addEvent_TYPE;

    @FXML
    private TextField addEvent_PARTICIPANTS;


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



    private ServiceEvenement serviceEvenement;

    @FXML
    void initialize() {
        addEvent_col_type.setCellValueFactory(new PropertyValueFactory<>("type_evenement"));
        addEvent_col_nom.setCellValueFactory(new PropertyValueFactory<>("nom_evenement"));
        addEvent_col_Lieu.setCellValueFactory(new PropertyValueFactory<>("lieu_evenement"));
        addEvent_col_DATEdebut.setCellValueFactory(new PropertyValueFactory<>("date_debut"));
        addEvent_col_DATEfin.setCellValueFactory(new PropertyValueFactory<>("date_fin"));
        addEvent_col_.setCellValueFactory(new PropertyValueFactory<>("nb_participants"));
        addEvent_col_CATEGORY1.setCellValueFactory(new PropertyValueFactory<>("category_id"));
        serviceEvenement = new ServiceEvenement(); // Initialize serviceEvenement

        // Chargez les données depuis votre source de données
        loadCategories();
        // Load event data
        loadEventData();

        TableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Enable delete button when a row is selected
                addEvent_deleteBtn.setDisable(false);
                // Update the text fields with the selected event's data
                Evenement selectedEvent = TableView.getSelectionModel().getSelectedItem();
                addEvent_NAME.setText(selectedEvent.getNom_evenement());
                addEvent_lieu.setText(selectedEvent.getLieu_evenement());
                addEvent_DATE.setValue(selectedEvent.getDate_debut().toLocalDate());
                addEvent_DATE1.setValue(selectedEvent.getDate_fin().toLocalDate());
                addEvent_type.setText(selectedEvent.getType_evenement());
                addEvent_nbrParticipant.setText(String.valueOf(selectedEvent.getNb_participants()));
                // You may need to set the category in the choice box based on the selected event's category ID
                // addEvent_CATEGORY.setValue(selectedEvent.getCategory());
            } else {
                // Disable delete button when no row is selected
                addEvent_deleteBtn.setDisable(true);
                // Clear the text fields when no row is selected
                clearFields();
            }
        });
    }

    private void loadEventData() {
        try {
            // Obtenez les données depuis votre source de données
            ServiceEvenement serviceEvenement = new ServiceEvenement();
            List<Evenement> events = serviceEvenement.afficher();

            // Créez une liste observable des données à afficher dans le TableView
            ObservableList<Evenement> observableEvents = FXCollections.observableArrayList(events);
            TableView.setItems(observableEvents);
        } catch (SQLException e) {
            // Gérez les erreurs de chargement des données
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterEvent(ActionEvent event) {
        if (!validateInput()) {
            showAlert(Alert.AlertType.ERROR, "Données invalides", "Veuillez remplir tous les champs requis correctement.");
            return;
        }

        try {
            String categoryName = addEvent_CATEGORY.getValue();
            CategoryEvent category = serviceCategoryEvent.getCategoryByName(categoryName);
            if (category == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La catégorie sélectionnée n'existe pas.");
                return;
            }

            Evenement newEvent = new Evenement(
                    addEvent_NAME.getText(),
                    imagePath,
                    addEvent_lieu.getText(),
                    LocalDateTime.of(addEvent_DATE.getValue(), LocalDateTime.MIN.toLocalTime()),
                    LocalDateTime.of(addEvent_DATE1.getValue(), LocalDateTime.MIN.toLocalTime()),
                    addEvent_type.getText(),
                    Integer.parseInt(addEvent_nbrParticipant.getText()),
                    category.getId()
            );

            serviceEvenement.ajouter(newEvent);

            showAlert(Alert.AlertType.INFORMATION, "Service ajouté", "Le service a été ajouté avec succès!");
            clearFields();
            loadEventData(); // Reload event data after adding a new event

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout du service : " + e.getMessage());
        }
    }

    /*@FXML
    void handleUploadImageClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        Stage stage = (Stage) main_form.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            addEvent_IMAGE.setImage(image);
            imagePath = file.getAbsolutePath(); // Store the file path
        }
    }*/

    @FXML
    void handleUploadImageClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        Stage stage = (Stage) main_form.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            addEvent_IMAGE.setImage(image);
            imagePath = file.getName();
        }
    }

    private void loadCategories() {
        try {
            List<CategoryEvent> categories = serviceCategoryEvent.loadCategories();
            ObservableList<String> observableCategories = FXCollections.observableArrayList();
            for (CategoryEvent category : categories) {
                observableCategories.add(category.getName()); // Add only the category name to the ChoiceBox
            }
            addEvent_CATEGORY.setItems(observableCategories);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading categories: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        if (!isNumeric(addEvent_nbrParticipant.getText())) {
            showAlert(Alert.AlertType.ERROR, "Données invalides", "Le nombre de participants doit être un nombre entier.");
            return false;
        }
        if (addEvent_NAME.getText().length() < 4) {
            showAlert(Alert.AlertType.ERROR, "Données invalides", "Le nom doit contenir au moins 4 caractères.");
            return false;
        }
        if (addEvent_type.getText().length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Données invalides", "Le type doit contenir au moins 5 caractères.");
            return false;
        }
        if (addEvent_lieu.getText().length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Données invalides", "Le lieu doit contenir au moins 5 caractères.");
            return false;
        }
        if (addEvent_DATE.getValue() == null || addEvent_DATE1.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Données invalides", "Veuillez sélectionner les dates de début et de fin.");
            return false;
        }
        if (addEvent_DATE.getValue().isAfter(addEvent_DATE1.getValue())) {
            showAlert(Alert.AlertType.ERROR, "Données invalides", "La date de fin doit être après la date de début.");
            return false;
        }
        return true;
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            int n = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void clearFields() {
        addEvent_NAME.clear();
        addEvent_lieu.clear();
        addEvent_DATE.setValue(null);
        addEvent_DATE1.setValue(null);
        addEvent_type.clear();
        addEvent_nbrParticipant.clear();
        addEvent_CATEGORY.getSelectionModel().clearSelection();
        addEvent_IMAGE.setImage(null);
        imagePath = null;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

    @FXML
    void pdffunction(ActionEvent event) {
        convertToPDF();
    }

    private void convertToPDF() {
        Document document = new Document();
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
            Stage stage = (Stage) main_form.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                PdfPTable pdfTable = new PdfPTable(6); // Number of columns


                // Add date on top right
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String formattedDate = now.format(formatter);
                Paragraph dateParagraph = new Paragraph(formattedDate, FontFactory.getFont(FontFactory.HELVETICA, 10));
                dateParagraph.setAlignment(Element.ALIGN_RIGHT);
                document.add(dateParagraph);

                // Add title above the table
                Paragraph title = new Paragraph("Liste des événements", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                // Add space between title and table
                Paragraph space = new Paragraph("\n");
                document.add(space);

                // Add column headers to the PDF table
                pdfTable.addCell("Nom");
                pdfTable.addCell("Lieu");
                pdfTable.addCell("Date début");
                pdfTable.addCell("Date fin");
                pdfTable.addCell("Type");
                pdfTable.addCell("Participants");

                // Add event data to the PDF table
                ObservableList<Evenement> events = TableView.getItems();
                for (Evenement event : events) {
                    pdfTable.addCell(event.getNom_evenement());
                    pdfTable.addCell(event.getLieu_evenement());
                    pdfTable.addCell(event.getDate_debut().toString());
                    pdfTable.addCell(event.getDate_fin().toString());
                    pdfTable.addCell(event.getType_evenement());
                    pdfTable.addCell(String.valueOf(event.getNb_participants()));
                }

                document.add(pdfTable);
                document.close();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Le TableView a été converti en PDF avec succès !");
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la conversion en PDF : " + e.getMessage());
        }
    }

    @FXML
    void clear(ActionEvent event) {
        System.out.println("DELETE BTN clicked");
        clearFields();
    }

    @FXML
    void supprimerC(ActionEvent event) {
        System.out.println("DELETE BTN clicked");
        Evenement selectedEvent = TableView.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(Alert.AlertType.ERROR, "Sélectionnez un événement", "Veuillez sélectionner un événement à supprimer.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Use ServiceEvenement to delete the event
                serviceEvenement.supprimer(selectedEvent);
                showAlert(Alert.AlertType.INFORMATION, "Événement supprimé", "L'événement a été supprimé avec succès !");
                loadEventData(); // Reload event data after deletion
                clearFields(); // Clear the fields after deletion
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression de l'événement : " + e.getMessage());
            }
        }
    }

    @FXML
    void updateEvent(ActionEvent event) {
        if (!validateInput()) {
            showAlert(Alert.AlertType.ERROR, "Données invalides", "Veuillez remplir tous les champs requis correctement.");
            return;
        }

        Evenement selectedEvent = TableView.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(Alert.AlertType.ERROR, "Sélectionnez un événement", "Veuillez sélectionner un événement à modifier.");
            return;
        }

        Evenement updatedEvent = new Evenement(
                addEvent_NAME.getText(),
                imagePath,
                addEvent_lieu.getText(),
                LocalDateTime.of(addEvent_DATE.getValue(), LocalDateTime.MIN.toLocalTime()),
                LocalDateTime.of(addEvent_DATE1.getValue(), LocalDateTime.MIN.toLocalTime()),
                addEvent_type.getText(),
                Integer.parseInt(addEvent_nbrParticipant.getText()),
                selectedEvent.getCategory_id()
        );
        updatedEvent.setId(selectedEvent.getId());

        try {
            serviceEvenement.modifier(updatedEvent);
            showAlert(Alert.AlertType.INFORMATION, "Événement mis à jour", "L'événement a été mis à jour avec succès!");
            loadEventData(); // Reload event data after update
            clearFields(); // Clear the fields after update
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la mise à jour de l'événement : " + e.getMessage());
        }
    }
    @FXML
    void categorypage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCategoryevent.fxml"));
            Parent root = loader.load();
            AddCategoryController controller = loader.getController();

            // Pass the logged-in admin data to the controller
            controller.setLoggedInAdmin(loggedInAdmin);
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            // Clear the children of the addCategoryEvenement_form AnchorPane

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the category page: " + e.getMessage());
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

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            // Clear the children of the addCategoryEvenement_form AnchorPane

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
