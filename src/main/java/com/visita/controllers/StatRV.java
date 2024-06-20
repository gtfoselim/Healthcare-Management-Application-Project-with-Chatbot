package com.visita.controllers;

import com.visita.models.Admin;
import com.visita.models.User;
import com.visita.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StatRV
{
    @FXML
    private Button btnclose;
    private Admin loggedInAdmin;

    @FXML
    private PieChart piechart;

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
    private static Connection connection = DataSource.getInstance().getConnection();

    // Méthode appelée lors de l'initialisation du contrôleur
    public void initialize() {
        // Appeler une méthode pour récupérer les données de la base de données
        Map<String, Integer> data = fetchDataFromDatabase();

        // Appeler une méthode pour traiter les données et les adapter au PieChart
        List<PieChart.Data> pieChartData = processData(data);

        // Ajouter les données au PieChart
        piechart.setData(FXCollections.observableArrayList(pieChartData));

    }

    // Méthode pour récupérer les données de la base de données (à remplacer par votre propre méthode de récupération de données)
    private Map<String, Integer> fetchDataFromDatabase() {



            // Créer une map pour stocker les données récupérées
            Map<String, Integer> data = new HashMap<>();

            // Exécuter la requête SQL pour récupérer les données
            String sql = "SELECT medecin.fullname, COUNT(rendezvous.id) AS nb_rendezvous\n" +
                    "FROM medecin\n" +
                    "LEFT JOIN rendezvous ON medecin.id = rendezvous.medecin_id\n" +
                    "WHERE medecin.role = '[\"medecin\"]'\n" +
                    "GROUP BY medecin.id;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql) ;
                 ResultSet rs = preparedStatement.executeQuery(sql)) {
                // Parcourir les résultats de la requête et les stocker dans la map
                while (rs.next()) {
                    String medecinNom = rs.getString("fullname");
                    int nbRendezvous = rs.getInt("nb_rendezvous");
                    data.put(medecinNom, nbRendezvous);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer les erreurs de connexion ou de requête
            }

            return data;
        }




    // Méthode pour traiter les données et les adapter au PieChart
    private List<PieChart.Data> processData(Map<String, Integer> data) {
        // Code pour traiter les données et les adapter au format requis par PieChart
        // Exemple de traitement : convertir les entrées de la map en PieChart.Data
        List<PieChart.Data> pieChartData = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            // Créer une étiquette pour le médecin avec le nombre de rendez-vous
            String label = entry.getKey() + " (" + entry.getValue() + ")";
            PieChart.Data slice = new PieChart.Data(label, entry.getValue());
            pieChartData.add(slice);
        }
        return pieChartData;
    }

    @FXML
    void close(ActionEvent event) throws IOException {
        back1();

    }

    void  back1() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrudRV.fxml"));
        Parent root = loader.load();
        CrudRV controller = loader.getController();

        // Pass the logged-in admin data to the controller
        controller.setLoggedInAdmin(loggedInAdmin);
        // Utilisez le bouton btnback pour obtenir sa scène parente
        Scene currentScene = btnclose.getScene();

        // Remplacez la racine de la scène actuelle par la racine de la nouvelle vue chargée
        currentScene.setRoot(root);


    }
    @FXML
    void minimize(ActionEvent event) {

    }





}
