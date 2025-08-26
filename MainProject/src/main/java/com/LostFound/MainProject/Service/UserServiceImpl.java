package com.LostFound.MainProject.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LostFound.MainProject.Dto.UserDto;
import com.LostFound.MainProject.Entities.Roles;
import com.LostFound.MainProject.Entities.User;
import com.LostFound.MainProject.Entities.UserType;
import com.LostFound.MainProject.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findUsersWithMostReports() {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        if (user.getRole() == Roles.ROLE_REPORTER) {
            throw new RuntimeException("You are not allowed to register as a REPORTER.");
        }

        if (user.getRole() == null ||
            !(user.getRole() == Roles.ROLE_USER || user.getRole() == Roles.ROLE_ADMIN)) {
            user.setRole(Roles.ROLE_USER);
        }

        return userRepository.save(user);
    }

    public User createReporterByAdmin(User user) {
        if (user.getRole() != Roles.ROLE_REPORTER) {
            throw new RuntimeException("Invalid role. This endpoint is only for creating reporters.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(updatedUser.getName());
                    existingUser.setEmail(updatedUser.getEmail());
                    if (updatedUser.getRole() != null) {
                        existingUser.setRole(updatedUser.getRole());
                    }
                    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
                        existingUser.setPassword(updatedUser.getPassword());
                    }
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public List<UserDto> findByUserTypeDto(UserType type) {
        return userRepository.findByUserType(type)
                             .stream()
                             .map(u -> new UserDto(
                                     u.getId(), u.getName(),
                                     u.getEmail(), u.getRole().name(),
                                     type != null ? type.name() : null))
                             .toList();
    }


 // --- ADMIN METHODS -------------------------------------------------

    @Override
    public List<UserDto> findAllDto() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserDto(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole().name(),          // enum ➜ String
                        u.getUserType() != null ? u.getUserType().name() : null))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto adminUpdateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        // String ➜ enum conversions (with null checks)
        if (dto.getRole() != null)
            user.setRole(Roles.valueOf(dto.getRole()));

        if (dto.getUserType() != null)
            user.setUserType(UserType.valueOf(dto.getUserType()));

        userRepository.save(user);

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),                            // enum ➜ String
                user.getUserType() != null ? user.getUserType().name() : null);
    }

    @Override
    public void adminDeleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
