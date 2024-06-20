package com.visita.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class processPayment
{

    @FXML
    private Button btnclose;
    @FXML
    private Button btnpayer;




    @FXML
    private TextField bankid;




    @FXML
    private TextField idcard;

    @FXML
    private TextField postalid;

    @FXML
    private TextField securityid;
    @FXML
    void minimize(ActionEvent event) {

    }
    /*
    public static void main(String[] args) {
        // Instancier la classe pour appeler la méthode de traitement de paiement
        processPayment paymentProcessor = new processPayment();

        // Appeler la méthode de traitement de paiement
        paymentProcessor.processPayment();
    }

     */
    @FXML
    private void processPayment(ActionEvent event) {

        // Vérifier si les champs securityid et idcard sont valides
        String securityId = securityid.getText();
        String bI = bankid.getText();

        if (!isValidSecurityId(securityId) || !isValidIdCard(bI)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Informations de carte invalides", "Veuillez vérifier les informations de votre carte.");
            return;
        }
        try {
// Set your secret key here
            Stripe.apiKey ="sk_test_51PBEhAJ8wVFBGMuI9cID3FZiOxrdb0LHvJp5iimPTRYzfLY3ea4OA7RdS9txTvBC7B55Wm0TCyHyZ5GbzcsaobYQ00HGioMKCF";

// Create a PaymentIntent with other payment details
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(1000L) // Amount in cents (e.g., $10.00)
                    .setCurrency("usd")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

// If the payment was successful, display a success message

            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());
            showAlert(Alert.AlertType.INFORMATION, "SUCCES", "Payement reussi !", "");
        } catch (StripeException e) {
// If there was an error processing the payment, display the error message
            showAlert(Alert.AlertType.ERROR, "Failer", "Payement non reussi !", "");
            System.out.println("Payment failed. Error: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    void close(ActionEvent event) throws IOException {
        back1();

    }
    void  back1() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/front.fxml"));
        Parent root = loader.load();

        // Utilisez le bouton btnback pour obtenir sa scène parente
        Scene currentScene = btnclose.getScene();

        // Remplacez la racine de la scène actuelle par la racine de la nouvelle vue chargée
        currentScene.setRoot(root);


    }
    private boolean isValidSecurityId(String securityId) {
        // Vérifier si le champ securityid contient exactement 4 chiffres
        return securityId.matches("\\d{4}");
    }

    private boolean isValidIdCard(String bankid) {
        // Vérifier si le champ idcard contient exactement 8 chiffres
        return bankid.matches("\\d{8}");
    }
}


