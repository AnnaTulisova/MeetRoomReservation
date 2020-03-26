package org.example.meetroomreservation.domain;

import javax.persistence.*;

@Entity
public class Meetroom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String location;

    public Meetroom(){ }

    public Meetroom(Integer id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Meetroom(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public  Integer getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
