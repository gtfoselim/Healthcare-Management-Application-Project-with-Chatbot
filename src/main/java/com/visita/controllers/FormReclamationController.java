package com.visita.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FormReclamationController {

    @FXML
    private TextField nameTextField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField subjectTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField emailTextField;

    // Define minimum and maximum lengths for each field
    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 50;
    private static final int SUBJECT_MIN_LENGTH = 5;
    private static final int SUBJECT_MAX_LENGTH = 100;
    private static final int DESCRIPTION_MIN_LENGTH = 10;
    private static final int DESCRIPTION_MAX_LENGTH = 500;
    private static final int EMAIL_MIN_LENGTH = 5;
    private static final int EMAIL_MAX_LENGTH = 50;

    // Define bad words
    private static final String[] BAD_WORDS = {
            // Mots en français
            "insulte", "injure", "vulgaire", "obscène", "offensant", "raciste", "sexiste",
            "pornographique", "haine", "violence", "menace", "dégradant", "harceler",
            "discrimination", "putain", "merde", "enculé", "salope", "connard", "cul", "bite",
            "criss", "tabarnak", "salaud", "nazi", "antisémite", "homophobe", "islamophobe",
            "xénophobe", "facho", "fasciste", "pédé", "pd", "transphobe", "fils de pute",
            "niquer", "baiser", "cochon", "foufoune", "pédophile", "viol", "sodomie", "inceste",
            // Mots en anglais
            "fuck", "shit", "asshole", "bitch", "dick", "cunt", "bastard", "cock", "pussy",
            "motherfucker", "whore", "slut", "faggot", "nigger", "retard", "gay", "lesbian",
            "tranny", "fag", "dyke", "jerk", "idiot", "douchebag", "twat", "wanker", "cunt",
            "screw", "blowjob", "orgasm", "rape", "masturbate", "penis", "vagina", "anal",
            "anus", "boobs", "tits", "nipples", "testicles", "scrotum", "clitoris", "sexual",
            "orgy", "fuckboy", "cuck", "prostitute", "fuckwit", "bastard", "shithead",
            "motherfucking", "wank", "dumbass", "fisting", "cannabis", "cocaine", "heroin",
            "meth", "drugs", "kill", "murder", "suicide", "death", "blood", "violence", "gun",
            "bomb", "terrorist", "terror", "hate", "racist", "sexist", "xenophobe", "bigot",
            "nazi", "holocaust", "arson", "kidnap", "pedophile", "paedophile", "porn", "naked",
            "nude", "sex", "erotic", "masturbation", "vibrator", "strap-on", "dildo", "lube",
            "condom", "pubic", "genital", "orgasm", "ejaculate", "sperm", "period", "menstruation",
            "abortion", "pregnant", "pregnancy", "child", "abuse", "incest", "bestiality", "kinky",
            "perv", "pervert", "beast", "sick", "gross", "disgusting", "fetish", "pee", "piss",
            "scat", "shitstorm", "stfu", "kys", "gtfo", "lmao", "lmfao", "rofl", "wtf", "omg"
    };


    @FXML
    public void initialize() {
        // Populate the categoryComboBox with predefined category choices
        categoryComboBox.getItems().addAll("Technical", "Design/UI", "Contenu", "Sécurité", "Communication/Support");
        // Optionally, you can set a default value or prompt text
        categoryComboBox.setPromptText("Select Category");
    }

    @FXML
    private void submitReclamation() {
        String nom = nameTextField.getText().trim();
        String categorie = categoryComboBox.getValue();
        String sujet = subjectTextField.getText().trim();
        String description = descriptionTextArea.getText().trim();
        String email = emailTextField.getText().trim(); // Capture the email entered by the user

        // Perform input validation
        if (nom.isEmpty() || categorie == null || sujet.isEmpty() || description.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
        } else if (nom.length() < NAME_MIN_LENGTH || nom.length() > NAME_MAX_LENGTH) {
            showAlert(Alert.AlertType.ERROR, "Error", "Name must be between " + NAME_MIN_LENGTH + " and " + NAME_MAX_LENGTH + " characters.");
        } else if (sujet.length() < SUBJECT_MIN_LENGTH || sujet.length() > SUBJECT_MAX_LENGTH) {
            showAlert(Alert.AlertType.ERROR, "Error", "Subject must be between " + SUBJECT_MIN_LENGTH + " and " + SUBJECT_MAX_LENGTH + " characters.");
        } else if (description.length() < DESCRIPTION_MIN_LENGTH || description.length() > DESCRIPTION_MAX_LENGTH) {
            showAlert(Alert.AlertType.ERROR, "Error", "Description must be between " + DESCRIPTION_MIN_LENGTH + " and " + DESCRIPTION_MAX_LENGTH + " characters.");
        } else if (email.length() < EMAIL_MIN_LENGTH || email.length() > EMAIL_MAX_LENGTH) {
            showAlert(Alert.AlertType.ERROR, "Error", "Email must be between " + EMAIL_MIN_LENGTH + " and " + EMAIL_MAX_LENGTH + " characters.");
        } else if (!isValidEmail(email)) { // Validate email format
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid email address.");
        } else if (containsBadWords(description)) { // Check for bad words in description
            showAlert(Alert.AlertType.ERROR, "Error", "Your description contains inappropriate words.");
        } else {
            // Optionally, you can perform additional validation here if needed
            // If validation passes, proceed with creating the reclamation
            CreateReclamationController createReclamationController = new CreateReclamationController();
            createReclamationController.createReclamation(nom, categorie, sujet, description, email);
        }
    }

    // Method to validate the email format
    private boolean isValidEmail(String email) {
        // Here, you can implement email validity check according to your requirements
        // For example, you can use regular expressions or an email validation library
        // For simplicity, we will just check if it contains "@" and "."
        return email.contains("@") && email.contains(".");
    }

    // Method to check if description contains bad words
    private boolean containsBadWords(String description) {
        for (String word : BAD_WORDS) {
            if (description.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
