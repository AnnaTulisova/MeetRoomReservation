package org.example.meetroomreservation.service.implementations;

import org.example.meetroomreservation.domain.Meetroom;
import org.example.meetroomreservation.repos.MeetroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.meetroomreservation.service.MeetroomService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetroomServiceImplem implements MeetroomService {
    @Autowired
    private MeetroomRepository meetroomRepository;

    @Override
    public List<Meetroom> findAll(){
        return meetroomRepository.findAllByOrderByIdAsc()
                .stream()
                .map(m-> new Meetroom(m.getId(), m.getName(), m.getLocation()))
                .collect(Collectors.toList());
    }

    public List<Meetroom> findAll(boolean onlyNames){
        return onlyNames
                ? meetroomRepository
                    .findAllByOrderByIdAsc()
                        .stream()
                        .map(m-> new Meetroom(m.getId(), m.getName()))
                        .collect(Collectors.toList())
                : meetroomRepository
                    .findAllByOrderByIdAsc()
                        .stream()
                        .map(m-> new Meetroom(m.getId(), m.getName(), m.getLocation()))
                        .collect(Collectors.toList());
    }

    public Meetroom findById(Integer id){return meetroomRepository.getOne(id);}
}
