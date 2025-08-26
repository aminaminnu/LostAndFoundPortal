package com.LostFound.MainProject.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.LostFound.MainProject.Entities.Roles;
import com.LostFound.MainProject.Entities.User;
import com.LostFound.MainProject.repository.UserRepository;

@Component // Required to make Spring run this
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdminUserInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Admin user
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin@123")); // 🔐 Hashed
            admin.setRole(Roles.ROLE_ADMIN);
            userRepository.save(admin);
            System.out.println("✅ Admin user created!");
        }

        // Reporter user
        if (userRepository.findByEmail("reporter@gmail.com").isEmpty()) {
            User reporter = new User();
            reporter.setName("Reporter");
            reporter.setEmail("reporter@gmail.com");
            reporter.setPassword(passwordEncoder.encode("reporter@123")); // 🔐 Hashed
            reporter.setRole(Roles.ROLE_REPORTER); // Make sure ROLE_REPORTER is defined in your enum
            userRepository.save(reporter);
            System.out.println("✅ Reporter user created!");
        }
    }
}
