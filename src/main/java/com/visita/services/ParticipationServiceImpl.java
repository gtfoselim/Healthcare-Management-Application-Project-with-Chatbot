package com.visita.services;



import com.visita.models.Participation;
import com.visita.Interface.IService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParticipationServiceImpl implements IService<Participation> {
    private Connection cnx;

    public ParticipationServiceImpl(Connection connection) {
        this.cnx = connection;
    }


    @Override
    public void ajouter(Participation participation) throws SQLException {
        String query = "INSERT INTO participant (date_participation, evenement_id, description, medecin_id) VALUES (?,?,?,?)";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setObject(1, participation.getDate_participation());
            statement.setInt(2, participation.getEvenement_id());
            statement.setString(3, participation.getDescription());
            statement.setInt(4, participation.getMedecin_id());
            statement.executeUpdate();
        }
    }
    public boolean checkIdsExist(int evenementId, int medecinId) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM participant WHERE evenement_id = ? AND  medecin_id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, evenementId);
            statement.setInt(2, medecinId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }


    public void updateNbrParticipants(int evenementId, int change) throws SQLException {
        String query = "UPDATE evenement SET nb_participants = nb_participants - ? WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, change);
            statement.setInt(2, evenementId);
            statement.executeUpdate();
        }
    }

    @Override
    public void modifier(Participation participation) throws SQLException {
        String query = "UPDATE participant SET date_participation=?, evenement_id=?, description=?, medecin_id=? WHERE id=?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setObject(1, participation.getDate_participation());
            statement.setInt(2, participation.getEvenement_id());
            statement.setString(3, participation.getDescription());
            statement.setInt(4, participation.getMedecin_id());
            statement.setInt(5, participation.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void supprimer(Participation participation) throws SQLException {
        String query = "DELETE FROM participant WHERE id=?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, participation.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public List<Participation> afficher() throws SQLException {
        List<Participation> participations = new ArrayList<>();
        String query = "SELECT * FROM participant";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    LocalDateTime date_participation = resultSet.getObject("date_participation", LocalDateTime.class);
                    int evenement_id = resultSet.getInt("evenement_id");
                    String description = resultSet.getString("description");
                    int medecin_id = resultSet.getInt("medecin_id");
                    Participation participation = new Participation(id, date_participation, evenement_id, description, medecin_id);
                    participations.add(participation);
                }
            }
        }
        return participations;
    }

    public Participation getById(int id) throws SQLException {
        String query = "SELECT * FROM participant WHERE id=?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    LocalDateTime date_participation = resultSet.getObject("date_participation", LocalDateTime.class);
                    int evenement_id = resultSet.getInt("evenement_id");
                    String description = resultSet.getString("description");
                    int medecin_id = resultSet.getInt("medecin_id");
                    return new Participation(id, date_participation, evenement_id, description, medecin_id);
                }
            }
        }
        return null;
    }
}