package com.visita.services;

import com.visita.models.react;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReactServiceImpl {
    private Connection cnx;

    public ReactServiceImpl(Connection connection) {
        this.cnx = connection;
    }

    public boolean checkIdsExist(int userId, int eventId) throws SQLException {
        String query = "SELECT COUNT(*) FROM react WHERE iduser = ? AND idevent = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, eventId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public void addReact(react react) throws SQLException {
        String query = "INSERT INTO react (iduser, idevent, reactContent) VALUES (?, ?, ?)";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, react.getUserId());
            statement.setInt(2, react.getEventId());
            statement.setString(3, react.getReactContent());
            statement.executeUpdate();
        }
    }

    public void updateReactContent(int userId, int eventId, String newReactContent) throws SQLException {
        String query = "UPDATE react SET reactContent = ? WHERE iduser = ? AND idevent = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, newReactContent);
            statement.setInt(2, userId);
            statement.setInt(3, eventId);
            statement.executeUpdate();
        }
    }
    public int countReactionsByContent(int eventId, String content) throws SQLException {
        String query = "SELECT COUNT(*) FROM react WHERE idevent = ? AND reactContent = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, eventId);
            statement.setString(2, content);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }

    public int countGoingReactions(int eventId) throws SQLException {
        return countReactionsByContent(eventId, "going");
    }

    public int countNotInterestedReactions(int eventId) throws SQLException {
        return countReactionsByContent(eventId, "notinterested");
    }

    public int countInterestedReactions(int eventId) throws SQLException {
        return countReactionsByContent(eventId, "interested");
    }
}
