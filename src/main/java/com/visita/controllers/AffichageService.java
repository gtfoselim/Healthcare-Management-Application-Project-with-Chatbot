package com.visita.controllers;
import com.visita.models.Doctor;
import com.visita.models.Patient;
import com.visita.models.Service;
import com.visita.models.User;
import com.visita.services.ServiceService;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
public class AffichageService implements Initializable {

    @FXML
    private ImageView ServiceImg;

    @FXML
    private VBox ServiceListVBox;

    @FXML
    private Label Service_name;

    @FXML
    private Button addreserv_btn;

    @FXML
    private Label category_service;

    @FXML
    private VBox chosenserviceCard;

    @FXML
    private Button logoutButton;

    @FXML
    private TextArea service_description;

    @FXML
    private TextField shearch_service;
    private Doctor loggedInDoctor;

    @FXML
    private Label pageNumberLabel;
    private int currentPage = 0;
    private int cardsPerPage = 6;

    @FXML
    private Button prevPageButton;

    @FXML
    private Button nextPageButton;

    ServiceService ps = new ServiceService();
    List<Service> services = ps.afficher();

    private List<VBox> serviceCardList = new ArrayList<>(); // Declare serviceCardList as a field

    /////////////////////reservation://///////////////
    private int selectedServiceIndex = -1; // Variable pour stocker l'indice du service sélectionné


/////////

    public void setLoggedInDoctor(Doctor doctor) {
        this.loggedInDoctor = doctor;
        // After setting the patient, initialize the labels

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ps = new ServiceService();
        loadServices();
        // Ajoutez un écouteur d'événements pour le champ de recherche
        shearch_service.setOnKeyReleased(event -> searchServices());
        // Ajoutez des gestionnaires d'événements pour les boutons de pagination
        prevPageButton.setOnAction(event -> {
            if (currentPage > 0) {
                currentPage--;
                loadPage(currentPage);
                updatePageNumberLabel(); // Mettre à jour le numéro de page
            }
        });


        nextPageButton.setOnAction(event -> {
            int maxPage = (int) Math.ceil((double) serviceCardList.size() / cardsPerPage) - 1;
            if (currentPage < maxPage) {
                currentPage++;
                loadPage(currentPage);
                updatePageNumberLabel(); // Mettre à jour le numéro de page
            }
        });

        // Charger la première page au démarrage
        loadPage(0);
        updatePageNumberLabel(); // Mettre à jour le numéro de page initial
        refreshDisplay();
    }

    private void updatePageNumberLabel() {
        pageNumberLabel.setText(String.valueOf(currentPage + 1)); // La première page est 0, donc ajoutez 1

    }


    private void loadPage(int page) {
        // Effacer les cartes de service actuellement affichées
        ServiceListVBox.getChildren().clear();

        // Calculer la plage de cartes à afficher pour la page actuelle
        int startIndex = page * cardsPerPage;
        int endIndex = Math.min((page + 1) * cardsPerPage, serviceCardList.size());

        // Afficher les cartes de service pour la page actuelle
        List<VBox> pageCards = serviceCardList.subList(startIndex, endIndex);
        displayServiceCards(pageCards);
    }


    private List<VBox> allServiceCards = new ArrayList<>();

    private void loadServices() {
        List<Service> services = ps.afficherActiveServices(); // Modifier afficherActiveServices() en fonction qui récupère uniquement les services actifs

        // Clear existing service cards and the list
        ServiceListVBox.getChildren().clear();
        serviceCardList.clear();

        // Check if services are retrieved successfully
        if (services != null && !services.isEmpty()) {
            for (int i = 0; i < services.size(); i++) {
                Service service = services.get(i);
                VBox serviceCard = createServiceCard(service); // Pass the index along with the service
                serviceCardList.add(serviceCard); // Add the created service card to the list
            }
            displayServiceCards(serviceCardList); // Display all service cards
        } else {
            showAlert("Error", "No active services found in the database.");
        }
    }


    private final int cardsPerRow = 3; // Nombre de cartes par ligne

    private void displayServiceCards(List<VBox> cards) {
        int rowCount = (int) Math.ceil((double) cards.size() / cardsPerRow);

        for (int i = 0; i < rowCount; i++) {
            HBox row = new HBox(10); // Espacement entre les cartes de service
            row.getStyleClass().add("Service-row"); // Ajouter une classe de style pour le style

            // Ajouter les cartes de service à la rangée
            int startIndex = i * cardsPerRow;
            int endIndex = Math.min((i + 1) * cardsPerRow, cards.size());
            for (int j = startIndex; j < endIndex; j++) {
                VBox serviceCard = cards.get(j);
                row.getChildren().add(serviceCard);
            }

            // Ajouter la rangée au VBox principal
            ServiceListVBox.getChildren().add(row);
        }
    }


    // Define a global variable to store the currently selected card
    private VBox selectedCard = null;

    private VBox createServiceCard(Service service) {
        VBox card = new VBox();
        card.getStyleClass().add("service-card");
        Color customColor = Color.rgb(186, 234, 195, 0.99);
        card.setPadding(new Insets(10));
        card.setBackground(new Background(new BackgroundFill(customColor, new CornerRadii(20), Insets.EMPTY)));

        card.setPrefWidth(200);
        card.setPrefHeight(220);
        card.setAlignment(Pos.CENTER);
        card.setOnMouseClicked(event -> {
            // Récupérer les données du service associé à cette carte
            Service selectedService = (Service) card.getUserData();
            System.out.println("Clicked on service card with ID: " + selectedService.getId());
            showServiceInfo(selectedService);
            applyAnimation(card);
        });
        card.setEffect(new DropShadow(10, Color.BLACK));

        // Stocker les données du service dans les métadonnées de la carte
        card.setUserData(service);

        ImageView imageView = new ImageView();

        String imageUrl = service.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String imagePath = "C:\\\\Users\\\\lenovo\\\\CompleteProject\\\\public\\\\uploads\\" + imageUrl;
            imageView.setImage(new Image("file:"+ imagePath));
        } else {
            imageView.setImage(new Image("default_image_icon.png"));
        }
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);

        Label nameLabel = new Label("Name: " + service.getNom());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
        nameLabel.setWrapText(true);
        nameLabel.setPadding(new Insets(10, 0, 0, 0));

        Label categoryLabel = new Label("Category: " + service.getCategory_nom());
        categoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
        categoryLabel.setWrapText(true);
        categoryLabel.setPadding(new Insets(5, 0, 0, 0));

        card.getChildren().addAll(imageView, nameLabel, categoryLabel);

        return card;
    }


    private void applyAnimation(VBox card) {
        // If another card is selected, reset its state
        if (selectedCard != null && selectedCard != card) {
            resetAnimation(selectedCard);
        }

        // Create a scaling animation
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), card);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();

        // Set the currently selected card to the clicked card
        selectedCard = card;
    }

    private void resetAnimation(VBox card) {
        // Reset the scaling animation for the specified card
        card.setScaleX(1.0);
        card.setScaleY(1.0);
    }


    private void showServiceInfo(Service service) {
        // Implement logic to display the selected service's information in the big card
        // Update the labels in the big card with the information of the selected service
        Service_name.setText(service.getNom());
        category_service.setText(service.getCategory_nom());
        service_description.setText(service.getDescription());

        // Load and set the image
        String imageUrl = service.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String imagePath = "C:\\\\Users\\\\lenovo\\\\CompleteProject\\\\public\\\\uploads\\" + imageUrl;

            Image image = new Image("file:"+imagePath);
            ServiceImg.setImage(image);
        } else {
            // If no image URL is provided, you can set a default image here
            // For example: ServiceImg.setImage(new Image("default_image_url"));
        }

        // Update selectedServiceIndex
        selectedServiceIndex = services.indexOf(service);
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void searchServices() {
        String searchText = shearch_service.getText().toLowerCase();
        try {
            // Récupérer tous les services
            List<Service> allServices = ps.afficherActiveServices();

            // Effacer les services actuellement affichés
            ServiceListVBox.getChildren().clear();
            serviceCardList.clear();

            // Filtrer les services en fonction du texte de recherche
            List<Service> filteredServices = new ArrayList<>();
            for (Service service : allServices) {
                if (service.getNom().toLowerCase().contains(searchText)) {
                    filteredServices.add(service);
                }
            }

            // Créer des cartes pour les services filtrés et les ajouter à la liste
            for (int i = 0; i < filteredServices.size(); i++) {
                VBox serviceCard = createServiceCard(filteredServices.get(i)); // Passer l'indice de l'élément
                serviceCardList.add(serviceCard);
            }

            // Afficher les cartes de service en conservant trois cartes par ligne
            displayServiceCards(serviceCardList);
        } catch (Exception e) {
            showAlert("Error", "PAS DE SERVICE POR LE MOMMENT DANS CETTE PAGE  " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void refreshDisplay() {
        try {
            loadServices(); // Recharge les services à partir de la base de données
            // Met à jour la pagination et le numéro de page actuel
            loadPage(currentPage);
            updatePageNumberLabel();
        } catch (Exception e) {
            showAlert("Error", "Failed to refresh display: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefreshButton(ActionEvent event) {

        refreshDisplay();
    }

    @FXML
    private void handleReservationButton(ActionEvent event) {
        // Récupérer les données du service associé à la carte sélectionnée
        Service selectedService = (Service) selectedCard.getUserData();
        if (selectedService != null) {
            try {
                // Charger la nouvelle fenêtre de réservation
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResevationService.fxml"));
                Parent root = loader.load();

                // Passez les informations du service sélectionné au contrôleur de la nouvelle fenêtre
                ResevationService controller = loader.getController();
                controller.initData(selectedService);

                // Créer une nouvelle scène et l'afficher dans une nouvelle fenêtre
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Please select a service before making a reservation.");
        }
    }


    @FXML
    private void handleNewServiceButtonAction(ActionEvent event) {
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
        redirectToAffcherPostController(event);
    }


    private void redirectToAffcherPostController(ActionEvent event) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/imc.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller


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



}













