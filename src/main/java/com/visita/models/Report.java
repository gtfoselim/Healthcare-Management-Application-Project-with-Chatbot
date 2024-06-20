package com.visita.models;

import java.time.LocalDateTime;

public class Report {
    private int id;
    private int entityId;
    private String entityType; // This will be either "post" or "comment"
    private int reporterId;
    private LocalDateTime timestamp;

    public Report() {
        // Default constructor
    }

    public Report(int entityId, String entityType, int reporterId) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.reporterId = reporterId;
        this.timestamp = LocalDateTime.now(); // Set the current timestamp
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getReporterId() {
        return reporterId;
    }

    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", entityId=" + entityId +
                ", entityType='" + entityType + '\'' +
                ", reporterId=" + reporterId +
                ", timestamp=" + timestamp +
                '}';
    }
}
