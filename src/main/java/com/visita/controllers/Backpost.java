package com.visita.controllers;

import com.visita.models.Admin;
import com.visita.models.User;
import com.visita.models.comment;
import com.visita.models.post;
import com.visita.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.visita.services.servicePost;
import com.visita.services.serviceComment;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Backpost {



    @FXML
    private TableView<post> tableView;

    @FXML
    private TableView<comment> tableView2;






    @FXML
    private TableColumn<post, String> titleColumn;

    @FXML
    private TableColumn<post, String> typeColumn;

    @FXML
    private TableColumn<post, String> descriptionColumn;

    @FXML
    private TableColumn<post, String> dateColumn;

    @FXML
    private TableColumn<post, String> countryColumn;
    @FXML
    private TableColumn<post, Void> validateColumn;
    @FXML
    private TableColumn<post, String> imageColumn;

    @FXML
    private TableColumn<post, Void> deleteColumn; // Column for the delete button

    @FXML
    private Button showcommntbttn;

    @FXML
    private TableColumn<comment, String> contenucomment;

    @FXML
    private TableColumn<comment, String> datepostedcomment;

    @FXML
    private TableColumn<comment, Void> deleteColumncomment;
    private ObservableList<post> postList;
    private ObservableList<comment>commetList;

    @FXML
    private Label countryStatisticsLabel;

    @FXML
    private PieChart countryPieChart;

    private Admin loggedInAdmin;

    public void initData(Admin admin, User loggedInUser) {
        // Check if the provided user is an instance of Patient
        if (admin != null && loggedInUser != null && loggedInUser instanceof Admin) {
            // If both the patient and loggedInUser are not null, cast loggedInUser to Patient
            // and initialize labels with patient data
            loggedInAdmin = (Admin) loggedInUser;

        } else {
            // Handle invalid data or show an error message

        }
    }

    public void setLoggedInAdmin(Admin admin) {
        this.loggedInAdmin = admin;
        // After setting the patient, initialize the labels

    }

    servicePost sp = new servicePost();
    serviceComment sc = new serviceComment();


    private void updateCountryPieChart() {
        // Calculate country counts from the post list
        Map<String, Integer> countryCounts = new HashMap<>();

        for (post p : postList) {
            String country = p.getCountry();
            countryCounts.put(country, countryCounts.getOrDefault(country, 0) + 1);
        }

        // Create an observable list of PieChart.Data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : countryCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // Set the data for the pie chart
        countryPieChart.setData(pieChartData);
    }

    public void initialize() {
        // Initialize the lists for posts and comments
        postList = FXCollections.observableArrayList();
        commetList = FXCollections.observableArrayList();

        // Load the data for posts and comments
        loadPosts();
        loadComments();


        // Set the items of the TableView for posts
        tableView.setItems(postList);

        // Map columns to properties of the post class
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title_post"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_post"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("contenu_post"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("makedate_post"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image_post"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        updateCountryPieChart();

        // If the postList is updated, recalculate the country statistics
        postList.addListener((ListChangeListener.Change<? extends post> change) -> updateCountryPieChart());

        // Add delete button to each row in the post table
        deleteColumn.setCellFactory(column -> new TableCell<post, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    post currentPost = getTableView().getItems().get(getIndex());
                    deletePost(currentPost);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
        validateColumn.setCellFactory(column -> new TableCell<post, Void>() {
            private final Button validateButton = new Button("Validate");

            {
                validateButton.setOnAction(event -> {
                    post currentPost = getTableView().getItems().get(getIndex());
                    validatePost(currentPost);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(validateButton);
                }
            }
        });

        // Map columns to properties of the comment class
        contenucomment.setCellValueFactory(new PropertyValueFactory<>("contenu_comment"));
        datepostedcomment.setCellValueFactory(new PropertyValueFactory<>("datecreation_comment"));






        // Add delete button to each row in the comment table
        deleteColumncomment.setCellFactory(column -> new TableCell<comment, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    comment currentComment = getTableView().getItems().get(getIndex());
                    deleteComment(currentComment);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // Set the action for the show comments button
        showcommntbttn.setOnAction(event -> {
            if (tableView.isVisible()) {
                tableView.setVisible(false);
                tableView2.setVisible(true);
                System.out.println("Switching to comments view.");

                tableView2.setItems(commetList);
                tableView2.refresh();

            } else {
                tableView.setVisible(true);
                tableView2.setVisible(false);
                System.out.println("Switching to posts view.");
            }
        });

        // Initially, display the posts TableView and hide the comments TableView
        tableView.setVisible(true);
        tableView2.setVisible(false);
    }





    private UserService userService; // Instance of UserService






    // Load initial posts data (you can replace this with real data fetching logic)
    private void loadPosts() {
        // Create an instance of UserService
        UserService userService = new UserService();

        // Retrieve the list of posts
        List<post> posts = sp.afficher();

        // Clear the existing postList if needed
        postList.clear();

        // Iterate over the list of posts
        for (post p : posts) {
            // Retrieve the creator's ID from the post
            int creatorId = p.getCreator();

            // Use UserService to retrieve the User object by creator ID
            //User creator = userService.getPatientById(creatorId);

            // Check if the User object is not null
            //if (creator != null) {
                // Create a new post object using the constructor that accepts a User object
                post newPost = new post(p.getId(), p.getLikes_post(), creatorId, p.getTitle_post(), p.getContenu_post(), p.getType_post(), p.getImage_post(), p.getMakedate_post(), p.getCountry());

                // Add the new post object to the postList
                postList.add(newPost);
            //} else {
                // Handle the case where the User object is not found (e.g., log an error message)
                //System.out.println("User with ID " + creatorId + " not found.");
            //}
        }
    }

    private void loadComments() {


        List<comment> comments = sc.afficherall();

        System.out.println("\n" + sc.afficherall());

        for (comment c : comments){
            commetList.add(new comment(c.getId(),c.getId_creatorcom(), c.getId_post_id(),c.getDatecreation_comment(),c.getContenu_comment(),c.getReportCount()));

            // Add more posts as needed

        }
    }

    // Handle deletion of a post
    private void deletePost(post postToDelete) {
        sp.Supprimer(postToDelete.getId_post());
        postList.remove(postToDelete);

        //System.out.println(postToDelete.getId() + postToDelete.getType_post());
        // Add code here to handle additional delete logic, such as removing the post from the database
    }

        private void deleteComment(comment commentToDelete) {

            System.out.println("\n"+commentToDelete.getId_post_id());
            sc.Supprimer(commentToDelete.getId());

            commetList.remove(commentToDelete);

            //System.out.println(postToDelete.getId() + postToDelete.getType_post());
            // Add code here to handle additional delete logic, such as removing the post from the database
        }
        private void validatePost(post postToValidate) {


            sp.validatePost(postToValidate.getId_post());



            //System.out.println(postToDelete.getId() + postToDelete.getType_post());
            // Add code here to handle additional delete logic, such as removing the post from the database
        }




    @FXML
    public void redirecttoadminPage(ActionEvent event) {
        redirectToAddAdminController(event, loggedInAdmin);
    }

    private void redirectToAddAdminController(ActionEvent event, User user) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAdmin.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            AfficherAdmin AddAdminController = loader.getController();
            AddAdminController.setLoggedInAdmin((Admin) user);


            // Get the current stage
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
            // Traverse up the scene graph until an AnchorPane is found


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}


