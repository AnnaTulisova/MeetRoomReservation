package org.example.meetroomreservation.service.implementations;

import org.example.meetroomreservation.domain.ReservationViewModel;
import org.example.meetroomreservation.domain.User;
import org.example.meetroomreservation.repos.UserRepository;
import org.example.meetroomreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImplem implements UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findById(Integer id) { return userRepository.getOne(id);}

    public void save(User user){
        userRepository.save(user);
    }

    public String getUserIdsFromReservation(ReservationViewModel reservationViewModel){
        String  user_ids = "";
        for(User u : reservationViewModel.getUsers()){
            user_ids +=u.getId().toString() + ";";
        }
        return  user_ids;
    }

    public List<Integer> findDifferentUsers(int[] old_ids, int[] new_ids){
         HashSet<Integer> s = new HashSet<>();
        for (int new_id : new_ids) s.add(new_id);
        List<Integer> lost_ids = new ArrayList<>();
        for (Integer old_id : old_ids)
            if (!s.contains(old_id))
                lost_ids.add(old_id);
        return  lost_ids;
    }

    public List<User> getUsersByIds(List<Integer> ids){
        return userRepository.findAllById(ids);
    }

    public int[] updateStringIdsToIntArray(String ids){
        return Arrays.stream(ids.split(";")).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public List<User> findAll(){
        return userRepository
                .findAll()
                .stream()
                .map(user -> new User(user.getId(), user.getEmail(), user.getLogin(), user.getPassword(), user.getRoles()))
                .collect(Collectors.toList());

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }
}
