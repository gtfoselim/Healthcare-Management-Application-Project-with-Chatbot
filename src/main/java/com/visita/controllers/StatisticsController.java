package com.visita.controllers;

import com.visita.utils.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {

    @FXML
    private PieChart statisticsPieChart;
    private Map<String, Integer> retrieveStatisticsDataFromDatabase() {
        Map<String, Integer> statisticsData = new HashMap<>();

        // Etablir la connexion à la base de données
        try (Connection connection = DatabaseConnector.getConnection()) {
            // Préparer la requête SQL pour récupérer les statistiques des réclamations
            String sql = "SELECT categorie, COUNT(*) AS count FROM reclamation GROUP BY categorie";

            // Créer une instruction préparée
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Exécuter la requête SQL et récupérer les résultats
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Parcourir les résultats et les ajouter à la map des données statistiques
                    while (resultSet.next()) {
                        String category = resultSet.getString("categorie");
                        int count = resultSet.getInt("count");
                        statisticsData.put(category, count);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception, par exemple en affichant un message d'erreur à l'utilisateur
        }

        return statisticsData;
    }


    // Méthode pour mettre à jour les données statistiques
    public void updateStatisticsData() {
        // Effacer les données existantes du graphique
        statisticsPieChart.getData().clear();

        // Charger à nouveau les nouvelles données statistiques
        loadStatisticsData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load statistics data
        loadStatisticsData();
    }

    private void loadStatisticsData() {
        // Suppose you have a method to retrieve statistics data from the database
        Map<String, Integer> statisticsData = retrieveStatisticsDataFromDatabase();

        // Clear existing data
        statisticsPieChart.getData().clear();

        // Add new data
        statisticsData.forEach((category, count) -> {
            PieChart.Data data = new PieChart.Data(category, count);
            statisticsPieChart.getData().add(data);
        });
    }

    private String getCategoryColor(String category) {
        // You can define custom colors for each category here
        // For demonstration purpose, let's use some predefined colors
        switch (category) {
            case "Technical":
                return "#2196F3"; // Blue
            case "Customer Service":
                return "#FFC107"; // Amber
            case "Billing":
                return "#4CAF50"; // Green
            case "Product Quality":
                return "#FF5722"; // Deep Orange
            default:
                return "#000000"; // Black (default color)
        }
    }
}
