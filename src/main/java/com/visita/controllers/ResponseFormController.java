package com.visita.controllers;

import com.visita.models.Reclamation;
import com.visita.utils.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class ResponseFormController {

    @FXML
    private TextArea responseTextArea;

    private Reclamation reclamation;

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

    @FXML
    private void handleReply() {
        String responseContent = responseTextArea.getText();

        // Validate input
        if (responseContent.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Response content cannot be empty.");
        } else if (responseContent.length() > 500) {
            showAlert(Alert.AlertType.ERROR, "Error", "Response content cannot exceed 500 characters.");
        } else {
            saveResponseToDatabase(responseContent);
            sendEmailToUser(responseContent);
            closeWindow();
        }
    }

    @FXML
    private void handleClose() {
        closeWindow();
    }

    private void saveResponseToDatabase(String responseContent) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "INSERT INTO reponse (reclamation_id, author, response_content, response_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, reclamation.getId());
                statement.setString(2, "Admin");
                statement.setString(3, responseContent);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save response to the database.");
        }
    }

    private boolean sendEmailToUser(String responseContent) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", EmailSender.SMTP_HOST);
        props.put("mail.smtp.port", EmailSender.SMTP_PORT);
        props.put("mail.smtp.ssl.trust", "*");

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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reclamation.getEmail()));
            message.setSubject("Réponse à votre réclamation");

            String htmlContent = "<div style=\"max-width: 600px; margin: 0 auto; background-color: #f9f9f9; padding: 20px; border-radius: 10px; font-family: Arial, sans-serif;\">"; // Set background color to a light gray and use Arial font
            htmlContent += "<div style=\"background-color:#ffd000; color: #fff; padding: 15px; text-align: center; border-radius: 10px 10px 0 0;\">"; // Centered headline with blue background and white text
            htmlContent += "<h1 style=\"margin: 0;\"> Réponse à votre réclamation </h1>";
            htmlContent += "</div>";
            htmlContent += "<div style=\"margin-top: 20px; padding: 20px;\">";
            htmlContent += "<p style=\"font-size: 18px;  color: #333;\"><strong>Hello, </strong></p>"; // Ajoutez une salutation générique
            htmlContent += "<p style=\"margin: 10px 0; font-size: 16px; color: #555; line-height: 1.5;\">" + responseContent + "</p>"; // Utilisez le contenu de la réponse
            htmlContent += "</div>";
            htmlContent += "</body></html>";

            // Set the HTML content of the message
            message.setContent(htmlContent, "text/html");

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully to " + reclamation.getEmail());
            return true; // Email sent successfully
        } catch (MessagingException e) {
            System.out.println("Failed to send email to " + reclamation.getEmail());
            e.printStackTrace();
            return false; // Email sending failed
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) responseTextArea.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
