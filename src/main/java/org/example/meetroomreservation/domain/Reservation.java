package org.example.meetroomreservation.domain;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer meetroom_id;
    private Integer user_id;
    private Timestamp date;
    private Time duration;

    public Reservation(){}

    public Reservation(Integer user_id, Integer meetroom_id, Timestamp date, Time duration) {
        this.user_id = user_id;
        this.meetroom_id = meetroom_id;
        this.date = date;
        this.duration = duration;
    }

    public  Integer getId(){
        return id;
    }

    public Integer getUser_id() {
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
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }
}
