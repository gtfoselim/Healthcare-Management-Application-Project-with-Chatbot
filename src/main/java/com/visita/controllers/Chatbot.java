package com.visita.controllers;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.visita.models.Patient;
import com.visita.models.User;
import javafx.stage.Window;
import org.json.JSONArray;
import org.json.JSONObject;
import com.visita.services.PatientService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.apache.commons.text.similarity.LevenshteinDistance;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Chatbot implements Initializable {
    @FXML
    private TextField questionField;
    @FXML
    private ScrollPane responseScrollPane;
    @FXML
    private Button readChatAnswer;
    private boolean isSpeaking = false;

    private Thread textToSpeechThread;


    @FXML
    private TextArea responseField;
    private Patient loggedInPatient;
    @FXML
    private Button updateButton;
    private PatientService patientService = new PatientService();

    public Chatbot() {
        // Create PatientService with DataSource instance
        this.patientService = new PatientService();
    }

    public void initData(User user) {
        // Check if the provided user is an instance of Patient
        if (user instanceof Patient) {
            // If the user is a Patient, cast it to Patient and initialize labels
            Patient patient = (Patient) user;

        } else {
            // Handle other types of users or show an error message
            showAlert("Error", "Invalid user type");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Check if the loggedInPatient object is not null
        if (loggedInPatient != null) {
            // Initialize the view with patient data

        } else {
            // Display an error message if the loggedInPatient object is null

        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String[] healthKeywords = {
            "health", "medical", "doctor", "hospital", "wellness", "fitness", "nutrition",
            "disease", "illness", "symptoms", "treatment", "therapy", "diagnosis", "medicine",
            "vaccine", "infection", "exercise", "diet", "mental health", "physical health",
            "public health", "healthy lifestyle", "healthcare", "pharmacy", "immunity", "pandemic",
            "emergency", "surgery", "prescription", "checkup", "virus", "bacteria", "contagious",
            "recovery", "pain", "stress", "chronic", "acute", "blood pressure", "heart rate",
            "respiratory", "immune system", "cancer", "stroke", "diabetes", "allergy", "asthma",
            "obesity", "nutrition", "sleep", "dietary", "mental illness", "addiction", "vaccination",
            "pandemic", "epidemic", "quarantine", "pandemic", "outbreak", "coronavirus", "COVID-19",
            // Add more health-related keywords here
            "hospice", "hypertension", "hormones", "anxiety", "depression", "alcoholism", "smoking",
            "tobacco", "sugar intake", "sugar levels", "exercise routine", "workout plan", "mental wellness",
            "physical therapy", "occupational therapy", "cardiovascular health", "blood sugar", "glucose levels",
            "immune response", "viral infection", "bacterial infection", "cognitive health", "brain health",
            "neurological disorders", "digestive health", "gastrointestinal issues", "hydration", "water intake",
            "health screening", "preventive care", "medical history", "family medical history", "chronic condition",
            "acute condition", "acute care", "chronic care", "chronic disease", "respiratory infection",
            "respiratory illness", "heart disease", "heart condition", "lung health", "kidney health",
            "liver health", "bone health", "osteoporosis", "arthritis", "joint pain", "muscle pain",
            "menstrual health", "menstrual cycle", "menopause", "reproductive health", "sexual health",
            "sexual wellness", "pregnancy", "fertility", "contraception", "birth control", "STDs", "STIs",
            "sexually transmitted diseases", "sexually transmitted infections", "mental wellbeing",
            "emotional health", "stress management", "mental resilience", "emotional resilience",
            "substance abuse", "drug addiction", "drug abuse", "alcohol addiction", "alcohol abuse",
            "nutritional supplements", "vitamins", "minerals", "nutrient intake", "dietary supplements"
    };



    @FXML
    void submitQuestion(ActionEvent event) {
        // Clear the response field and stop any ongoing typing animation
        responseField.clear();

        // Get the question entered by the user
        String question = questionField.getText().trim();

        // Check if the question is related to health
        if (isHealthQuestion(question)) {
            // If it's a health-related question, proceed to get the chatbot response
            String response = getChatbotResponse("Answer me in Healthcare Field " + question);

            // Clear the question field
            questionField.clear();

            // Append the question bubble to the response field
            appendBubble("You", question);

            // Append the response bubble to the response field
            appendBubble("Chatbot", response);

            // Scroll to the bottom of the response field
            responseField.positionCaret(responseField.getLength());
            responseField.selectPositionCaret(responseField.getLength());
            responseField.deselect();

        } else {
            // If it's not a health-related question, display an error message or handle it accordingly
            responseField.setText("Please enter a question related to health.");
        }
    }


    @FXML
    void readChatAnswer(ActionEvent event) {
        // Toggle the speaking flag
        isSpeaking = !isSpeaking;

        // Get the text from the response field
        String response = responseField.getText();

        // Check if there's text to read aloud
        if (!response.isEmpty()) {
            if (isSpeaking) {
                // If speech is currently active and no thread is running, start reading aloud
                if (textToSpeechThread == null || !textToSpeechThread.isAlive()) {
                    readAloud(response);
                }
            } else {
                // If speech is not active, stop the text-to-speech process
                stopTextToSpeech();
            }
        }
    }



    private void readAloud(String text) {
        // Load the Kevin voice directory
        KevinVoiceDirectory kevinVoiceDirectory = new KevinVoiceDirectory();

        // Get the Kevin voice from the directory
        Voice kevinVoice = kevinVoiceDirectory.getVoices()[0]; // Assuming "kevin" is the first voice

        // Allocate the voice resources
        kevinVoice.allocate();

        // Start the text-to-speech process in a background thread
        textToSpeechThread = new Thread(() -> {
            // Speak the text
            kevinVoice.speak(text);

            // Deallocate the voice resources
            kevinVoice.deallocate();
        });

        // Start the background thread
        textToSpeechThread.start();
    }

    private void stopTextToSpeech() {
        // Check if the background thread is running
        if (textToSpeechThread != null && textToSpeechThread.isAlive()) {
            // Interrupt the background thread to stop the text-to-speech process
            textToSpeechThread.interrupt();
        }
    }
    private boolean isHealthQuestion(String question) {
        // Normalize the question
        String normalizedQuestion = question.toLowerCase();
        // Set a threshold for the maximum allowed distance
        int threshold = 999; // Increase the threshold
        // Initialize Levenshtein distance calculator
        LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

        // Check if any of the keywords appear in the normalized question
        for (String keyword : healthKeywords) {
            // Normalize the keyword
            String normalizedKeyword = keyword.toLowerCase();
            // Calculate the Levenshtein distance between the normalized question and keyword
            int distance = levenshteinDistance.apply(normalizedQuestion, normalizedKeyword);
            // If the distance is within the threshold, consider it a match
            if (distance <= threshold) {
                return true;
            }
            // Print debug information
            System.out.println("Normalized Question: " + normalizedQuestion);
            System.out.println("Keyword: " + normalizedKeyword);
            System.out.println("Distance: " + distance);

        }
        return false;
    }


    private int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    private void appendBubble(String sender, String message) {
        // Append the bubble with appropriate style class
        if (sender.equals("You")) {
            responseField.appendText(sender + ":\n" + message + "\n");
            responseField.getStyleClass().add("text-area-user");
        } else {
            responseField.appendText(sender + ":\n" + message + "\n");
            responseField.getStyleClass().add("text-area-chatbot");
        }

        // Append newline for separation
        responseField.appendText("\n");
    }


    private void animateTyping(String response) {
        Timeline timeline = new Timeline();
        for (int i = 0; i < response.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 50), event -> {
                responseField.appendText(String.valueOf(response.charAt(index)));

                // Check if it's the last character of the response
                if (index == response.length() - 1) {
                    // After the response is fully displayed, adjust the height of the ScrollPane
                    double textHeight = responseField.getHeight();
                    responseScrollPane.setPrefViewportHeight(textHeight + 10); // Add some padding
                }
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
    }









    private String getChatbotResponse(String question) {

        String apiKey = "Your Key here"; // Replace with your OpenAI API key
        String apiUrl = "Your API Endpoint";
        try {
            // Prepare the request body
            String requestBody = "{\"prompt\": \"" + question + "\", \"max_tokens\": 250, \"model\": \"gpt-3.5-turbo-instruct\"}";

            // Create HttpURLConnection
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);

            // Write request body
            conn.getOutputStream().write(requestBody.getBytes(StandardCharsets.UTF_8));

            // Check HTTP response code
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                // Extract response
                return extractResponse(response.toString());
            } else {
                // Read error response
                Scanner errorScanner = new Scanner(conn.getErrorStream());
                StringBuilder errorResponse = new StringBuilder();
                while (errorScanner.hasNextLine()) {
                    errorResponse.append(errorScanner.nextLine());
                }
                errorScanner.close();

                // Print error response
                System.out.println("Error response from server: " + errorResponse.toString());

                return "Error: Failed to get response from the chatbot. Status code: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Failed to communicate with the chatbot API";
        }
    }


    private String extractResponse(String responseBody) {
        try {
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(responseBody);

            // Check if the response contains a 'choices' array
            if (jsonResponse.has("choices")) {
                JSONArray choicesArray = jsonResponse.getJSONArray("choices");

                // Check if the 'choices' array is not empty
                if (choicesArray.length() > 0) {
                    // Get the first choice (assuming it's the response)
                    JSONObject firstChoice = choicesArray.getJSONObject(0);

                    // Extract the text from the choice
                    if (firstChoice.has("text")) {
                        return firstChoice.getString("text");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return an error message if the response format is unexpected
        return "Error: Unable to extract response from API";
    }

    public void setLoggedInPatient(Patient patient) {
        this.loggedInPatient = patient;
        // After setting the patient, initialize the labels

    }
    @FXML
    private void RedirectToProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Afficheruser.fxml"));
            Parent root = loader.load();

            // Pass the modified patient object to the AfficherUser controller
            AfficherUser afficherUserController = loader.getController();
            afficherUserController.setLoggedInPatient(loggedInPatient);

            // Get the current stage
            Stage stage = (Stage) updateButton.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleLogoutButton(ActionEvent event) {
        // Code to handle the logout action goes here
        logout(event);
    }

    private void logout(ActionEvent event) {
        loggedInPatient = null;

        // Redirect the user to the login page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void RedirectToDoctorProfile(ActionEvent event) throws IOException {
        if (loggedInPatient != null) {

            RedirectToTableViewAdminUser(event, loggedInPatient);
        } else {
            showAlert("Error", "Logged in admin is null.");
        }
    }


    private void RedirectToTableViewAdminUser(ActionEvent event, User user) throws IOException {
        if (user instanceof Patient) {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowDoctor.fxml"));
            Parent root = loader.load();

            ShowDoctor adminTableViewController = loader.getController();
            adminTableViewController.setLoggedInPatient((Patient) user);

            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } else {
            showAlert("Error", "Logged in user is not a patient.");
        }
    }


    @FXML
    public void redirecttoadminPage(ActionEvent event) {
        redirectToAddAdminController(event, loggedInPatient);
    }

    private void redirectToAddAdminController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            AfficherUser AddAdminController = loader.getController();
            AddAdminController.setLoggedInPatient((Patient) user);


            // Get the current stage
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
            // Traverse up the scene graph until an AnchorPane is found


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void RedirectToAfficherPost(ActionEvent event) {
        redirectToAffcherPostController(event, loggedInPatient);
    }


    private void redirectToAffcherPostController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherpost.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            afficherpost afficherpostController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller
            afficherpostController.setLoggedInPatient((Patient) user);

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void RedirectToServicePost(ActionEvent event) {
        redirectToAffcherServController(event, loggedInPatient);
    }


    private void redirectToAffcherServController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherService.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            AffichageService afficherpostController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller
            afficherpostController.setLoggedInPatient((Patient) user);

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void RedirectToImcPost(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AddService
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/imc.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre pour afficher la scène
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Gérer les exceptions liées au chargement du fichier FXML
            e.printStackTrace();
        }
    }












    @FXML
    private void redirectToRec(ActionEvent event) {
        redirectToReclaimController(event, loggedInPatient);
    }


    private void redirectToReclaimController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            MainController chatbotController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller
            chatbotController.setLoggedInPatient((Patient) user);

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void redirectToEvent(ActionEvent event) {
        redirectToEventController(event, loggedInPatient);
    }


    private void redirectToEventController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            HomeController chatbotController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller
            chatbotController.setLoggedInPatient((Patient) user);

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}