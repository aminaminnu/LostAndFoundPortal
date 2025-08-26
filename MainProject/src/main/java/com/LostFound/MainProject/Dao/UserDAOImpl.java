package com.LostFound.MainProject.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.LostFound.MainProject.Entities.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class UserDAOImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> users = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public List<User> findUsersWithMostReports() {
        // Returns top 5 users ordered by total reports (lost + found items)
        return entityManager.createQuery(
                        "SELECT u FROM User u ORDER BY " +
                        "(SELECT COUNT(l) FROM LostItem l WHERE l.user = u) + " +
                        "(SELECT COUNT(f) FROM FoundItem f WHERE f.user = u) DESC", User.class)
                .setMaxResults(5)
                .getResultList();
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User save(User user) {
        // Encode password if it's new or not already encoded
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getId() == null) {
            entityManager.persist(user);  // New user
            return user;
        } else {
            return entityManager.merge(user);  // Existing user update
        }
    }

    @Override
    public void deleteById(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
