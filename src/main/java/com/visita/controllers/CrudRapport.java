package com.visita.controllers;
import com.visita.models.Admin;
import com.visita.models.Rapport;
import com.visita.models.Rendezvous;
import com.visita.models.User;
import com.visita.services.RapportService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrudRapport
{
    @FXML
    private TextField addRV_search;

    @FXML
    private AnchorPane addRpp_form;
    @FXML
    private Button btnrv;

    @FXML
    private Button btnclear;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdaterp;

    @FXML
    private TextField idrapport;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TableColumn<Rapport, String> note;

    @FXML
    private TextField noteid;

    @FXML
    private TextField rapportid;
    @FXML
    private TextField addRP_search;
    @FXML
    private TextField rendezvousid;

    @FXML
    private ChoiceBox<Rendezvous> addService_rendezvous;
    @FXML
    private TableColumn<Rapport, Rendezvous> rendezvous;

    @FXML
    private TableView<Rapport> tableview;

    @FXML
    private TableColumn<Rapport, String> type;

    @FXML
    private TextField typeid;

    private Admin loggedInAdmin;
    /*public CrudRapport(){

    }*/
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

    RapportService ps = new RapportService();

    @FXML
    void initialize() {
        // Liaison des propriétés des colonnes avec les propriétés des objets Rendezvous
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        note.setCellValueFactory(new PropertyValueFactory<>("note"));
        rendezvous.setCellValueFactory(new PropertyValueFactory<>("rendezvous_nom"));


        // Récupération de la liste des rendez-vous depuis le service ps
        List<Rapport> liste = ps.afficher();
        // Ajout des rendez-vous à la tableview
        tableview.getItems().addAll(liste);
        loadrv();

        // Gestionnaire d'événements pour la sélection de ligne dans le TableView
        tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Mettre à jour les champs texte avec les données de la ligne sélectionnée
                Rapport selectedRendezvous = tableview.getSelectionModel().getSelectedItem();
                typeid.setText(String.valueOf(selectedRendezvous.getType() ));
                noteid.setText(selectedRendezvous.getNote() );
                //rendezvousid.setText(String.valueOf(selectedRendezvous.getRendzvous_id() ));
               idrapport.setText(String.valueOf(selectedRendezvous.getId() ));
                // Définir le rv sélectionnée
                Rendezvous selectedrv = findrvByName(newSelection.getRendezvous_nom());
                addService_rendezvous.setValue(selectedrv);


                // Définir la valeur sélectionnée dans le ChoiceBox addService_rendezvous
                addService_rendezvous.setValue(selectedrv);





            }
          /*  else {
                // Réinitialiser les champs texte si aucune ligne n'est sélectionnée
                //medecinid.clear();
                noteid.clear();
                typeid.clear();

              // rendezvousid.clear();

            }

           */
        }
        );
        refreshChoiceBox();
    }

    @FXML
    void addrv(ActionEvent event)
    {


        // Obtenir l'ID de la catégorie sélectionnée
        Rendezvous selectedCategory = addService_rendezvous.getValue();
        int rendezvousId = selectedCategory != null ? selectedCategory.getId() : -1; // -1 si la catégorie n'est pas sélectionnée

        ps.ajouter(new Rapport(
                        rendezvousId, // medecin_id (supposant qu'il s'agit d'un champ texte)
                        typeid.getText(), // fullname (supposant qu'il s'agit d'un champ texte)

                        noteid.getText())
                 // rapport_id (supposant qu'il s'agit d'un champ texte)
        );




        chargerDonnees();




    }



    @FXML
    void clearrv(ActionEvent event) {
        //medecinid.clear();
        typeid.clear();
        noteid.clear();
        addService_rendezvous.setValue(null);

        //rendezvousid.clear();



    }
    private void chargerDonnees() {
        // Effacer les éléments existants dans le tableau
        tableview.getItems().clear();

        // Charger les nouvelles données dans le tableau
        List<Rapport> liste = ps.afficher();
        tableview.getItems().addAll(liste);
    }

    @FXML
    void close(ActionEvent event) {

    }

    @FXML
    void deleteRV() throws IOException {
// Obtenir le rv sélectionné dans la TableView
        Rapport selectedrv = tableview.getSelectionModel().getSelectedItem(); // Utiliser TableView avec une majuscule

        // Vérifier si une catégorie est sélectionnée
        if (selectedrv != null) {
            // Demander confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer ce Rapport ?");
            alert.setContentText("Rapport : " + selectedrv.getType() + "\n\nCette action est irréversible. Veuillez confirmer.");

            Optional<ButtonType> result = alert.showAndWait();

            // Si l'utilisateur confirme, supprimer rv
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer rv
                ps.supprimer(selectedrv);
                chargerDonnees();

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Suppression réussie");
                successAlert.setHeaderText("Rapport supprimée !");
                successAlert.setContentText("Le Rapport \"" + selectedrv.getType() + "\" a été supprimée avec succès.\n\n Opération réussie !");
                successAlert.showAndWait();
                refreshChoiceBox();
            }
        } else {
            // Aucune catégorie sélectionnée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sélection manquante");
            alert.setHeaderText("Erreur de sélection");
            alert.setContentText(" Il semble que vous n'ayez pas sélectionné un Rapport. Veuillez en choisir une à supprimer.");
            alert.showAndWait();
        }

    }


    @FXML
    void minimize(ActionEvent event) {

    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    void updaterapport(ActionEvent event) {
        // Obtenir l'ID de la catégorie sélectionnée
        Rendezvous selectedrv = addService_rendezvous.getValue();
        int rendezvousId = selectedrv != null ? selectedrv.getId() : -1; // -1 si la catégorie n'est pas sélectionnée

        ps.modifier(new Rapport(Integer.parseInt(idrapport.getText()),
                        rendezvousId, // medecin_id (supposant qu'il s'agit d'un champ texte)
                        typeid.getText(), // fullname (supposant qu'il s'agit d'un champ texte)

                        noteid.getText())
                // rapport_id (supposant qu'il s'agit d'un champ texte)
        );
       // System.out.println(rendezvousId);




        chargerDonnees();
    }



    private void loadrv() {
        // Récupérer la liste des catégories depuis le service
        List<Rendezvous> categories = RapportService.getAllrv();

        // Convertir la liste en ObservableList
        ObservableList<Rendezvous> observableCategories = FXCollections.observableArrayList(categories);

        // Assigner la liste observable au ChoiceBox
        addService_rendezvous.setItems(observableCategories);

        // Définir un convertisseur de chaîne pour afficher les noms de catégorie
        addService_rendezvous.setConverter(new StringConverter<Rendezvous>() {
            @Override
            public String toString(Rendezvous category) {
                return category == null ? "" : category.getFullname();
            }

            @Override
            public Rendezvous fromString(String string) {
                // Si vous souhaitez permettre à l'utilisateur d'ajouter de nouvelles catégories en saisissant du texte,
                // vous pouvez ajouter une logique ici pour créer une nouvelle catégorie à partir de la chaîne de texte.
                // Sinon, vous pouvez simplement retourner null ici.
                return null;
            }
        });
    }


    private void refreshChoiceBox() {
        // Effacer le contenu actuel du ChoiceBox
        addService_rendezvous.getItems().clear();

        // Récupérer les dernières données de la base de données
        List<Rendezvous> latestRendezvous = RapportService.getAllrv();

        // Ajouter les dernières données au ChoiceBox
        addService_rendezvous.getItems().addAll(latestRendezvous);

        // Définir un convertisseur de chaîne pour afficher les noms de rendez-vous
        addService_rendezvous.setConverter(new StringConverter<Rendezvous>() {
            @Override
            public String toString(Rendezvous rendezvous) {
                return rendezvous == null ? "" : rendezvous.getFullname();
            }

            @Override
            public Rendezvous fromString(String string) {
                // Si nécessaire, implémentez la logique pour convertir une chaîne en objet Rendezvous
                return null;
            }
        });
    }
    private Rendezvous findrvByName(String rvName) {
        // Obtenir la liste des catégories disponibles
        ObservableList<Rendezvous> rvs = addService_rendezvous.getItems();

        // Parcourir les catégories et trouver celle dont le nom correspond au nom fourni
        for (Rendezvous rv : rvs) {
            if (rv.getFullname().equals(rvName)) {
                return rv;
            }
        }

        // Retourner null si aucune catégorie ne correspond au nom fourni
        return null;
    }

    @FXML
    void searchrP() {
        // Get the user's search criteria
        String searchText = addRP_search.getText().toLowerCase();

        // Filter the list of patients based on the search criteria
        List<Rapport> allrv = ps.afficher(); // Get all patients
        List<Rapport> filtererv = new ArrayList<>();

        for (Rapport rp : allrv) {
            // Check if username, email, phone, or fullname of the patient matches the search criteria
            boolean matchesUsername = rp.getType().toLowerCase().contains(searchText);
            boolean matchesnote = rp.getNote().toLowerCase().contains(searchText);
            boolean matchesrv = rp.getRendezvous_nom().toLowerCase().contains(searchText);


            // If any of the fields match the search criteria, add the patient to the filtered list
            if (matchesUsername || matchesnote || matchesrv ) {
                filtererv.add(rp);
            }
        }

        // Update the TableView with the filtered patients
        ObservableList<Rapport> observableList = FXCollections.observableList(filtererv);
        tableview.setItems(observableList);


    }


    @FXML
    void actionrv (ActionEvent event) throws IOException {


        rv();

    }
    void  rv() throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/CrudRV.fxml"));
        Parent root=loader.load();
        CrudRV controller = loader.getController();

        // Pass the logged-in admin data to the controller
        controller.setLoggedInAdmin(loggedInAdmin);

        typeid.getScene().setRoot(root);


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
