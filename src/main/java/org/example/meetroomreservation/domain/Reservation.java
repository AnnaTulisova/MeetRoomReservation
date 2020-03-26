package org.example.meetroomreservation.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
   /* private Integer meetroom_id;
    private Integer user_id;*/
   @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime datetime;
    @DateTimeFormat(pattern = "HH:mm")
    private Time duration;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Meetroom meetroom;

    public Reservation(){}

    public Reservation( LocalDateTime datetime, Time duration) {
        this.datetime = datetime;
        this.duration = duration;
    }

    public  Integer getId(){
        return id;
    }

 /*   public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public Integer getMeetroom_id() {
        return meetroom_id;
    }

    public void setMeetroom_id(Integer meetroom_id) {
        this.meetroom_id = meetroom_id;
    }*/

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
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
