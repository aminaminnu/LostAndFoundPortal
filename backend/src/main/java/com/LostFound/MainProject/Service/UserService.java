package com.LostFound.MainProject.Service;

import java.util.List;
import java.util.Optional;

import com.LostFound.MainProject.Dto.UserDto;
import com.LostFound.MainProject.Entities.User;
import com.LostFound.MainProject.Entities.UserType;

public interface UserService {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();                  // returns full User entities
    User saveUser(User user);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);

    List<User> findUsersWithMostReports();
    
    List<UserDto> findByUserTypeDto(UserType type);

    // 🔽 Add these for admin functionality
    List<UserDto> findAllDto();                      // return only basic info for dashboard
    UserDto adminUpdateUser(Long id, UserDto dto);   // role/userType update
    void adminDeleteUser(Long id);  


}
