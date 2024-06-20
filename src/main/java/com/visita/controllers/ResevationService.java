package com.visita.controllers;
import com.visita.models.Patient;
import com.visita.models.ReservationService;
import com.visita.models.User;
import com.visita.services.ReservationSrvService;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.visita.models.Service;
import com.visita.services.ServiceService;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Properties;


public class ResevationService {
    @FXML
    private Label MessageLabel;

    @FXML
    private Button Reserver;

    @FXML
    private ImageView ServiceIMG;

    @FXML
    private TextField Service_Name;

    @FXML
    private TextField User_Email_resv;

    @FXML
    private TextField user_NAME_res;

    @FXML
    private TextField SERVICE_ID_res1;
    @FXML
    private Label messageLabel; // Déclaration de messageLabel

    private int serviceId;
    private String serviceName;
    private String serviceImageURL;
    private ServiceService serviceService;
    private ReservationSrvService reservationService;
    private Service selectedService; // Le service sélectionné

    public ResevationService() {
        reservationService = new ReservationSrvService();
    }


    // Méthode pour initialiser les données du service sélectionné
    // Méthode pour initialiser les données du service sélectionné
    @FXML
    public void initData(Service selectedService) {
        // Assurez-vous que selectedService n'est pas null
        if (selectedService != null) {
            // Utilisez les données du service sélectionné pour initialiser les éléments de l'interface utilisateur
            Service_Name.setText(selectedService.getNom());
            SERVICE_ID_res1.setText(String.valueOf(selectedService.getId()));

            // Charger et définir l'image
            String imageUrl = selectedService.getImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String imagePath = "C:\\Users\\lenovo\\CompleteProject\\public\\uploads\\" + imageUrl;

                ServiceIMG.setImage(new Image("file:"+imagePath));
            }

            // Définir selectedService sur la valeur passée en paramètre
            this.selectedService = selectedService;
        } else {
            // Si selectedService est null, affichez un message d'erreur ou effectuez une autre action appropriée
            messageLabel.setText("Error: Selected service is null.");
        }
    }




    // Méthode pour gérer l'action de réservation
    // Méthode pour gérer l'action de réservation
    @FXML
    private void handleReservation(ActionEvent event) {
        // Vérifiez si selectedService est null avant de l'utiliser
        if (selectedService == null) {
            messageLabel.setText("Error: No service selected.");
            return;
        }

        String userName = user_NAME_res.getText();
        String userEmail = User_Email_resv.getText();

        // Validation des données
        if (userName.isEmpty() || userEmail.isEmpty()) {
            messageLabel.setText("Please fill in all required fields.");
            return;
        }

        // Validation de l'email (optionnel)
        if (!isValidEmail(userEmail)) {
            messageLabel.setText("Invalid email address.");
            return;
        }

        // Création de l'objet ReservationService à ajouter
        ReservationService reservation = new ReservationService(selectedService.getId(), userName, userEmail);

        // Ajout de la réservation
        reservationService.ajouter(reservation);

        // Affichage du message de succès
        messageLabel.setText("Reservation successful!");


        // Send email notification
        sendReservationNotification(selectedService.getNom(), userEmail);


    }


    // Méthode pour valider l'email (exemple)
    private boolean isValidEmail(String email) {
        // Vérifiez si l'e-mail correspond à un format valide à l'aide d'une expression régulière
        // Voici un exemple d'expression régulière simple pour valider les e-mails :
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Vérifiez si l'e-mail correspond au format spécifié par l'expression régulière
        if (email.matches(regex)) {
            // Si l'e-mail est valide, retournez true
            return true;
        } else {
            // Sinon, retournez false
            return false;
        }
    }



    private boolean sendReservationNotification(String serviceName, String userEmail) {
        // SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", EmailSender.SMTP_HOST);
        props.put("mail.smtp.port", EmailSender.SMTP_PORT);

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailSender.EMAIL_USERNAME, EmailSender.EMAIL_PASSWORD);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EmailSender.EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject("RESERVATION A TRAITER");


            String htmlContent = "<div style=\"max-width: 600px; margin: 0 auto; background-color: #f9f9f9; padding: 20px; border-radius: 10px; font-family: Arial, sans-serif;\">\n" +
                    "    <div style=\"background-color:#ffd000; color: #fff; padding: 15px; text-align: center; border-radius: 10px 10px 0 0;\">\n" +
                    "        <h1 style=\"margin: 0;\"> Réservation à traiter </h1>\n" +
                    "    </div>\n" +
                    "    <div style=\"padding: 20px;\">\n" +
                    "        <p style=\"font-size: 18px; color: #333;\">Cher(e) <strong>" + serviceName + "</strong>,</p>\n" +
                    "        <p style=\"font-size: 16px; color: #555;\">Nous sommes heureux de vous informer que votre réservation pour le service <strong>" + serviceName + "</strong> est en cours.</p>\n" +
                    "        <p style=\"font-size: 16px; color: #555;\">N'hésitez pas à nous contacter si vous avez des questions ou besoin d'assistance supplémentaire.</p>\n" +
                    "        <p style=\"font-size: 16px; color: #555;\">Cordialement,</p>\n" +
                    "        <p style=\"font-size: 16px; color: #555;\">L'équipe Visita</p>\n" +
                    "    </div>\n" +
                    "</div>";



            // Set the HTML content of the message
            message.setContent(htmlContent, "text/html");

            // Send the message
            Transport.send(message);

            System.out.println("Verification code sent successfully to " + userEmail);
            return true; // Email sent successfully
        } catch (MessagingException e) {
            System.out.println("Failed to send verification code to " +userEmail);
            e.printStackTrace();
            return false; // Email sending failed
        }
    }






}




















