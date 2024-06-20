module Test{
    requires javafx.controls;
    requires javafx.fxml;
    requires  javafx.graphics;
    requires java.sql;
    requires java.desktop;

    requires org.json;
    requires itextpdf;
    requires twilio;
    requires spring.security.core;
    requires freetts;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.commons.text;
    requires com.google.api.client;
    requires java.mail;
    opens com.visita.controllers;
    opens com.visita.models;
    opens com.visita.services;
    opens com.visita.test;
    requires stripe.java;
}