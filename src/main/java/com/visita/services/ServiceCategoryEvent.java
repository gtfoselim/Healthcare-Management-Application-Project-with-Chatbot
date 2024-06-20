package com.visita.services;

import com.visita.models.CategoryEvent;
import com.visita.utils.DataSource;
import com.visita.Interface.IService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategoryEvent implements IService<CategoryEvent> {


    private Connection cnx;

    public ServiceCategoryEvent() {
        cnx = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(CategoryEvent categoryEvent) throws SQLException {
        String req = "INSERT INTO category_event (name,description) VALUES (?,?)";

        try (PreparedStatement pre = cnx.prepareStatement(req)) {
            pre.setString(1, categoryEvent.getName());
            pre.setString(2, categoryEvent.getDescription());
            pre.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding event: " + e.getMessage());
        }

    }
    // Add the following method to your ServiceCategoryEvent class

    public CategoryEvent getCategoryByName(String name) throws SQLException {
        List<CategoryEvent> categories = loadCategories();
        for (CategoryEvent category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public List<Integer> listevenement() {
        String req = "SELECT id FROM category_event";
        List<Integer> list = new ArrayList<>();
        try (Statement ste = cnx.createStatement(); ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                int idd = res.getInt(1); // Use index 1 to get the value of the first (and only) column
                list.add(idd);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving events: " + e.getMessage());
        }
        return list;
    }

    public List<CategoryEvent> getAllCategories() {
        List<CategoryEvent> categories = new ArrayList<>();
        String req = "SELECT * FROM category_event;";

        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Récupérer les colonnes de la base de données
                int id = rs.getInt("id");
                String nom = rs.getString("name");
                String description = rs.getString("description");


                // Créer un objet Category à partir des résultats
                CategoryEvent category = new CategoryEvent(id, nom, description);

                // Ajouter la catégorie à la liste
                categories.add(category);
            }

            // Fermer le ResultSet et le PreparedStatement
            rs.close();
            pst.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return categories;
    }
    public List<CategoryEvent> loadCategories() throws SQLException {
        String query = "SELECT * FROM category_event";
        List<CategoryEvent> categories = new ArrayList<>();

        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");

                CategoryEvent category = new CategoryEvent(id, name, description);
                categories.add(category);
            }
        }

        return categories;
    }


    @Override
    public void modifier(CategoryEvent categoryEvent) throws SQLException {
        String req = "UPDATE category_event SET name=?, description=? WHERE id=?";
        try (PreparedStatement pre = cnx.prepareStatement(req)) {
            pre.setString(1, categoryEvent.getName());
            pre.setString(2, categoryEvent.getDescription());
            pre.setInt(3, categoryEvent.getId());
            pre.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error modifying event: " + e.getMessage());
        }
    }





    @Override
    public void supprimer(CategoryEvent categoryEvent) throws SQLException {
        String req = "DELETE FROM category_event WHERE id=?";
        try (PreparedStatement pre = cnx.prepareStatement(req)) {
            pre.setInt(1, categoryEvent.getId());
            pre.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting event: " + e.getMessage());
        }
    }


    @Override
    public List<CategoryEvent> afficher() throws SQLException {
        String req = "SELECT * FROM category_event";
        List<CategoryEvent> list = new ArrayList<>();
        try (Statement ste = cnx.createStatement(); ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                CategoryEvent categoryEvent = new CategoryEvent();

                // Assurez-vous d'utiliser les noms de colonnes corrects dans le ResultSet
                categoryEvent.setName(res.getString("name"));
                categoryEvent.setDescription(res.getString("description"));

                list.add(categoryEvent);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving category events: " + e.getMessage());
        }
        return list;
    }

    public boolean existCategory(int categoryId) throws SQLException {
        String query = "SELECT id FROM category_event WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Renvoie vrai si la catégorie avec cet ID existe
            }
        }
    }


}

