package com.visita.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import com.visita.models.Patient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.visita.services.PatientService;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;

public class Signup {

    @FXML
    private AnchorPane main_form;
    @FXML
    private ImageView uploadedImageView;

    @FXML
    private TextField username;

    @FXML
    private TextField fullname;
    private String imagePath= "C:\\Users\\lenovo\\CompleteProject\\public\\uploads\\photos\\"  ;

    @FXML
    private Label SignupMessageLabel;

    @FXML
    private TextField email;

    @FXML
    private TextField phone;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmpassword;

    @FXML
    private Label confirmPasswordLabel;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private PatientService patientService = new PatientService();



    @FXML
    void handleSignupButtonClick(ActionEvent event) {
        try {
            // Get user input from text fields
            String enteredUsername = username.getText();
            String enteredFullname = fullname.getText();
            String enteredEmail = email.getText();
            String enteredPhone = phone.getText();

            String enteredPassword = password.getText();
            String confirmedPassword = confirmpassword.getText();

            // Check if any field is empty
            if (enteredUsername.isEmpty() || enteredFullname.isEmpty() || enteredEmail.isEmpty() || enteredPhone.isEmpty() || enteredPassword.isEmpty() || confirmedPassword.isEmpty()) {
                SignupMessageLabel.setText("Please fill in all fields!");
                return; // Exit the method if any field is empty
            }

            // Validate entered data
            if (enteredPassword.equals(confirmedPassword)) {
                if (Patient.isValidEmail(enteredEmail)) {
                    if (Patient.isValidPhoneNumber(enteredPhone)) {
                        if (Patient.isValidPassword(enteredPassword)) {
                            if (Patient.isValidFullname(enteredFullname)) {
                                //String defaultImagePath = "/values/default.png";
                                //String imagePath = (uploadedImageView.getImage() != null) ? uploadedImageView.getImage().getUrl() : defaultImagePath;

                                String imageName = getImageNameFromPath(imagePath);


                                String encodedPassword = passwordEncoder.encode(enteredPassword);

                                // Try to encode the password, and handle exceptions
                                if (encodedPassword != null && !encodedPassword.isEmpty()) {
                                    patientService.ajouter(new Patient(enteredUsername, enteredEmail, enteredPhone, encodedPassword, "1", imageName, new String[]{"user"}, enteredFullname));
                                    SignupMessageLabel.setText("User has been added successfully!");

                                    // Clear the text fields
                                    username.clear();
                                    fullname.clear();
                                    email.clear();
                                    phone.clear();
                                    password.clear();
                                    confirmpassword.clear();
                                    uploadedImageView.setImage(null);
                                } else {
                                    SignupMessageLabel.setText("Error encrypting password!");
                                }
                            } else {
                                SignupMessageLabel.setText("Please enter a valid fullname (letters only)!");
                            }
                        } else {
                            SignupMessageLabel.setText("Please enter a valid password (minimum length 6)!");
                        }
                    } else {
                        SignupMessageLabel.setText("Please enter a valid phone number (length 😎!");
                    }
                } else {
                    SignupMessageLabel.setText("Please enter a valid email address!");
                }
            } else {
                SignupMessageLabel.setText("Passwords do not match!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            SignupMessageLabel.setText("User has not been added !");
        }
    }
    private String getImageNameFromPath(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            return imageFile.getName();
        }
        return null;
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
            uploadedImageView.setImage(image);
            imagePath = uriString;
        }

    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @FXML
    void LoginPage(ActionEvent event) throws IOException {
        Next(event);
    }

    void Next(ActionEvent event) throws IOException {
        // Get the current scene's window
        Window window = ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();

        // Get the current stage
        Stage stage = (Stage) window;

        // Set the new scene
        stage.setScene(new Scene(root));
    }




    @FXML
    void handleDoctorSignupClick(ActionEvent event) throws IOException {
        SignupDoc(event);
    }


    private void SignupDoc(ActionEvent event) throws IOException{
        Window window = ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignupDoc.fxml"));
        Parent root = loader.load();

        // Get the current stage
        Stage stage = (Stage) window;

        // Set the new scene
        stage.setScene(new Scene(root));
    }
 /* @FXML
    void handleSignupButtonClick(ActionEvent event) {
        try {
            // Get user input from text fields
            String enteredUsername = username.getText();
            String enteredFullname = fullname.getText();
            String enteredEmail = email.getText();
            String enteredPhone = phone.getText();
            String defaultImagePath = "values/default.png";
            String imagePath = (uploadedImageView.getImage() != null) ? uploadedImageView.getImage().getUrl() : defaultImagePath;

            String enteredPassword = password.getText();
            if (password.getText().equals(confirmpassword.getText())) {
                SignupMessageLabel.setText("Passwords match");


                // Call the add function in the service class
                patientService.ajouter(new Patient(enteredUsername, enteredEmail, enteredPhone, enteredPassword, "1", imagePath, "patient", enteredFullname));

            /* Show a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Patient added successfully!");
            alert.showAndWait();

                SignupMessageLabel.setText("User has been added successfully!");

                // Clear the text fields
                username.clear();
                fullname.clear();
                email.clear();
                phone.clear();
                password.clear();
                confirmpassword.clear();
                uploadedImageView.clear();
            } else {
                SignupMessageLabel.setText("Passwords do not match!");
            }
        } catch (Exception e) {
             Show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error while adding patient: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
            SignupMessageLabel.setText("User has not been added !");
        }

    }*/

}