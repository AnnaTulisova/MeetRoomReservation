package org.example.meetroomreservation.service;

import org.example.meetroomreservation.domain.viewModels.ReservationViewModel;
import org.example.meetroomreservation.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> findAll();
    User findByEmail(String email);
    User findById(Integer id);
    String getUserIdsFromReservation(ReservationViewModel reservationViewModel);
    List<Integer> findDifferentUsers(int[] old_ids, int[] new_ids);
    List<User> getUsersByIds(List<Integer> ids);
    int[] updateStringIdsToIntArray(String ids);
    void save(User user);

}
