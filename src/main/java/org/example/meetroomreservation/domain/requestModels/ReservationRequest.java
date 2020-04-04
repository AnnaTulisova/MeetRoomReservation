package org.example.meetroomreservation.domain.requestModels;

public class ReservationRequest {
    private String datetime;
    private Integer meetroomId;

    public ReservationRequest() { }

    public ReservationRequest(String dateTime, Integer meetroomId) {
        this.datetime = dateTime;
        this.meetroomId = meetroomId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Integer getMeetroomId() {
        return meetroomId;
    }

    public void setMeetroomId(Integer meetroomId) {
        this.meetroomId = meetroomId;
    }
}
