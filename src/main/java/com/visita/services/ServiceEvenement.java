package com.visita.services;

import com.visita.models.CategoryEvent;
import com.visita.models.Evenement;
import com.visita.utils.DataSource;
import com.visita.Interface.IService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceEvenement implements IService<Evenement> {


    private Connection cnx;
    public ServiceEvenement() {
        cnx = DataSource.getInstance().getConnection();
    }
    @Override
    public void ajouter(Evenement evenement) throws SQLException {
        String req = "INSERT INTO evenement (image_evenement,type_evenement,nom_evenement,lieu_evenement, date_debut, date_fin,nb_participants,category_id) VALUES (?, ?, ?, ?, ?,?,?,?)";

        try (PreparedStatement pre = cnx.prepareStatement(req)) {
            pre.setString(1, evenement.getImage_evenement());
            pre.setString(2, evenement.getType_evenement());
            pre.setString(3, evenement.getNom_evenement());
            pre.setString(4, evenement.getLieu_evenement());
            pre.setString(5, String.valueOf(evenement.getDate_debut()));
            pre.setString(6, String.valueOf(evenement.getDate_fin()));
            pre.setInt(7,evenement.getNb_participants());
            pre.setInt(8,evenement.getCategory_id());



            pre.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding event: " + e.getMessage());
        }

    }

   @Override
   public void modifier(Evenement evenement) throws SQLException {
       String req = "UPDATE evenement SET image_evenement=?, type_evenement=?, nom_evenement=?, lieu_evenement=?, date_debut=?, date_fin=?, nb_participants=?, category_id=? WHERE id=?";
       try (PreparedStatement pre = cnx.prepareStatement(req)) {
           pre.setString(1, evenement.getImage_evenement());
           pre.setString(2, evenement.getType_evenement());
           pre.setString(3, evenement.getNom_evenement());
           pre.setString(4, evenement.getLieu_evenement());
           pre.setString(5, String.valueOf(evenement.getDate_debut()));
           pre.setString(6, String.valueOf(evenement.getDate_fin()));
           pre.setInt(7, evenement.getNb_participants());
           pre.setInt(8, evenement.getCategory_id());
           pre.setInt(9, evenement.getId());

           pre.executeUpdate();
       } catch (SQLException e) {
           System.err.println("Error modifying event: " + e.getMessage());
       }
   }

    @Override
    public void supprimer(Evenement evenement) throws SQLException {
        String req = "DELETE FROM evenement WHERE id=?";
        try (PreparedStatement pre = cnx.prepareStatement(req)) {
            pre.setInt(1, evenement.getId());
            pre.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting event: " + e.getMessage());
        }

    }
    public List<CategoryEvent> getCategoriesForEvents() throws SQLException {
        String req = "SELECT id FROM category_event";
        List<CategoryEvent> categories = new ArrayList<>();
        try (Statement ste = cnx.createStatement(); ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                int id = res.getInt("id");
                // Créez un nouvel objet CategoryEvent avec les données récupérées de la base de données
                CategoryEvent category = new CategoryEvent(id);
                categories.add(category);
            }
        }
        return categories;
    }

    public List<Integer> getCategoryIdsForEvents() throws SQLException {
        String req = "SELECT DISTINCT category_id FROM evenement";
        List<Integer> categoryIds = new ArrayList<>();
        try (Statement ste = cnx.createStatement(); ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                categoryIds.add(res.getInt("category_id"));
            }
        }
        return categoryIds;
    }


    @Override
    public List<Evenement> afficher() throws SQLException {
        String req = "SELECT evenement.*, category_event.id " +
                "FROM evenement " +
                "INNER JOIN category_event ON category_event.id = evenement.category_id";



        //String req = "SELECT evenement.*, category.id" + "FROM evenement " + " INNER JOIN  category ON category.id = evenement.category_id ";
        List<Evenement> list = new ArrayList<>();
        try (Statement ste = cnx.createStatement(); ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                Evenement evenement = new Evenement();
                //Evenement evenement = new Evenement();
                evenement.setId(res.getInt(1));
                evenement.setImage_evenement(res.getString(2));
                evenement.setType_evenement(res.getString(3));
                evenement.setNom_evenement(res.getString(4));
                evenement.setLieu_evenement(res.getString(5));
                evenement.setDate_debut(res.getTimestamp(6).toLocalDateTime());
                evenement.setDate_fin(res.getTimestamp(7).toLocalDateTime());
                evenement.setNb_participants(res.getInt(8));
                evenement.setCategory_id(res.getInt("category_id"));


                list.add(evenement);
            }
            return list;
        }

    }

    public List<Evenement> searchEvents(String keyword) throws SQLException {
        String req = "SELECT evenement.*, category_event.id " +
                "FROM evenement " +
                "INNER JOIN category_event ON category_event.id = evenement.category_id " +
                "WHERE evenement.nom_evenement LIKE ? OR evenement.type_evenement LIKE ?";

        List<Evenement> searchResults = new ArrayList<>();
        try (PreparedStatement pre = cnx.prepareStatement(req)) {
            pre.setString(1, "%" + keyword + "%");
            pre.setString(2, "%" + keyword + "%");

            try (ResultSet res = pre.executeQuery()) {
                while (res.next()) {
                    Evenement evenement = new Evenement();
                    evenement.setId(res.getInt(1));
                    evenement.setImage_evenement(res.getString(2));
                    evenement.setType_evenement(res.getString(3));
                    evenement.setNom_evenement(res.getString(4));
                    evenement.setLieu_evenement(res.getString(5));
                    evenement.setDate_debut(res.getTimestamp(6).toLocalDateTime());
                    evenement.setDate_fin(res.getTimestamp(7).toLocalDateTime());
                    evenement.setNb_participants(res.getInt(8));
                    evenement.setCategory_id(res.getInt("category_id"));

                    searchResults.add(evenement);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching events: " + e.getMessage());
        }

        return searchResults;
    }
    public List<Evenement> getEventsForPage(int pageNumber, int pageSize) throws SQLException {
        int offset = (pageNumber - 1) * pageSize;
        String req = "SELECT evenement.*, category_event.id " +
                "FROM evenement " +
                "INNER JOIN category_event ON category_event.id = evenement.category_id " +
                "LIMIT ? OFFSET ?";
        List<Evenement> eventsForPage = new ArrayList<>();
        try (PreparedStatement pre = cnx.prepareStatement(req)) {
            pre.setInt(1, pageSize);
            pre.setInt(2, offset);
            try (ResultSet res = pre.executeQuery()) {
                while (res.next()) {
                    Evenement evenement = new Evenement();
                    evenement.setId(res.getInt(1));
                    evenement.setImage_evenement(res.getString(2));
                    evenement.setType_evenement(res.getString(3));
                    evenement.setNom_evenement(res.getString(4));
                    evenement.setLieu_evenement(res.getString(5));
                    evenement.setDate_debut(res.getTimestamp(6).toLocalDateTime());
                    evenement.setDate_fin(res.getTimestamp(7).toLocalDateTime());
                    evenement.setNb_participants(res.getInt(8));
                    evenement.setCategory_id(res.getInt("category_id"));
                    eventsForPage.add(evenement);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting events for page: " + e.getMessage());
        }
        return eventsForPage;
    }
    public int getTotalCount() throws SQLException {
        String req = "SELECT COUNT(*) FROM evenement";
        int totalCount = 0;
        try (Statement ste = cnx.createStatement(); ResultSet res = ste.executeQuery(req)) {
            if (res.next()) {
                totalCount = res.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total count: " + e.getMessage());
        }
        return totalCount;
    }

    public int getCategoryIdByName(String categoryName) throws SQLException {
        String req = "SELECT id FROM category_event WHERE name = ?";
        try (PreparedStatement pre = cnx.prepareStatement(req)) {
            pre.setString(1, categoryName);
            try (ResultSet res = pre.executeQuery()) {
                if (res.next()) {
                    return res.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting category ID by name: " + e.getMessage());
        }
        return -1; // Return -1 if category name is not found
    }


}
