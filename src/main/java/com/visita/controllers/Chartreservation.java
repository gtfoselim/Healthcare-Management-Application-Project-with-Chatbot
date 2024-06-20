package com.visita.controllers;

import com.visita.models.ReservationService;
import com.visita.services.ReservationSrvService;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.application.Platform;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chartreservation {
    @FXML
    private PieChart pieChartReservationService;

    private final ReservationSrvService ps = new ReservationSrvService();

    @FXML
    private void initialize() {
        pieChartReservationService.setTitle("Statistiques des réservations par service");
    }

    public void showReservationStatistics() {
        if (pieChartReservationService == null) {
            System.out.println("Erreur : pieChartReservationService est null");
            return;
        }

        Platform.runLater(() -> {
            try {
                List<ReservationService> reservationServices = ps.afficher();
                if (reservationServices == null || reservationServices.isEmpty()) {
                    System.out.println("Aucune donnée de réservation disponible.");
                    return;
                }

                Map<String, Integer> serviceCounts = new HashMap<>();
                for (ReservationService reservationService : reservationServices) {
                    String serviceName = reservationService.getService_nom();
                    serviceCounts.put(serviceName, serviceCounts.getOrDefault(serviceName, 0) + 1);
                }

                pieChartReservationService.getData().clear();
                for (Map.Entry<String, Integer> entry : serviceCounts.entrySet()) {
                    String serviceName = entry.getKey();
                    Integer count = entry.getValue();
                    PieChart.Data data = new PieChart.Data(serviceName, count);
                    pieChartReservationService.getData().add(data);
                }

                for (PieChart.Data data : pieChartReservationService.getData()) {
                    StringProperty nameProperty = new SimpleStringProperty(data.getName() + " : " + (int) data.getPieValue() + " réservations");
                    data.nameProperty().bind(nameProperty);
                    Tooltip tooltip = new Tooltip(String.format("%s : %d réservations", data.getName(), (int) data.getPieValue()));
                    Tooltip.install(data.getNode(), tooltip);
                }
            } catch (Exception e) {
                System.out.println("Une erreur s'est produite lors de l'affichage des statistiques de réservation : " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
