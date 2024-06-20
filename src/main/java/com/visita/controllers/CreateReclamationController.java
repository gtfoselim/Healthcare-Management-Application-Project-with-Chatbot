package com.visita.controllers;

import com.visita.models.Reclamation;
import com.visita.services.ReclamationDAO;

import java.time.LocalDateTime;

public class CreateReclamationController {

    public boolean createReclamation(String nom, String categorie, String sujet, String description, String email) {
        // Vérifier si l'e-mail est vide
        if (email.isEmpty()) {
            System.err.println("Error: Email cannot be empty.");
            return false;
        }

        // Vérifier si toutes les autres informations requises sont présentes
        if (nom == null || categorie == null || sujet == null || description == null) {
            System.err.println("Error: Missing required information.");
            return false;
        }

        // Créer une nouvelle réclamation
        Reclamation reclamation = new Reclamation();
        reclamation.setNom(nom);
        reclamation.setCategorie(categorie);
        reclamation.setSujet(sujet);
        reclamation.setDescription(description);
        reclamation.setSubdate(LocalDateTime.now());
        reclamation.setEmail(email);

        // Enregistrer la réclamation dans la base de données
        boolean success = ReclamationDAO.saveReclamationToDatabase(reclamation);

        // Afficher un message en fonction du succès de l'opération
        if (success) {
            System.out.println("Reclamation created successfully.");
        } else {
            System.err.println("Failed to create reclamation.");
        }

        return success;
    }
}
