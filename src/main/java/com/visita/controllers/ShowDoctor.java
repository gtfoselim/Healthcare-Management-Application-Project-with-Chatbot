package com.visita.controllers;


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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import com.visita.models.Doctor;
import com.visita.models.Patient;
import com.visita.models.User;
import com.visita.services.DoctorService;
import com.visita.services.PatientService;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShowDoctor  implements Initializable {
    @FXML
    private AnchorPane root;
    private int userId;
    @FXML
    private VBox doctorListVBox;

    @FXML
    private Label DoctorFullname;
    @FXML
    private Label DoctorSpecialite;
    @FXML
    private TextField searchtext;
    @FXML
    private Label DoctorAdress;
    @FXML
    private Label DoctorPhone;
    @FXML
    private ImageView doctorImageView;
    @FXML
    private VBox doctorCardsContainer;
    private Patient loggedInPatient;
    private DoctorService doctorService = new DoctorService();
    private PatientService patientService = new PatientService();

    public ShowDoctor() {
        // Create PatientService with DataSource instance
        this.patientService = new PatientService();
    }
    public void setLoggedInPatient(Patient patient) {
        this.loggedInPatient = patient;
        // After setting the patient, initialize the labels
loadDoctors();
    }

    public void initData(User user) {
        // Check if the provided user is an instance of Patient
        if (user instanceof Patient) {
            // If the user is a Patient, cast it to Patient and initialize labels
            Patient patient = (Patient) user;
            loadDoctors();
        } else {
            // Handle other types of users or show an error message
            showAlert("Error", "Invalid user type");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Check if the loggedInPatient object is not null
        if (loggedInPatient != null) {
            // Initialize the view with patient data
            loadDoctors();
        } else {
            // Display an error message if the loggedInPatient object is null

        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadDoctors() {
        try {
            List<Doctor> doctors = doctorService.afficher();

            // Check if doctors are retrieved successfully
            if (doctors != null && !doctors.isEmpty()) {
                int doctorsPerRow = 3; // Number of doctors per row
                int rowCount = (int) Math.ceil((double) doctors.size() / doctorsPerRow);

                for (int i = 0; i < rowCount; i++) {
                    HBox row = new HBox(10); // Spacing between doctors
                    row.getStyleClass().add("doctor-row"); // Add a style class for styling

                    // Add doctors to the row
                    int startIndex = i * doctorsPerRow;
                    int endIndex = Math.min((i + 1) * doctorsPerRow, doctors.size());
                    for (int j = startIndex; j < endIndex; j++) {
                        Doctor doctor = doctors.get(j);
                        VBox doctorCard = createDoctorCard(doctor);
                        row.getChildren().add(doctorCard);
                    }

                    // Add the row to the doctorListVBox
                    doctorListVBox.getChildren().add(row);
                }
            } else {
                showAlert("Error", "No doctors found in the database.");
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to retrieve doctors from the database: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private VBox createDoctorCard(Doctor doctor) {

        VBox card = new VBox();
        card.getStyleClass().add("service-card");

        HBox.setMargin(card, new Insets(8, 20, 10, 8));
        // Add styling for spacing and background color
        card.setPadding(new Insets(20));
        Color customColor = Color.rgb(186, 234, 195, 0.99);
        card.setPadding(new Insets(10));
        card.setBackground(new Background(new BackgroundFill(customColor, new CornerRadii(20), Insets.EMPTY)));

        card.setPrefWidth(200);
        card.setPrefHeight(220);


        card.setAlignment(Pos.CENTER);
        card.setOnMouseClicked(event -> {
            showDoctorInfo(doctor);
            applyAnimation(card);
        });

        // Create an ImageView for the doctor's image
        ImageView imageView = new ImageView();
        // Set the doctor's image
        String photoUrl = doctor.getPhoto();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            // Assuming the photos are stored in a specific directory, construct the full file path
            String imagePath = "C:\\Users\\lenovo\\CompleteProject\\public\\uploads\\photos\\" + photoUrl;
            // Create an Image object using the file URL
            Image image = new Image("file:" + imagePath);
            imageView.setImage(image);
        } else {
            // If no image URL is provided, you can set a default image here
            // For example: imageView.setImage(new Image("file:/path/to/default_image.png"));
        }
        imageView.setFitWidth(85); // Adjust the width of the image
        imageView.setFitHeight(85); // Adjust the height of the image


        // Populate the card with doctor information
        Label nameLabel = new Label("Name: " + doctor.getFullname());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
        nameLabel.setWrapText(true);
        nameLabel.setPadding(new Insets(10, 0, 0, 0));

        Label specialtyLabel = new Label("Specialty: " + doctor.getSpecialite());
        specialtyLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
        specialtyLabel.setWrapText(true);
        specialtyLabel.setPadding(new Insets(10, 0, 0, 0));
        // Add more labels for other information if needed

        card.getChildren().addAll(imageView, nameLabel, specialtyLabel);

        return card;
    }

    private void showDoctorInfo(Doctor doctor) {
        // Implement logic to display the selected doctor's information in the big card
        // Update the labels in the big card with the information of the selected doctor
        DoctorFullname.setText( doctor.getFullname());
        DoctorSpecialite.setText( doctor.getSpecialite());
        DoctorAdress.setText( doctor.getAddress());
        DoctorPhone.setText( doctor.getPhone());

        // Load and set the image
        String photoUrl = doctor.getPhoto();

        if (photoUrl != null && !photoUrl.isEmpty()) {
            String imagePath = "C:\\Users\\lenovo\\CompleteProject\\public\\uploads\\photos\\" + photoUrl;
            Image image = new Image("file:" + imagePath);
            doctorImageView.setImage(image);
        } else {
            doctorImageView.setImage(null);
        }
    }

    @FXML
    void searchDoctors(String searchText) {
        try {
            List<Doctor> allDoctors = doctorService.afficher();

            // Clear existing doctor cards
            doctorListVBox.getChildren().clear();

            // Display filtered doctors if search text is not empty
            if (!searchText.isEmpty()) {
                List<Doctor> filteredDoctors = new ArrayList<>();

                // Filter doctors based on search text
                for (Doctor doctor : allDoctors) {
                    if (doctor.getFullname().toLowerCase().contains(searchText.toLowerCase()) ||
                            doctor.getSpecialite().toLowerCase().contains(searchText.toLowerCase())) {
                        filteredDoctors.add(doctor);
                    }
                }

                displayDoctors(filteredDoctors);
            } else {
                // Display all doctors if search text is empty
                displayDoctors(allDoctors);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to retrieve doctors from the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayDoctors(List<Doctor> doctors) {
        // Display the filtered doctors
        for (Doctor doctor : doctors) {
            VBox doctorCard = createDoctorCard(doctor);
            doctorListVBox.getChildren().add(doctorCard);
        }
    }



    private void applyAnimation(VBox card) {
        // If another card is selected, reset its state
        if (doctorListVBox != null && doctorListVBox != card) {
            resetAnimation(doctorListVBox);
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
        doctorListVBox = card;
    }

    private void resetAnimation(VBox card) {
        // Reset the scaling animation for the specified card
        card.setScaleX(1.0);
        card.setScaleY(1.0);
    }
    @FXML
    private void RedirectToReservation(ActionEvent event) {
        redirectToAffcherresController(event, loggedInPatient);
    }


    private void redirectToAffcherresController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            Front afficherpostController = loader.getController();

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


}