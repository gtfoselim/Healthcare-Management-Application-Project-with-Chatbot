package com.visita.models;

public class react {
    private int userId;
    private int eventId;
    private String reactContent;

    public react() {
    }

    public react(int userId, int eventId, String reactContent) {
        this.userId = userId;
        this.eventId = eventId;
        this.reactContent = reactContent;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getReactContent() {
        return reactContent;
    }

    public void setReactContent(String reactContent) {
        this.reactContent = reactContent;
    }

    @Override
    public String toString() {
        return "React{" +
                "userId=" + userId +
                ", eventId=" + eventId +
                ", reactContent='" + reactContent + '\'' +
                '}';
    }
}
