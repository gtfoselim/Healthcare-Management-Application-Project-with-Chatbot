package com.visita.models;

public class comment {
    private int id;
    private int id_creatorcom;
    private int id_post_id;
    private String datecreation_comment;
    private String contenu_comment;

    // New field to track report count
    private int reportCount;

    public comment() {
        this.reportCount = 0;  // Initialize report count to 0
    }

    public comment(int id, int id_creatorcom, int id_post_id, String datecreation_comment, String contenu_comment, int reportCount) {
        this.id = id;
        this.id_creatorcom = id_creatorcom;
        this.id_post_id = id_post_id;
        this.datecreation_comment = datecreation_comment;
        this.contenu_comment = contenu_comment;
        this.reportCount = reportCount;  // Initialize report count

    }

    public comment(int id_creatorcom, int id_post_id, String datecreation_comment, String contenu_comment, int reportCount) {
        this.id_creatorcom = id_creatorcom;
        this.id_post_id = id_post_id;
        this.datecreation_comment = datecreation_comment;
        this.contenu_comment = contenu_comment;
        this.reportCount = reportCount;  // Initialize report count
    }

    public comment(String contenu_comment) {
        this.contenu_comment = contenu_comment;
        this.reportCount = 0;  // Initialize report count to 0
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_creatorcom() {
        return id_creatorcom;
    }

    public void setId_creatorcom(int id_creatorcom) {
        this.id_creatorcom = id_creatorcom;
    }

    public int getId_post_id() {
        return id_post_id;
    }

    public void setId_post_id(int id_post_id) {
        this.id_post_id = id_post_id;
    }

    public String getDatecreation_comment() {
        return datecreation_comment;
    }

    public void setDatecreation_comment(String datecreation_comment) {
        this.datecreation_comment = datecreation_comment;
    }

    public String getContenu_comment() {
        return contenu_comment;
    }

    public void setContenu_comment(String contenu_comment) {
        this.contenu_comment = contenu_comment;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    @Override
    public String toString() {
        return "comment{" +
                "id=" + id +
                ", id_creatorcom=" + id_creatorcom +
                ", id_post_id=" + id_post_id +
                ", datecreation_comment='" + datecreation_comment + '\'' +
                ", contenu_comment='" + contenu_comment + '\'' +
                ", reportCount=" + reportCount + // Include reportCount in toString
                '}';
    }
}
