package com.visita.services;

import com.visita.models.Category;
import com.visita.models.Service;
import com.visita.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Categoryservice implements ICategoryservice<Category> {
    private Connection connection = DataSource.getInstance().getConnection();
    @Override
    public void ajouter(Category category){
        String req = "INSERT INTO `category`(`nom`, `description`, `icon`) VALUES (?, ?, ?);";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, category.getNom());
            pst.setString(2, category.getDescription());
            pst.setString(3, category.getIcon());
            pst.executeUpdate();
            System.out.println("Category ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Category category) {
        String req = "DELETE FROM `category` WHERE `category`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, category.getId());
            pst.executeUpdate();
            System.out.println("Category supprimer avec succes !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Category category) {
        String req = "UPDATE `category` SET `nom` = ?, `description` = ?, `icon` = ? WHERE `category`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, category.getNom());
            pst.setString(2, category.getDescription());
            pst.setString(3, category.getIcon());
            pst.setInt(4, category.getId()); // Spécifiez le quatrième paramètre avec l'ID de la catégorie

            pst.executeUpdate();
            System.out.println("Category modifié avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Category> afficher() {
        List<Category> categories = new ArrayList();

        String req = "SELECT * FROM `category`";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("nom"), rs.getString("description"), rs.getString("icon")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return categories;
    }

    public boolean isCategoryNameUnique(String categoryName) {
        List<Category> categories = afficher(); // Récupérer toutes les catégories
        for (Category category : categories) {
            if (category.getNom().equalsIgnoreCase(categoryName)) {
                return false; // Nom de catégorie déjà existant
            }
        }
        return true; // Nom de catégorie unique
    }

}
