package org.example.meetroomreservation.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ReservationViewModel {
    private Integer id;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime datetime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime duration;
    private List<User> users;
    private Meetroom meetroom;


    public ReservationViewModel(){}

    public ReservationViewModel(Integer id, LocalDateTime datetime, LocalTime duration, List<User> users, Meetroom meetroom) {
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Meetroom getMeetroom() {
        return meetroom;
    }

    public void setMeetroom(Meetroom meetroom) {
        this.meetroom = meetroom;
    }
}
