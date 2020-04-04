package org.example.meetroomreservation.domain.viewModels;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ReservationView {
    private Integer id;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime datetime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime duration;
    private List<UserView> users;
    private MeetroomView meetroom;


    public ReservationView(){}

    public ReservationView(Integer id, LocalDateTime datetime, LocalTime duration, List<UserView> users, MeetroomView meetroom) {
        this.id = id;
        this.datetime = datetime;
        this.duration = duration;
        this.users = users;
        this.meetroom = meetroom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public List<UserView> getUsers() {
        return users;
    }

    public void setUsers(List<UserView> users) {
       this.users = users;
    }

    public MeetroomView getMeetroom() {
        return meetroom;
    }

    public void setMeetroom(MeetroomView meetroom) {
        this.meetroom = meetroom;
    }
}
