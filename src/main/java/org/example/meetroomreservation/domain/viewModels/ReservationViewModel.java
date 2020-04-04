package org.example.meetroomreservation.domain.viewModels;

import org.example.meetroomreservation.domain.Meetroom;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ReservationViewModel {
    private Integer id;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime datetime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime duration;
    private List<UserViewModel> users;
    private MeetroomViewModel meetroom;


    public ReservationViewModel(){}

    public ReservationViewModel(Integer id, LocalDateTime datetime, LocalTime duration, List<UserViewModel> users, MeetroomViewModel meetroom) {
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

    public List<UserViewModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserViewModel> users) {
       this.users = users;
    }

    public MeetroomViewModel getMeetroom() {
        return meetroom;
    }

    public void setMeetroom(MeetroomViewModel meetroom) {
        this.meetroom = meetroom;
    }
}
