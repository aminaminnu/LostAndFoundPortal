package com.LostFound.MainProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LostFound.MainProject.Entities.User;
import com.LostFound.MainProject.Entities.UserType;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByUserType(UserType userType);
}
