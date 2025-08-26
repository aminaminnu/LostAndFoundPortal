package com.LostFound.MainProject.Dao;

import java.util.List;
import java.util.Optional;

import com.LostFound.MainProject.Entities.User;

public interface UserDao {

	    Optional<User> findById(Long id);

	    Optional<User> findByEmail(String email);

	    List<User> findAll();

	    User save(User user);

	    void deleteById(Long id);

	    List<User> findUsersWithMostReports(); // Should be well defined in implementation (reports = found + lost?)



}
