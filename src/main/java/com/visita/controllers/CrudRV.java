package com.visita.controllers;

import com.visita.models.*;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class CrudRV {
    @FXML
    private Button rappelid;
    @FXML
    private AnchorPane addRV_form;

    @FXML
    private Button btnstat;

    @FXML
    private Button btnreport;
    @FXML
    private ChoiceBox<Doctor> medecinid;

    @FXML
    private TextField telid;
    @FXML
    private TextField addRV_search;

    @FXML
    private ChoiceBox<Doctor> addService_Rv;

    @FXML
    private TextField idrendezvous;
    @FXML
    private Button btnclear;
    @FXML
    private Button btnvalider;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<Rendezvous, Date> date;

    @FXML
    private DatePicker dateid;

    @FXML
    private TableColumn<Rendezvous, Integer> doctor;

    @FXML
    private TableColumn<Rendezvous, String> email;

    @FXML
    private TextField emailid;

    @FXML
    private TableColumn<Rendezvous, Boolean> etat;

    @FXML
    private TableColumn<Rendezvous, String> fullname;

    @FXML
    private TextField fullnameid;

    @FXML
    private AnchorPane main_form;


    @FXML
    private TableColumn<Rendezvous, String> note;

    @FXML
    private TextField noteid;

    @FXML
    private TableColumn<Rendezvous, Integer> phone;

    @FXML
    private TableColumn<Rendezvous, Rapport> report;

    @FXML
    private TableView<Rendezvous> tableview;

    @FXML
    private TableColumn<Rendezvous, Time> time;
    private Admin loggedInAdmin;

    @FXML
    private TextField timeid;

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


    RendezvousService ps = new RendezvousService();


    @FXML
    void initialize() {
        // Liaison des propriétés des colonnes avec les propriétés des objets Rendezvous
        doctor.setCellValueFactory(new PropertyValueFactory<>("medecin_nom"));
        fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        phone.setCellValueFactory(new PropertyValueFactory<>("tel"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        note.setCellValueFactory(new PropertyValueFactory<>("note"));
        etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        report.setCellValueFactory(new PropertyValueFactory<>("rapport_id"));

        // Récupération de la liste des rendez-vous depuis le service ps
        List<Rendezvous> liste = ps.afficher();
        // Ajout des rendez-vous à la tableview
        tableview.getItems().addAll(liste);
        loadrv();

        // Gestionnaire d'événements pour la sélection de ligne dans le TableView
        tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Mettre à jour les champs texte avec les données de la ligne sélectionnée
                Rendezvous selectedRendezvous = tableview.getSelectionModel().getSelectedItem();

                fullnameid.setText(selectedRendezvous.getFullname());
                telid.setText(String.valueOf(selectedRendezvous.getTel()));

                // Afficher l'heure
                timeid.setText(selectedRendezvous.getTime().toString());
                noteid.setText(selectedRendezvous.getNote());
                emailid.setText(selectedRendezvous.getEmail());
                idrendezvous.setText(String.valueOf(selectedRendezvous.getId()));
               // loadRV();
                //addRV_search.textProperty().addListener((observable, oldValue, newValue) -> {
                    // Call a method to update the table based on the new search value
                    //searchrv();
                //});
                // Définir le rv sélectionnée
                Doctor selectedM = findrvByName(newSelection.getMedecin_nom());
                medecinid.setValue(selectedM);

                // Récupérer la date du service sélectionné depuis la base de données (java.util.Date)
                Date dateFromDB = selectedRendezvous.getDate();

// Convertir java.util.Date en java.sql.Date
                java.sql.Date sqlDate = new java.sql.Date(dateFromDB.getTime());

// Convertir java.sql.Date en LocalDate
                LocalDate localDate = sqlDate.toLocalDate();

// Définir la date dans le DatePicker
                dateid.setValue(localDate);

            } else {
                // Réinitialiser les champs texte si aucune ligne n'est sélectionnée
                //medecinid.clear();
                fullnameid.clear();
                telid.clear();
                dateid.setValue(null);
                timeid.clear();
                noteid.clear();
                emailid.clear();
                idrendezvous.clear();
                medecinid.setValue(null);
            }
        });
    }


    // Méthode pour charger les données dans le tableau
    private void chargerDonnees() {
        // Effacer les éléments existants dans le tableau
        tableview.getItems().clear();

        // Charger les nouvelles données dans le tableau
        List<Rendezvous> liste = ps.afficher();
        tableview.getItems().addAll(liste);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void addrv(ActionEvent event) throws ParseException {

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
            ps.ajouter(new Rendezvous(
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

           chargerDonnees();

        } catch (ParseException e) {
            // Gérer l'erreur de parsing de la date ou de l'heure
            e.printStackTrace();

            // Afficher une alerte en cas d'échec de l'ajout du rendez-vous
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du rendez-vous !","");
        }
    }


    @FXML
    void close(ActionEvent event)
    {

    }

    @FXML
    void deleteRV() throws IOException {
// Obtenir le rv sélectionné dans la TableView
        Rendezvous selectedrv = tableview.getSelectionModel().getSelectedItem(); // Utiliser TableView avec une majuscule

        // Vérifier si une catégorie est sélectionnée
        if (selectedrv != null) {
            // Demander confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer ce Rendezvous ?");
            alert.setContentText("Rendezvous : " + selectedrv.getFullname() + "\n\nCette action est irréversible. Veuillez confirmer.");

            Optional<ButtonType> result = alert.showAndWait();

            // Si l'utilisateur confirme, supprimer rv
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer rv
                ps.supprimer(selectedrv);
                chargerDonnees();

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Suppression réussie");
                successAlert.setHeaderText("Rendezvous supprimée !");
                successAlert.setContentText("Le Rendezvous \"" + selectedrv.getFullname() + "\" a été supprimée avec succès.\n\n Opération réussie !");
                successAlert.showAndWait();
            }
        } else {
            // Aucune catégorie sélectionnée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sélection manquante");
            alert.setHeaderText("Erreur de sélection");
            alert.setContentText(" Il semble que vous n'ayez pas sélectionné de Rendezvous. Veuillez en choisir une à supprimer.");
            alert.showAndWait();
        }

    }

    @FXML
    void minimize(ActionEvent event) {

    }

    @FXML
    void updaterv(ActionEvent event) {

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

            // Création d'un objet Rendezvous avec les données mises à jour
            Doctor selectedM = medecinid.getValue();
            int MID = selectedM != null ? selectedM.getId() : -1;

            Rendezvous rendezvous = new Rendezvous(Integer.parseInt(idrendezvous.getText()),
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
            ps.modifier(rendezvous);

            chargerDonnees();
            // Afficher une alerte pour indiquer que le rendez-vous a été mis à jour avec succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Rendez-vous mis à jour avec succès !","");


        } catch (ParseException e) {
            // Gérer l'erreur de parsing de la date ou de l'heure
            e.printStackTrace();

            // Afficher une alerte en cas d'échec de la mise à jour du rendez-vous
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la mise à jour du rendez-vous !","");
        }

    }

    @FXML
    void clearrv(ActionEvent event) {
        //medecinid.clear();
        fullnameid.clear();
        telid.clear();
        dateid.setValue(null); // Effacer la date sélectionnée
        timeid.clear();
        noteid.clear();
        emailid.clear();
        medecinid.setValue(null);





    }

    private void loadRV() {
        // Récupérer la liste des médecins depuis le service
        List<Doctor> medecins = RendezvousService.getAllMedecin();

        // Convertir la liste en ObservableList
        ObservableList<Doctor> observableMedecins = FXCollections.observableArrayList(medecins);

        // Assigner la liste observable au ChoiceBox
        addService_Rv.setItems(observableMedecins);

        // Définir un convertisseur de chaîne pour afficher les noms des médecins
        addService_Rv.setConverter(new StringConverter<Doctor>() {
            @Override
            public String toString(Doctor medecin) {
                return medecin == null ? "" : medecin.getFullname();
            }

            @Override
            public Doctor fromString(String string) {
                // Si vous souhaitez permettre à l'utilisateur d'ajouter de nouveaux médecins en saisissant du texte,
                // vous pouvez ajouter une logique ici pour créer un nouveau médecin à partir de la chaîne de texte.
                // Sinon, vous pouvez simplement retourner null ici.
                return null;
            }
        });
    }

    private int getSelectedMedecinId() {
        // Récupérer l'objet Medecin sélectionné dans le ChoiceBox
        Doctor selectedMedecin = addService_Rv.getValue();

        // Vérifier si un médecin est sélectionné
        if (selectedMedecin != null) {
            // Retourner l'ID du médecin sélectionné
            return selectedMedecin.getId();
        } else {
            // Si aucun médecin n'est sélectionné, retourner une valeur par défaut (0 ou une autre valeur appropriée)
            return 0;
        }
    }

     @FXML
   void searchrv() {
        // Get the user's search criteria
        String searchText = addRV_search.getText().toLowerCase();

        // Filter the list of patients based on the search criteria
        List<Rendezvous> allrv = ps.afficher(); // Get all patients
        List<Rendezvous> filtererv = new ArrayList<>();

        for (Rendezvous patient : allrv) {
            // Check if username, email, phone, or fullname of the patient matches the search criteria
            boolean matchesUsername = patient.getFullname().toLowerCase().contains(searchText);
            boolean matchesEmail = patient.getEmail().toLowerCase().contains(searchText);
            boolean matchesPhone = patient.getNote().toLowerCase().contains(searchText);
            boolean matchesmedecin = patient.getMedecin_nom().toLowerCase().contains(searchText);


            // If any of the fields match the search criteria, add the patient to the filtered list
            if (matchesUsername || matchesEmail || matchesPhone || matchesmedecin ) {
                filtererv.add(patient);
            }
        }

        // Update the TableView with the filtered patients
        ObservableList<Rendezvous> observableList = FXCollections.observableList(filtererv);
        tableview.setItems(observableList);


    }

    @FXML
    void actionreport(ActionEvent event) throws IOException {

        report();


    }



    void report() throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/CrudRapport.fxml"));
        Parent root=loader.load();
        CrudRapport controller = loader.getController();

        // Pass the logged-in admin data to the controller
        controller.setLoggedInAdmin(loggedInAdmin);

        medecinid.getScene().setRoot(root);
    }
    @FXML
    void stat(ActionEvent event) throws IOException {

        stat();


    }



    void stat() throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/StatRV.fxml"));
        Parent root=loader.load();

        StatRV controller = loader.getController();

        // Pass the logged-in admin data to the controller
        controller.setLoggedInAdmin(loggedInAdmin);

        medecinid.getScene().setRoot(root);
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



    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private Doctor findrvByName(String rvName) {
        // Obtenir la liste des catégories disponibles
        ObservableList<Doctor> rvs = medecinid.getItems();

        // Parcourir les catégories et trouver celle dont le nom correspond au nom fourni
        for (Doctor rv : rvs) {
            if (rv.getFullname().equals(rvName)) {
                return rv;
            }
        }

        // Retourner null si aucune catégorie ne correspond au nom fourni
        return null;
    }


    ///////////////////////////smssss


    @FXML
    void sendsms(ActionEvent event)
    {
        // Récupérez l'objet sélectionné dans le TableView
        Rendezvous objetSelectionne = tableview.getSelectionModel().getSelectedItem();

        if (objetSelectionne != null) {
            int id = objetSelectionne.getId(); // Supposons que getId() retourne l'identifiant de l'objet
            // Appelez la méthode pour mettre à jour l'état dans la base de données
            ps.modifierETAT(objetSelectionne);
            // Appelez la méthode du contrôleur Twilio pour envoyer le SMS de validation
            com.visita.controllers.TwilioSMS.envoyerSMS("phone number","Rendezvous confirmé !"); // Appelez la méthode main du contrôleur Twilio
        }



    }

///////////////////////////rappel//////////////
@FXML
void rappelrv(ActionEvent event)
{

    // Récupérer les rendez-vous à venir depuis la base de données
    List<Rendezvous> rendezVousAVenir =  ps.afficher();
    // Appeler la méthode pour envoyer les notifications de rappel par SMS
    ps.envoyerNotificationsSMS(rendezVousAVenir);

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






