package com.visita.services;

import com.visita.models.ReponseEvaluation;
import com.visita.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReponseEvaluationDAO {

    public void addEvaluation(ReponseEvaluation evaluation) {
        String sql = "INSERT INTO response_evaluation (reponse_id, reclamation_id, rating) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, evaluation.getReponseId());
            statement.setInt(2, evaluation.getReclamationId());
            statement.setInt(3, evaluation.getEvaluation());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

