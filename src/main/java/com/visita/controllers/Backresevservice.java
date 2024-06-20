package com.visita.controllers;
import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPCell;
import com.visita.models.Admin;
import com.visita.models.User;
import javafx.stage.FileChooser;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.collections.ObservableList;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.visita.models.ReservationService;
import com.visita.services.ReservationSrvService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.*;
import java.util.List;

import javafx.stage.Window;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Backresevservice {

    @FXML
    private TableView<ReservationService> TableView;
    @FXML
    private ListView<String> list_service;
    @FXML
    private TextField afficherreserv_search;

    @FXML
    private Button afficherresv_clearBtn;

    @FXML
    private Button afficherresv_confBtn;

    @FXML
    private Button afficherresv_rejectBtn;

    @FXML
    private AnchorPane afficherserv_form;

    @FXML
    private TableColumn<ReservationService, String> affresv_col_EMAIL;

    @FXML
    private TableColumn<ReservationService, String> affresv_col_NAME;

    @FXML
    private TableColumn<ReservationService, String> affresv_col_servname;

    @FXML
    private TextField affsrv_Email;

    @FXML
    private TextField affsrv_NAME;

    @FXML
    private TextField affsrv_Service;

    @FXML
    private AnchorPane main_form;
    private Admin loggedInAdmin;
    @FXML
    private Label verf;
    @FXML
    private Button charts;
    @FXML
    private Button export_EXL;
    @FXML
    private Button pdf_btn;
    @FXML
    private Button category_btn_nv;
    @FXML
    private Button service_btn_nv;

    @FXML
    private Button serviceresv_btn_nv;
    private boolean isTableViewVisible = true;
    @FXML
    public  void close(){
        System.exit(0);
    }
    ReservationSrvService ps= new  ReservationSrvService();
    @FXML
    public void minimize(){

        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private BarChart<String, Number> chartreservationservice;


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
    void clearFields(ActionEvent event) {
        // Effacer les champs de texte
        affsrv_NAME.setText("");
        affsrv_Email.setText("");
        affsrv_Service.setText("");
    }
    void clearFields() {
        affsrv_NAME.setText("");
        affsrv_Email.setText("");
        affsrv_Service.setText("");

    }

    private void refreshTableView() {
        List<ReservationService> reservationServices = ps.afficher();
        ObservableList<ReservationService> observableList = FXCollections.observableList(reservationServices);
        TableView.setItems(observableList);
        // Force the TableView to refresh
        TableView.refresh();
    }
    @FXML
    public void initialize() {
        ReservationSrvService reservationsrvservice = new ReservationSrvService();

        // Configurer la TableView pour permettre la sélection multiple
        TableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Configurer l'écouteur de changement de sélection pour la TableView
        TableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Vérifier si un nouveau service est sélectionné
            if (newValue != null) {
                // Remplir les champs de texte avec les propriétés du service sélectionné
                affsrv_NAME.setText(newValue.getNom());
                affsrv_Email.setText(newValue.getEmail());
                affsrv_Service.setText(newValue.getService_nom());
               /* // Définir la catégorie sélectionnée
                Category selectedCategory = findCategoryByName(newValue.getCategory_nom());
                addService_CATEGORY.setValue(selectedCategory);*/
            } else {
                // Effacer les champs de texte si aucun service n'est sélectionné
                clearFields();
            }
        });



        // Configurez les colonnes de tableView

        affresv_col_NAME.setCellValueFactory(new PropertyValueFactory<ReservationService, String>("nom"));
        affresv_col_EMAIL.setCellValueFactory(new PropertyValueFactory<ReservationService, String>("email"));
        affresv_col_servname.setCellValueFactory(new PropertyValueFactory<ReservationService, String>("service_nom"));
        // Afficher les statistiques de réservation
        //handleShowStatisticsButtonClick();
        // Chargez les services lorsque le contrôleur est initialisé
        refreshTableView();
    }

    @FXML
    void supprimerReservserv(ActionEvent event) {
        // Obtenir la catégorie sélectionnée dans la TableView
        ReservationService selectedReservation = TableView.getSelectionModel().getSelectedItem();

        // Vérifier si une cReservation est sélectionnée
        if (selectedReservation != null) {
            // Demander confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer cette Reservation ?");
            alert.setContentText("Service Reserverver : " + selectedReservation.getService_nom() + "\n\nCette action est irréversible. Veuillez confirmer.");

            Optional<ButtonType> result = alert.showAndWait();

            // Si l'utilisateur confirme, supprimer la catégorie
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer la catégorie avec le service
                ps.supprimer(selectedReservation);
                refreshTableView();

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Suppression de la RESERVATION");
                successAlert.setHeaderText("RESERVATION REJECTER !");
                successAlert.setContentText("La RESERVATION  \"" + selectedReservation.getNom() + "\" a été supprimée avec succès.\n\n Opération réussie !");
                successAlert.showAndWait();
            }
        } else {
            // Aucune catégorie sélectionnée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sélection manquante");
            alert.setHeaderText("Erreur de sélection");
            alert.setContentText(" Il semble que vous n'ayez pas sélectionné de Reservation. Veuillez en choisir une à supprimer.");
            alert.showAndWait();
        }
        // Effacer les champs de texte après la modification
        clearFields();
        // Send email notification
        sendReservationNotificationrej(selectedReservation.getNom(), selectedReservation.getEmail());
    }


    @FXML
    void ConfirmerReservserv(ActionEvent event) {
        // Obtenir la catégorie sélectionnée dans la TableView
        ReservationService selectedReservation = TableView.getSelectionModel().getSelectedItem();

        // Vérifier si une cReservation est sélectionnée
        if (selectedReservation != null) {
            // Demander confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la RESERVATION ");
            alert.setHeaderText("Voulez-vous vraiment confirmer cette Reservation ?");
            alert.setContentText("Service Reserverver : " + selectedReservation.getService_nom() + "\n\nCette action est irréversible. Veuillez confirmer.");

            Optional<ButtonType> result = alert.showAndWait();

            // Si l'utilisateur confirme, supprimer la catégorie
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer la catégorie avec le service
                ps.supprimer(selectedReservation);
                refreshTableView();

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("RESERVATION réussie ");
                successAlert.setHeaderText(" LA RESERVATION EST  CONFIRMER  !");
                successAlert.setContentText("LA RESERVATION \"" + selectedReservation.getNom() + "\" a été supprimée avec succès.\n\n Opération réussie !");
                successAlert.showAndWait();
            }
        } else {
            // Aucune catégorie sélectionnée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sélection manquante");
            alert.setHeaderText("Erreur de sélection");
            alert.setContentText(" Il semble que vous n'ayez pas sélectionné de Reservation. Veuillez en choisir une à supprimer.");
            alert.showAndWait();
        }
        // Effacer les champs de texte après la modification
        clearFields();

        // Send email notification
        sendReservationNotificationconf(selectedReservation.getNom(), selectedReservation.getEmail());

    }







    @FXML
    void searchReservationServ() {
        // Obtenez les critères de recherche de l'utilisateur
        String searchText = afficherreserv_search.getText().toLowerCase();

        // Filtrer la liste des catégories en fonction des critères de recherche
        List<ReservationService> reservationServices = ps.afficher(); // Récupérez toutes les catégories
        List<ReservationService> filteredreservationServices= new ArrayList<>();

        for (ReservationService reservationservice : reservationServices) {
            // Vérifiez si l'ID, le nom, la description ou l'icône de la catégorie correspondent aux critères de recherche

            boolean matchesName = reservationservice.getNom().toLowerCase().contains(searchText);
            boolean matchesEmail = reservationservice.getEmail().toLowerCase().contains(searchText);
            boolean matchesService = reservationservice.getService_nom().toLowerCase().contains(searchText);

            // Si l'un des champs correspond aux critères de recherche, ajoutez la catégorie à la liste filtrée
            if ( matchesName || matchesEmail || matchesService) {
                filteredreservationServices.add(reservationservice);
            }
        }

        // Mettre à jour la TableView avec les catégories filtrées
        ObservableList<ReservationService> observableList = FXCollections.observableList(filteredreservationServices);
        TableView.setItems(observableList);
    }
    private boolean sendReservationNotificationconf(String serviceName, String userEmail) {
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
            message.setSubject("RESERVATION ACCEPTER");


            String htmlContent = "<div style=\"max-width: 600px; margin: 0 auto; background-color: #f9f9f9; padding: 20px; border-radius: 10px; font-family: Arial, sans-serif;\">\n" +
                    "    <div style=\"background-color:#3cba9f; color: #fff; padding: 15px; text-align: center; border-radius: 10px 10px 0 0;\">\n" +
                    "        <h1 style=\"margin: 0;\"> Réservation acceptée </h1>\n" +
                    "    </div>\n" +
                    "    <div style=\"padding: 20px;\">\n" +
                    "        <p style=\"font-size: 18px; color: #333;\">Cher(e) <strong>" + serviceName + "</strong>,</p>\n" +
                    "        <p style=\"font-size: 16px; color: #555;\">Nous sommes heureux de vous informer que votre réservation pour le service <strong>" + serviceName + "</strong> a été acceptée.</p>\n" +
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




    private boolean sendReservationNotificationrej(String serviceName, String userEmail) {
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
            message.setSubject(" RESERVATION REJETEE ");


            String htmlContent = "<div style=\"max-width: 600px; margin: 0 auto; background-color: #f9f9f9; padding: 20px; border-radius: 10px; font-family: Arial, sans-serif;\">\n" +
                    "    <div style=\"background-color:#db4437; color: #fff; padding: 15px; text-align: center; border-radius: 10px 10px 0 0;\">\n" +
                    "        <h1 style=\"margin: 0;\"> Réservation rejetée </h1>\n" +
                    "    </div>\n" +
                    "    <div style=\"padding: 20px;\">\n" +
                    "        <p style=\"font-size: 18px; color: #333;\">Cher(e) <strong>" + serviceName + "</strong>,</p>\n" +
                    "        <p style=\"font-size: 16px; color: #555;\">Nous sommes désolés de vous informer que votre réservation pour le service <strong>" + serviceName + "</strong> a été rejetée.</p>\n" +
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
    @FXML
    private void handleShowChartButtonClick(ActionEvent event) {
        try {
            // Charger le fichier FXML de l'interface Chartreservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chartreservation.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur Chartreservation
            Chartreservation chartController = loader.getController();

            // Appeler la méthode showReservationStatistics pour mettre à jour le bar chart
            chartController.showReservationStatistics();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre pour afficher l'interface Chartreservation
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePdfButtonAction(ActionEvent event) {
        Document document = new Document(PageSize.A4);

        try {
            // Specify the output path of the PDF file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("reservations.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

            if (selectedFile != null) {
                PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                document.open();

                // Add a customized title
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
                Paragraph title = new Paragraph("Rapport de Réservations de Services", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20); // Add spacing after the title
                document.add(title);

                // Add a logo
                Image logo = Image.getInstance("C:\\Users\\lenovo\\IdeaProjects\\Projet_pi_java_3A54-master\\Projet_pi_java_3A54-master\\src\\main\\resources\\values\\default_image12.png");
                logo.setAlignment(Element.ALIGN_CENTER);
                logo.scaleToFit(200, 200);
                document.add(logo);

                // Create a table PDF with 3 columns for reservation data
                PdfPTable pdfTable = new PdfPTable(3);
                pdfTable.setWidthPercentage(100); // Set table width to 100% of page width

                // Add table headers with styling
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
                PdfPCell cell1 = new PdfPCell(new Phrase("Nom", headerFont));
                PdfPCell cell2 = new PdfPCell(new Phrase("Email", headerFont));
                PdfPCell cell3 = new PdfPCell(new Phrase("Service", headerFont));

                // Set background color for header cells
                cell1.setBackgroundColor(BaseColor.GRAY);
                cell2.setBackgroundColor(BaseColor.GRAY);
                cell3.setBackgroundColor(BaseColor.GRAY);

                pdfTable.addCell(cell1);
                pdfTable.addCell(cell2);
                pdfTable.addCell(cell3);

                // Get data from the TableView
                ObservableList             <ReservationService> reservationServices = TableView.getItems();

                // Add data from the TableView to the PDF table
                for (ReservationService reservationService : reservationServices) {
                    pdfTable.addCell(new Phrase(reservationService.getNom()));
                    pdfTable.addCell(new Phrase(reservationService.getEmail()));
                    pdfTable.addCell(new Phrase(reservationService.getService_nom()));
                }

                // Add the PDF table to the document
                document.add(pdfTable);

                // Close the document
                document.close();

                // Show a success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PDF Généré");
                alert.setHeaderText(null);
                alert.setContentText("Le fichier PDF a été généré avec succès !");
                alert.showAndWait();
            }
        } catch (DocumentException | IOException e) {
            // Handle exceptions
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur est survenue lors de la génération du PDF !");
            alert.showAndWait();
        }
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
                Sheet sheet = workbook.createSheet("Reservation Services");

                // Créez une ligne d'en-tête dans la feuille
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Nom");
                headerRow.createCell(1).setCellValue("EMAIL");
                headerRow.createCell(2).setCellValue("SERVICE");


                // Récupérez les services de votre TableView
                List<ReservationService> servicesList = new ArrayList<>(TableView.getItems());

                // Remplissez la feuille de calcul avec les données des services
                for (int i = 0; i < servicesList.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    ReservationService service = servicesList.get(i);

                    // Remplissez les cellules avec les données du service
                    row.createCell(0).setCellValue(service.getNom());
                    row.createCell(1).setCellValue(service.getEmail());
                    row.createCell(2).setCellValue(service.getService_nom());
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






