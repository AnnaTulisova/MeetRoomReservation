package org.example.meetroomreservation.service;

import org.example.meetroomreservation.domain.Meetroom;

import java.util.List;

public interface MeetroomService {
    List<Meetroom> findAll();
    List<Meetroom> findAll(boolean onlyNames);
}
