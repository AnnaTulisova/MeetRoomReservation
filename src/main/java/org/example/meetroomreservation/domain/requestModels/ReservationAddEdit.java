package org.example.meetroomreservation.domain.requestModels;

public class ReservationAddEdit {
    private String userIds;
    private Integer meetroomId;
    private String datetime;
    private String duration;

    public ReservationAddEdit() { }

    public ReservationAddEdit(String userIds, Integer meetroomId, String datetime, String duration) {
        this.userIds = userIds;
        this.meetroomId = meetroomId;
        this.datetime = datetime;
        this.duration = duration;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public Integer getMeetroomId() {
        return meetroomId;
    }

    public void setMeetroomId(Integer meetroomId) {
        this.meetroomId = meetroomId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
