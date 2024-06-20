package com.visita.services;

import com.visita.models.Response;
import com.visita.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResponseDAO {

    // Méthode pour insérer une réponse dans la base de données
    public static void insertResponse(Response response) throws SQLException {
        String sql = "INSERT INTO reponse (reclamation_id, author, response_content, response_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, response.getReclamationId());
            statement.setString(2, response.getAuthor());
            statement.setString(3, response.getResponseContent());
            statement.executeUpdate();
        }
    }

    // Méthode pour récupérer toutes les réponses d'une réclamation spécifique
    public static List<Response> getResponsesByReclamationId(int reclamationId) throws SQLException {
        List<Response> responses = new ArrayList<>();
        String sql = "SELECT * FROM reponse WHERE reclamation_id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reclamationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Response response = new Response();
                    response.setId(resultSet.getInt("id"));
                    response.setReclamationId(resultSet.getInt("reclamation_id"));
                    response.setAuthor(resultSet.getString("author"));
                    response.setResponseContent(resultSet.getString("response_content"));
                    response.setResponseDate(resultSet.getTimestamp("response_date").toLocalDateTime());
                    responses.add(response);
                }
            }
        }
        return responses;
    }

    // Méthode pour mettre à jour une réponse dans la base de données
    public static void updateResponse(Response response) throws SQLException {
        String sql = "UPDATE reponse SET response_content = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, response.getResponseContent());
            statement.setInt(2, response.getId());
            statement.executeUpdate();
        }
    }

    // Méthode pour supprimer une réponse de la base de données
    public static void deleteResponse(int responseId) throws SQLException {
        String sql = "DELETE FROM reponse WHERE id = ?";
        try (Connection connection =DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, responseId);
            statement.executeUpdate();
        }
    }
}
