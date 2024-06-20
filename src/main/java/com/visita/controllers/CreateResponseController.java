package com.visita.controllers;

import com.visita.models.Response;
import com.visita.services.ResponseDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class CreateResponseController {

    @FXML
    private TextField reclamationIdField;

    @FXML
    private TextField authorField;

    @FXML
    private TextArea responseContentArea;

    @FXML
    private void createResponse() {
        int reclamationId = Integer.parseInt(reclamationIdField.getText());
        String author = authorField.getText();
        String responseContent = responseContentArea.getText();

        Response response = new Response();
        response.setReclamationId(reclamationId);
        response.setAuthor(author);
        response.setResponseContent(responseContent);

        try {
            ResponseDAO.insertResponse(response);
            clearFields();
            // Optionally, display a success message or perform other actions after successful insertion
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
            // Optionally, display an error message to the user
        }
    }

    private void clearFields() {
        reclamationIdField.clear();
        authorField.clear();
        responseContentArea.clear();
    }
}
