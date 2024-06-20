package com.visita.services;

import com.visita.models.Category;
import com.visita.models.Service;
import com.visita.utils.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Date;

public class ServiceService  {

    private Connection connection = DataSource.getInstance().getConnection();


    public void ajouter(Service service) {
        String req = "INSERT INTO `service`(nom, description, date_cr, image, id_categorie_id, active) VALUES (?, ?, ?, ?, ?, ?);";

        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, service.getNom());
            pst.setString(2, service.getDescription());
            pst.setDate(3, service.getDateCreation());
            pst.setString(4, service.getImage());
            pst.setInt(5, service.getCategory_id());
            pst.setBoolean(6, service.isActive());

            pst.executeUpdate();
            System.out.println("Service ajouté avec succès !");

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du service : " + e.getMessage());
            // Vous pouvez également envisager de lancer une exception personnalisée ici pour informer le code appelant
        }
    }



    public void supprimer(Service service) {
        String req = "DELETE FROM service WHERE service.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, service.getId());
            pst.executeUpdate();
            System.out.println("Service supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void modifier(Service service) {
        String req = "UPDATE service SET nom = ?, description = ?, date_cr = ?,`image` = ?, id_categorie_id = ?, active = ? WHERE service.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, service.getNom());
            pst.setString(2, service.getDescription());
            pst.setDate(3,service.getDateCreation());
            pst.setString(4, service.getImage());
            pst.setInt(5, service.getCategory_id());
            pst.setBoolean(6, service.isActive());
            pst.setInt(7, service.getId());
            pst.executeUpdate();
            System.out.println("Service modifié avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // Surcharge de la méthode modifier pour accepter un nouveau nom en plus de l'objet Service
    public void modifier(Service service, String nouveauNom) {
        service.setImage(nouveauNom);
        modifier(service);
    }

    public List<Service> afficher() {
        List<Service> services = new ArrayList<>();

        String req = "SELECT s.*, c.nom AS category_nom " +
                "FROM service s " +
                "INNER JOIN category c ON s.id_categorie_id = c.id"; // Requête SQL avec la jointure

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                java.sql.Date dateCreation = rs.getDate("date_cr");
                String imageName = rs.getString("image"); // Récupérer le nom de l'image seulement
                boolean active = rs.getBoolean("active");
                int category_id = rs.getInt("id_categorie_id");
                String category_nom = rs.getString("category_nom");

                // Create a Service object
                Service service = new Service(id, nom, description, dateCreation, imageName, category_id, active); // Utilisez le nom de l'image
                service.setCategory_nom(category_nom); // Set the category name
                // Add the Service object to the list
                services.add(service);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return services;
    }

    public List<Service> afficherActiveServices() {
        List<Service> services = new ArrayList<>();

        String req = "SELECT s.*, c.nom AS category_nom " +
                "FROM service s " +
                "INNER JOIN category c ON s.id_categorie_id = c.id " +
                "WHERE s.active = ?"; // Ajouter une clause WHERE pour filtrer les services actifs

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setBoolean(1, true); // Indiquer que vous voulez uniquement les services actifs
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                java.sql.Date dateCreation = rs.getDate("date_cr");
                String imageName = rs.getString("image"); // Récupérer le nom de l'image seulement
                boolean active = rs.getBoolean("active");
                int category_id = rs.getInt("id_categorie_id");
                String category_nom = rs.getString("category_nom");

                // Create a Service object
                Service service = new Service(id, nom, description, dateCreation, imageName, category_id, active);
                service.setCategory_nom(category_nom); // Définir le nom de la catégorie
                // Add the service to the list
                services.add(service);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return services;
    }


    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String req = "SELECT * FROM category;";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Récupérer les colonnes de la base de données
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                String icon = rs.getString("icon");

                // Créer un objet Category à partir des résultats
                Category category = new Category(id, nom, description, icon);

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





}