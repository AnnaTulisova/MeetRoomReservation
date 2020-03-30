package org.example.meetroomreservation.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
   @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime datetime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime duration;

    @ManyToOne
    private User user;

    @ManyToOne
    private Meetroom meetroom;

    public Reservation(){}

    public Reservation( LocalDateTime datetime, LocalTime duration) {
        this.datetime = datetime;
        this.duration = duration;
    }

    public Reservation(LocalDateTime datetime, LocalTime duration, User user, Meetroom meetroom) {
        this.id = null;
        this.datetime = datetime;
        this.duration = duration;
        this.user = user;
        this.meetroom = meetroom;
    }

    public  Integer getId(){
        return id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Meetroom getMeetroom() {
        return meetroom;
    }

    public void setMeetroom(Meetroom meetroom) {
        this.meetroom = meetroom;
    }
}
