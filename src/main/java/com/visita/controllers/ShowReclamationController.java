package com.visita.controllers;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.visita.models.Reclamation;
import com.visita.utils.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;

public class ShowReclamationController {

    @FXML
    private TableView<Reclamation> reclamationTableView;

    @FXML
    private TableColumn<Reclamation, Integer> idColumn;

    @FXML
    private TableColumn<Reclamation, String> nomColumn;

    @FXML
    private TableColumn<Reclamation, String> categorieColumn;

    @FXML
    private TableColumn<Reclamation, String> sujetColumn;

    @FXML
    private TextField searchField;


    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;

    @FXML
    private TableColumn<Reclamation, String> subdateColumn;

    @FXML
    private Button backButton;

    @FXML
    private Button generatePdfButton; // Ajout du bouton pour générer le PDF
    private ObservableList<Reclamation> sortedReclamations = FXCollections.observableArrayList();
    // Déclaration de la liste de réclamations triée

    private String nomPrenom; // variable pour stocker les données saisies par l'utilisateur

    // Méthode pour définir les données saisies par l'utilisateur
    // Méthode pour définir les données saisies par l'utilisateur
    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
        loadReclamations(); // Charger les réclamations filtrées
    }


    @FXML
    public void initialize() {
        // Initialize columns
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        categorieColumn.setCellValueFactory(cellData -> cellData.getValue().categorieProperty());
        sujetColumn.setCellValueFactory(cellData -> cellData.getValue().sujetProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        subdateColumn.setCellValueFactory(cellData -> cellData.getValue().subdateProperty().asString());
        // Set custom cell factories for the buttons
        setupUpdateButtonColumn();
        setupDeleteButtonColumn();
        setupViewResponsesButtonColumn();
        setupGeneratePDFButtonColumn();
        // Load reclamations
        loadReclamations();
        // Tri des réclamations
        sortedReclamations = FXCollections.observableArrayList(reclamationTableView.getItems());
        Collections.sort(sortedReclamations, Comparator.comparing(Reclamation::getNom)); // Remplacez getNom par le getter approprié pour le champ utilisé pour la recherche

        // Écouteur pour le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                // Si le champ de recherche n'est pas vide, effectuer la recherche
                searchReclamationBySubject(newValue);
            } else {
                // Si le champ de recherche est vide, recharger toutes les réclamations
                loadReclamations();
            }
        });
        sortedReclamations.addAll(reclamationTableView.getItems());
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        // Call a method to refresh the table data
        refreshTable();
    }

    private void refreshTable() {
        // Implement code to refresh the table data
        loadReclamations();
    }

    private void setupUpdateButtonColumn() {
        TableColumn<Reclamation, Void> updateColumn = new TableColumn<>("Modifier");
        updateColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Modifier");

            {
                updateButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    openUpdateView(reclamation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        reclamationTableView.getColumns().add(updateColumn);
    }

    private void setupDeleteButtonColumn() {
        TableColumn<Reclamation, Void> deleteColumn = new TableColumn<>("Supprimer");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    deleteReclamation(reclamation);
                    reclamationTableView.getItems().remove(reclamation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        reclamationTableView.getColumns().add(deleteColumn);
    }

    public void loadReclamations() {
        ObservableList<Reclamation> reclamationList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM reclamation WHERE nom = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nomPrenom);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Reclamation reclamation = new Reclamation();
                    reclamation.setId(resultSet.getInt("id"));
                    reclamation.setNom(resultSet.getString("nom"));
                    reclamation.setCategorie(resultSet.getString("categorie"));
                    reclamation.setSujet(resultSet.getString("sujet"));
                    reclamation.setDescription(resultSet.getString("description"));
                    reclamation.setSubdate(resultSet.getTimestamp("subdate").toLocalDateTime());
                    reclamationList.add(reclamation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        reclamationTableView.setItems(reclamationList);
        sortedReclamations.clear(); // Clear the sortedReclamations list
        sortedReclamations.addAll(reclamationList); // Add all reclamations to the sortedReclamations list
    }


    private void deleteReclamation(Reclamation reclamation) {
        String sql = "DELETE FROM reclamation WHERE id=?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reclamation.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openUpdateView(Reclamation reclamation) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateReclamation.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
            UpdateReclamationController controller = loader.getController();
            controller.setReclamationToUpdate(reclamation);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupViewResponsesButtonColumn() {
        TableColumn<Reclamation, Void> viewResponsesColumn = new TableColumn<>("Voir Réponses");
        viewResponsesColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewResponsesButton = new Button("Voir Réponses");

            {
                viewResponsesButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    openViewResponsesView(reclamation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewResponsesButton);
                }
            }
        });

        reclamationTableView.getColumns().add(viewResponsesColumn);
    }

    private void openViewResponsesView(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewResponses.fxml"));
            Parent root = loader.load();

            ViewResponsesController controller = loader.getController();
            controller.setReclamation(reclamation);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Réponses pour la Réclamation: " + reclamation.getId());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            // Load the FXML file of the main scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent mainRoot = loader.load();

            // Create a new scene with the content of the main scene
            Scene mainScene = new Scene(mainRoot);

            // Get the main stage from the current scene
            Stage mainStage = (Stage) reclamationTableView.getScene().getWindow();

            // Set the main scene on the main stage
            mainStage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception if the FXML file cannot be loaded
        }

    }

    @FXML
    private void goToMainView(ActionEvent event) {
        // Implement the logic to navigate back to the main view
        // You can load the main view FXML and set it as the scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour effectuer une recherche et afficher la réclamation trouvée
    private void searchReclamationBySubject(String targetSubject) {
        // Effacer la liste des réclamations
        reclamationTableView.getItems().clear();

        // Parcourir la liste triée des réclamations
        for (Reclamation reclamation : sortedReclamations) {
            // Vérifier si le sujet de la réclamation commence par le texte entré
            if (reclamation.getSujet().toLowerCase().startsWith(targetSubject.toLowerCase())) {
                // Ajouter la réclamation correspondante à la table
                reclamationTableView.getItems().add(reclamation);
            }
        }
    }
    private void generatePdf() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("/reclamations.pdf"));
            document.open();
            for (Reclamation reclamation : reclamationTableView.getItems()) {
                document.add(new Paragraph("ID: " + reclamation.getId()));
                document.add(new Paragraph("Nom: " + reclamation.getNom()));
                document.add(new Paragraph("Catégorie: " + reclamation.getCategorie()));
                document.add(new Paragraph("Sujet: " + reclamation.getSujet()));
                document.add(new Paragraph("Description: " + reclamation.getDescription()));
                document.add(new Paragraph("Date de soumission: " + reclamation.getSubdate().toString()));
                document.add(new Paragraph("\n"));
            }
            document.close();
            // Ouvrir le PDF généré avec l'application par défaut
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File("reclamations.pdf"));
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
    private void setupGeneratePDFButtonColumn() {
        TableColumn<Reclamation, Void> generatePDFColumn = new TableColumn<>("Generate PDF");
        generatePDFColumn.setCellFactory(param -> new TableCell<>() {
            private final Button generatePDFButton = new Button("Generate PDF");

            {
                generatePDFButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    generatePDF(reclamation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(generatePDFButton);
                }
            }
        });

        reclamationTableView.getColumns().add(generatePDFColumn);
    }

    private void generatePDF(Reclamation reclamation) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("reclamation_" + reclamation.getId() + ".pdf"));
            document.open();




            // Titre principal
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Votre Réclamation", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Espacement
            document.add(Chunk.NEWLINE);

            // Séparateur
            LineSeparator separator = new LineSeparator();
            separator.setLineColor(BaseColor.LIGHT_GRAY);
            separator.setLineWidth(2);
            document.add(new Chunk(separator));

            // Informations de la réclamation
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
            addEmptyLine(document, 1);

            // Ajout des informations avec une couleur différente pour les titres
            Font titleInfoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
            document.add(new Paragraph("Nom:", titleInfoFont));
            document.add(new Paragraph(reclamation.getNom(), normalFont));
            document.add(new Paragraph("Catégorie:", titleInfoFont));
            document.add(new Paragraph(reclamation.getCategorie(), normalFont));
            document.add(new Paragraph("Sujet:", titleInfoFont));
            document.add(new Paragraph(reclamation.getSujet(), normalFont));
            document.add(new Paragraph("Description:", titleInfoFont));
            document.add(new Paragraph(reclamation.getDescription(), normalFont));
            document.add(new Paragraph("Date de soumission:", titleInfoFont));
            document.add(new Paragraph(reclamation.getSubdate().toString(), normalFont));

            document.close();

            // Ouvrir le PDF généré avec l'application par défaut
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File("reclamation_" + reclamation.getId() + ".pdf"));
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }


    private void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }

    }


}

