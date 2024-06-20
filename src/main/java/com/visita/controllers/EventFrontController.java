package com.visita.controllers;

import com.visita.models.Evenement;
import com.visita.models.Participation;
import com.visita.models.react;
import com.visita.services.ParticipationServiceImpl;
import com.visita.services.ReactServiceImpl;
import com.visita.utils.DataSource;
import com.visita.services.EmailSender;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventFrontController {
    @FXML
    private Label nbp;
    @FXML
    private ImageView img;

    @FXML
    private Label title;

    @FXML
    private Label type;

    @FXML
    private Label lieu;

    @FXML
    private Label dtd;

    @FXML
    private Label dtf;

    @FXML
    private HBox react_container;
    @FXML
    private Label nb_going;

    @FXML
    private Label nb_inter;

    @FXML
    private Label nb_not;

    @FXML
    private Button participationBtn;

    private Evenement evenement;
    ParticipationServiceImpl participationService = new ParticipationServiceImpl(DataSource.getInstance().getConnection());
    private ReactServiceImpl reactService = new ReactServiceImpl(DataSource.getInstance().getConnection());


    public void initialize(Connection connection) {
        this.participationService = new ParticipationServiceImpl(connection);
    }
    public void setData(Evenement evenement) {
        this.evenement = evenement;

        String photoUrl = evenement.getImage_evenement();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            String imagePath = "C:\\Users\\lenovo\\CompleteProject\\public\\uploads\\" + photoUrl;
            Image image = new Image("file:" + imagePath);
            img.setImage(image);
        } else {
            img.setImage(null);
        }


        title.setText("Title: " + evenement.getNom_evenement());
        type.setText("Type: " + evenement.getType_evenement());
        lieu.setText("Lieu: " + evenement.getLieu_evenement());
        nbp.setText("NBR Participant: " + Integer.toString(evenement.getNb_participants()));

        dtd.setText("Date début: " + evenement.getDate_debut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dtf.setText("Date fin: " + evenement.getDate_fin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        // Update the counts of reactions
        try {
            int eventId = evenement.getId();
            int goingCount = reactService.countGoingReactions(eventId);
            int interestedCount = reactService.countInterestedReactions(eventId);
            int notInterestedCount = reactService.countNotInterestedReactions(eventId);

            nb_going.setText("Going: " + goingCount);
            nb_inter.setText("Interested: " + interestedCount);
            nb_not.setText("Not Interested: " + notInterestedCount);
        } catch (SQLException e) {
            displayErrorMessage("Error fetching reaction counts: " + e.getMessage());
        }
    }

    @FXML
    void react() {
        react_container.setVisible(!react_container.isVisible());
    }

    @FXML
    void participate() {
        try {
            if (participationService == null) {
                throw new IllegalStateException("Participation service is not set.");
            }

            int medecinId = 1; // Assuming a default medecin_id for now
            int evenementId = evenement.getId();

            // Check if both medecin_id and evenement_id exist
            if (!participationService.checkIdsExist(evenementId, medecinId)) {
                // Add participation
                Participation participation = createParticipation(evenement);
                participationService.ajouter(participation);
                participationService.updateNbrParticipants(evenementId, 1);
                EmailSender.sendEmail("emna.chelly2019@gmail.com");
            } else {
                // Decrement the number of participants
                System.out.println("test failed");

            }
        } catch (SQLException e) {
            displayErrorMessage("Error adding participation: " + e.getMessage());
        }
    }

    private Participation createParticipation(Evenement evenement) {
        Participation participation = new Participation();
        participation.setDate_participation(LocalDateTime.now());
        participation.setEvenement_id(evenement.getId());
        participation.setDescription("Participation description");
        participation.setMedecin_id(1);
        return participation;
    }
    @FXML
    void handleGoingClick() {
        updateReactContent("going");
    }

    @FXML
    void handleInterestedClick() {
        updateReactContent("interested");
    }

    @FXML
    void handleNotInterestedClick() {
        updateReactContent("not interested");
    }
    private void updateReactContent(String newReactContent) {
        // Check if the react service is initialized
        if (reactService == null) {
            displayErrorMessage("React service is not initialized.");
            return;
        }

        try {
            // Check if the react record exists for the current user and event
            int userId = 1; // Assuming a default user ID for now
            int eventId = evenement.getId();

            if (reactService.checkIdsExist(userId, eventId)) {
                // Update the react content
                reactService.updateReactContent(userId, eventId, newReactContent);
            } else {
                // Add a new react
                reactService.addReact(new react(userId, eventId, newReactContent));
            }
        } catch (SQLException e) {
            displayErrorMessage("Error updating react content: " + e.getMessage());
        }
    }



    private void displayErrorMessage(String message) {
        System.err.println(message);
    }
}
