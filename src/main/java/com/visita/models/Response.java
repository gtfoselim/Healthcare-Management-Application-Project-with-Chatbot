package com.visita.models;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Response {
    private final IntegerProperty id;
    private final IntegerProperty reclamationId;
    private final StringProperty author;
    private final StringProperty responseContent;
    private final ObjectProperty<LocalDateTime> responseDate;

    public Response() {
        this.id = new SimpleIntegerProperty();
        this.reclamationId = new SimpleIntegerProperty();
        this.author = new SimpleStringProperty();
        this.responseContent = new SimpleStringProperty();
        this.responseDate = new SimpleObjectProperty<>();
    }

    // Getter and setter methods for id
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Getter and setter methods for reclamationId
    public int getReclamationId() {
        return reclamationId.get();
    }

    public void setReclamationId(int reclamationId) {
        this.reclamationId.set(reclamationId);
    }

    public IntegerProperty reclamationIdProperty() {
        return reclamationId;
    }

    // Getter and setter methods for author
    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public StringProperty authorProperty() {
        return author;
    }

    // Getter and setter methods for responseContent
    public String getResponseContent() {
        return responseContent.get();
    }

    public void setResponseContent(String responseContent) {
        this.responseContent.set(responseContent);
    }

    public StringProperty responseContentProperty() {
        return responseContent;
    }

    // Getter and setter methods for responseDate
    public LocalDateTime getResponseDate() {
        return responseDate.get();
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate.set(responseDate);
    }

    public ObjectProperty<LocalDateTime> responseDateProperty() {
        return responseDate;
    }
}