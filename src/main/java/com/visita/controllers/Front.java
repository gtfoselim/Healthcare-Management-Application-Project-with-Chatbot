package com.visita.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.visita.models.*;
import com.visita.services.RapportService;
import com.visita.services.RendezvousService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;


public class Front {

    @FXML
    private Button btnafficher;
    @FXML
    private Button btnpdf;
    @FXML
    private Button btnvalider;
    @FXML
    private Button getrv;

    @FXML
    private Button btnclear;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private VBox chosenFruitCard;
    @FXML
    private Button btnpayer;

    @FXML
    private DatePicker dateid;

    @FXML
    private TextField emailid;

    @FXML
    private TextField fullnameid;

    @FXML
    private TextField idrendezvous;

    @FXML
    private TextField idrendezvous1;

    @FXML
    private Button logoutButton;

    @FXML
    private ChoiceBox<Doctor> medecinid;


    @FXML
    private TextField noteid;

    @FXML
    private TextArea rapportT;


    /*
@FXML
private Text rapportT;

     */
    @FXML
    private TextField telid;

    @FXML
    private TextField timeid;

    private RendezvousService rendezvousService = new RendezvousService();
    private RapportService rapportService = new RapportService();






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
    void initialize() {
        loadrv();


    }

    @FXML
    void addrv(ActionEvent event) {

        try {
            // Conversion de la date de type String en objet Date
            // Obtenez la valeur sélectionnée du DatePicker
            LocalDate selectedDate = dateid.getValue();

            // Convertissez la LocalDate en String
            String dateString = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Parsez la String en objet Date
            Date parsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);

            // Conversion de l'heure de type String en objet Time
            Time parsedTime = Time.valueOf(timeid.getText());


            // Obtenir l'ID de la catégorie sélectionnée
            Doctor selectedM = medecinid.getValue();
            int MEDECINID = selectedM != null ? selectedM.getId() : -1; // -1 si la catégorie n'est pas sélectionnée

            // Ajout du rendez-vous en utilisant la méthode ajouter de votre service
            rendezvousService.ajouter(new Rendezvous(
                            MEDECINID, // medecin_id (supposant qu'il s'agit d'un champ texte)
                            fullnameid.getText(), // fullname (supposant qu'il s'agit d'un champ texte)
                            Integer.parseInt(telid.getText()), // tel (supposant qu'il s'agit d'un champ texte)
                            parsedDate, // date (convertie de la chaîne de caractères de votre champ de texte)
                            parsedTime, // time (convertie de la chaîne de caractères de votre champ de texte)
                            noteid.getText(), // note (supposant qu'il s'agit d'un champ texte)
                            // etat (converti de la valeur de la checkbox)
                            emailid.getText()// email (supposant qu'il s'agit d'un champ texte)
                    ) // rapport_id (supposant qu'il s'agit d'un champ texte)
            );


            // Afficher une alerte pour indiquer que le rendez-vous a été ajouté avec succès


        } catch (ParseException e) {
            // Gérer l'erreur de parsing de la date ou de l'heure
            e.printStackTrace();

            // Afficher une alerte en cas d'échec de l'ajout du rendez-vous
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du rendez-vous !", "");
        }
    }

    @FXML
    void afficherRP(ActionEvent event) {
        // Récupérer l'identifiant du rendez-vous depuis le champ de texte
        int rendezvousId = Integer.parseInt(idrendezvous.getText());

        // Appeler le service pour récupérer le rendez-vous par son ID
        Rendezvous rendezvous = rendezvousService.getById(rendezvousId);

        // Vérifier si le rendez-vous existe
        if (rendezvous != null) {
            // Accéder au rapport associé au rendez-vous
            int rapportId = rendezvous.getRapport_id();

            // Vérifier si le rapport existe
            if (rapportId == 0) {
                // Le rendez-vous n'a pas de rapport associé
                showAlert(Alert.AlertType.INFORMATION, "Information", "Aucun rapport trouvé", "Ce rendez-vous n'a pas de rapport associé.");
                rapportT.setText("Aucun rapport associé a ce rendezvous pour le moment ");
            } else {
                // Le rendez-vous a un rapport associé, récupérer les données du rapport
                Rapport rapport = rapportService.getById(rapportId);

                // Vérifier si le rapport existe
                if (rapport != null) {
                    // Afficher les attributs du rapport dans le TextArea
                    String rapportDetails = "VOTRE RAPPORT : \n" + "Type:\t " + rapport.getType() + "\n" +
                            "Note:\t " + rapport.getNote() + "\n";
                    rapportT.setText(rapportDetails);
                } else {
                    // Le rapport n'existe pas
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Rapport introuvable", "Le rapport associé à ce rendez-vous n'existe pas.");
                }
            }
        } else {
            // Le rendez-vous n'existe pas
            showAlert(Alert.AlertType.ERROR, "Erreur", "Rendez-vous introuvable", "Aucun rendez-vous trouvé avec cet identifiant.");
        }
    }


    @FXML
    void clearrv(ActionEvent event) {
        fullnameid.clear();
        telid.clear();
        dateid.setValue(null); // Effacer la date sélectionnée
        timeid.clear();
        noteid.clear();
        emailid.clear();
        idrendezvous1.clear();
        medecinid.setValue(null);
        idrendezvous.clear();
        rapportT.clear();


    }

    @FXML
    void handleLogoutButton(ActionEvent event) {

    }

    @FXML
    void updaterv(ActionEvent event) {
        try {
            // Récupération de la date actuelle
            LocalDate currentDate = LocalDate.now();
            // Récupération de la date du rendez-vous depuis la base de données
            Rendezvous rv = rendezvousService.getById(Integer.parseInt(idrendezvous1.getText())); // Supposons que vous récupérez le rendez-vous par son ID
            // Conversion de la date SQL en java.util.Date
            Date rendezvousUtilDate = new Date(rv.getDate().getTime());
            // Conversion de la date du rendez-vous en LocalDate
            LocalDate rendezvousDate = rendezvousUtilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // Calcul de la différence en jours entre la date actuelle et la date du rendez-vous
            long daysUntilAppointment = ChronoUnit.DAYS.between(currentDate, rendezvousDate);
            // Vérification si la différence est inférieure ou égale à 7 jours
            if (daysUntilAppointment <= 7) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Vous ne pouvez pas modifier un rendez-vous moins de 7 jours avant la date prévue.", "");
                return;
            }


            // Conversion de la date de type String en objet Date
            // Obtenez la valeur sélectionnée du DatePicker
            LocalDate selectedDate = dateid.getValue();

            // Convertissez la LocalDate en String
            String dateString = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Parsez la String en objet Date
            Date parsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);

            // Conversion de l'heure de type String en objet Time
            Time parsedTime = Time.valueOf(timeid.getText());

            // Création d'un objet Rendezvous avec les données mises à jour
            Doctor selectedM = medecinid.getValue();
            int MID = selectedM != null ? selectedM.getId() : -1;

            Rendezvous rendezvous = new Rendezvous(Integer.parseInt(idrendezvous1.getText()),
                    MID, // medecin_id (supposant qu'il s'agit d'un champ texte)
                    fullnameid.getText(), // fullname (supposant qu'il s'agit d'un champ texte)
                    Integer.parseInt(telid.getText()), // tel (supposant qu'il s'agit d'un champ texte)
                    parsedDate, // date (convertie de la chaîne de caractères de votre champ de texte)
                    parsedTime, // time (convertie de la chaîne de caractères de votre champ de texte)
                    noteid.getText(), // note (supposant qu'il s'agit d'un champ texte)
                    // etat (converti de la valeur de la checkbox)
                    emailid.getText() // email (supposant qu'il s'agit d'un champ texte)
            ); // rapport_id (supposant qu'il s'agit d'un champ texte)

            // Ajout du rendez-vous en utilisant la méthode modifier de votre service
            rendezvousService.modifier(rendezvous);

            // Afficher une alerte pour indiquer que le rendez-vous a été mis à jour avec succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Rendez-vous mis à jour avec succès !", "");


        } catch (ParseException e) {
            // Gérer l'erreur de parsing de la date ou de l'heure
            e.printStackTrace();

            // Afficher une alerte en cas d'échec de la mise à jour du rendez-vous
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la mise à jour du rendez-vous !", "");
        }


    }


    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void loadrv() {
        // Récupérer la liste des catégories depuis le service
        List<Doctor> medecins = RendezvousService.getAllMedecin();

        // Convertir la liste en ObservableList
        ObservableList<Doctor> observableCategories = FXCollections.observableArrayList(medecins);

        // Assigner la liste observable au ChoiceBox
        medecinid.setItems(observableCategories);

        // Définir un convertisseur de chaîne pour afficher les noms de catégorie
        medecinid.setConverter(new StringConverter<Doctor>() {
            @Override
            public String toString(Doctor medecin) {
                return medecin == null ? "" : medecin.getFullname();
            }

            @Override
            public Doctor fromString(String string) {
                // Si vous souhaitez permettre à l'utilisateur d'ajouter de nouvelles catégories en saisissant du texte,
                // vous pouvez ajouter une logique ici pour créer une nouvelle catégorie à partir de la chaîne de texte.
                // Sinon, vous pouvez simplement retourner null ici.
                return null;
            }
        });
    }

    private void refreshChoiceBox() {
        // Effacer le contenu actuel du ChoiceBox
        medecinid.getItems().clear();

        // Récupérer les dernières données de la base de données
        List<Doctor> latestM = RendezvousService.getAllMedecin();

        // Ajouter les dernières données au ChoiceBox
        medecinid.getItems().addAll(latestM);

        // Définir un convertisseur de chaîne pour afficher les noms de rendez-vous
        medecinid.setConverter(new StringConverter<Doctor>() {
            @Override
            public String toString(Doctor medecin) {
                return medecin == null ? "" : medecin.getFullname();
            }

            @Override
            public Doctor fromString(String string) {
                // Si nécessaire, implémentez la logique pour convertir une chaîne en objet Rendezvous
                return null;
            }
        });
    }


    private Doctor findrvByName(String rvName) {
        if (rvName == null || rvName.isEmpty()) {
            // Gérer le cas où rvName est null ou vide
            return null;
        }

        // Obtenir la liste des médecins disponibles
        ObservableList<Doctor> rvs = medecinid.getItems();

        // Parcourir les médecins et trouver celui dont le nom correspond au nom fourni
        for (Doctor m : rvs) {
            if (m.getFullname().equalsIgnoreCase(rvName)) {
                return m;
            }
        }

        // Aucun médecin correspondant trouvé, vous pouvez ajouter un message de journalisation ou d'alerte ici
        return null;
    }


    @FXML
    void getrv(ActionEvent event) {
        // Récupérer l'identifiant du rendez-vous depuis le champ de texte
        int rendezvousId = Integer.parseInt(idrendezvous1.getText());

        // Appeler le service pour récupérer le rendez-vous par son ID
        Rendezvous rendezvous = rendezvousService.getById(rendezvousId);

        // Vérifier si le rendez-vous existe
        if (rendezvous != null) {

            fullnameid.setText(rendezvous.getFullname());
            emailid.setText(rendezvous.getEmail());
            noteid.setText(rendezvous.getNote());
            // Convertir le temps en chaîne de caractères
            String timeAsString = rendezvous.getTime().toString();

// Définir la valeur dans le champ de texte
            timeid.setText(timeAsString);






            // Convertir l'entier en chaîne de caractères
            String telAsString = String.valueOf(rendezvous.getTel());

// Définir la valeur dans le champ de texte
            telid.setText(telAsString);
            // Définir le rv sélectionnée
            Doctor selectedM = findById(rendezvous.getMedecin_id());
            medecinid.setValue(selectedM);

            // Récupérer la date du service sélectionné depuis la base de données (java.util.Date)
            Date dateFromDB = rendezvous.getDate();

// Convertir java.util.Date en java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(dateFromDB.getTime());

// Convertir java.sql.Date en LocalDate
            LocalDate localDate = sqlDate.toLocalDate();

// Définir la date dans le DatePicker
            dateid.setValue(localDate);



        } else {
            // Le rendez-vous n'existe pas
            showAlert(Alert.AlertType.ERROR, "Erreur", "Rendez-vous introuvable", "Aucun rendez-vous trouvé avec cet identifiant.");
        }

    }


    public Doctor findById(int id) {
        // Accédez à votre source de données (par exemple, la base de données) pour rechercher le médecin par son ID
        // Assurez-vous de gérer les exceptions appropriées (par exemple, SQLException en cas d'accès à une base de données)
        try {
            // Votre logique pour rechercher le médecin par son ID et le retourner s'il est trouvé
            // Par exemple, si vous avez une liste de médecins, vous pouvez itérer sur la liste pour trouver le médecin avec l'ID correspondant
            for (Doctor medecin : rendezvousService.getAllMedecin()) {
                if (medecin.getId() == id) {
                    return medecin;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'exception selon les besoins (par exemple, journalisation, affichage d'un message d'erreur, etc.)
        }
        // Si aucun médecin n'est trouvé avec l'ID fourni, retournez null
        return null;
    }

    //---------------------------pdf---------------------

/*
    @FXML
    void generatePDF(ActionEvent event) {
        String content = rapportT.getText(); // Récupère le contenu du TextArea

        // Récupérer le contenu du TextArea
        String content1 = rapportT.getText();
        // Générer un PDF avec le contenu du TextArea
        pdf(content1);
    }

    private void pdf(String content) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Rapport.pdf"));
            document.open();

            // Ajouter le contenu du TextArea au PDF
            document.add(new Paragraph(content));

            document.close();
            System.out.println("PDF file generated successfully.");
            showAlert(Alert.AlertType.CONFIRMATION, "SUCCES", "PDF Generé", "Check your files ");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }


        /////////////////////smssss

    }

 */

    @FXML
    void generatePDF(ActionEvent event) {

        String content = rapportT.getText(); // Récupère le contenu du TextArea

        // Récupérer le contenu du TextArea
        String content1 = rapportT.getText();
        // Générer un PDF avec le contenu du TextArea
        pdf(content1);
    }
    private void pdf(String content) {
        Document document = new Document();
        try {


            PdfWriter.getInstance(document, new FileOutputStream("Rapport.pdf"));

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Rapport.pdf"));
            writer.setPageEvent(new Background());
            document.open();
            // Titre
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
            Paragraph title = new Paragraph("Rapport PDF", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Espacement
           document.add(new Paragraph("\n")); // Ajoute un paragraphe vide pour créer de l'espace



            // Contenu du rapport
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Paragraph contentParagraph = new Paragraph(content, contentFont);
            contentParagraph.setIndentationLeft(40);
            document.add(contentParagraph);

            // Espacement
            document.add(new Paragraph("\n")); // Ajoute un paragraphe vide pour créer de l'espace

            // Espacement avant le contenu
            Paragraph spacer = new Paragraph();
            spacer.setSpacingBefore(100); // Ajustez la valeur selon vos besoins pour déplacer le contenu vers le haut de la page
            document.add(spacer);
            // Date de génération
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY);
            Paragraph dateTime = new Paragraph("Date de génération : " + getCurrentDateTime(), dateFont);
            dateTime.setAlignment(Element.ALIGN_RIGHT);
            dateTime.setIndentationRight(20);
            document.add(dateTime);

            document.close();
            System.out.println("PDF file generated successfully.");
            showAlert(Alert.AlertType.CONFIRMATION, "SUCCES", "PDF Generé", "Check your files ");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }


        /////////////////////smssss

    }

    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return formatter.format(now);
    }

    // Classe pour définir un fond sur chaque page
    class Background extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfContentByte canvas = writer.getDirectContentUnder();
                Image image = Image.getInstance("src/main/resources/images/test.jpg"); // Remplacez "background.jpg" par le chemin de votre image de fond
                image.scaleAbsolute(document.getPageSize());
                image.setAbsolutePosition(0, 0);
                canvas.addImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /////////////////////////payer///////////
    @FXML
    void payer(ActionEvent event)throws IOException
    {

        payer1();
    }

    void payer1() throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/Payment.fxml"));
        Parent root=loader.load();

        idrendezvous1.getScene().setRoot(root);
    }
}