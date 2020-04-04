package org.example.meetroomreservation.domain.requestModels;

public class ReservationEdit {
    private String userIds;
    private Integer meetroomId;
    private String datetime;
    private String duration;
    private Integer oldMeetroomId;
    private String oldDatetime;

    public ReservationEdit(String userIds, Integer meetroomId, String datetime, String duration, Integer oldMeetroomId, String oldDatetime) {
        this.userIds = userIds;
        this.meetroomId = meetroomId;
        this.datetime = datetime;
        this.duration = duration;
        this.oldMeetroomId = oldMeetroomId;
        this.oldDatetime = oldDatetime;
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

    public Integer getOldMeetroomId() {
        return oldMeetroomId;
    }

    public void setOldMeetroomId(Integer oldMeetroomId) {
        this.oldMeetroomId = oldMeetroomId;
    }

    public String getOldDatetime() {
        return oldDatetime;
    }

    public void setOldDatetime(String oldDatetime) {
        this.oldDatetime = oldDatetime;
    }
}
