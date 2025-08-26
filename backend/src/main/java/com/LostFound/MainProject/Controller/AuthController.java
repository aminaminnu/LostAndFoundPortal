package com.LostFound.MainProject.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LostFound.MainProject.Entities.Roles;
import com.LostFound.MainProject.Entities.User;
import com.LostFound.MainProject.Security.JwtUtil;
import com.LostFound.MainProject.repository.UserRepository;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {
	 @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private JwtUtil jwtUtil;

	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    @PostMapping("/register")
	    public ResponseEntity<?> registerUser(@RequestBody User user) {
	        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
	            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
	        }

	        // Disallow registration as REPORTER role
	        if (user.getRole() == Roles.ROLE_REPORTER) {
	            return ResponseEntity.badRequest().body(Map.of("error", "You cannot register as a REPORTER."));
	        }

	        // Set default role if null or invalid (optional)
	        if (user.getRole() == null || !(user.getRole() == Roles.ROLE_USER || user.getRole() == Roles.ROLE_ADMIN)) {
	            user.setRole(Roles.ROLE_USER);
	        }

	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        userRepository.save(user);
	        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
	    }




	    @PostMapping("/login")
	    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
	        try {
	            String email = loginData.get("email");
	            String password = loginData.get("password");

	            Authentication auth = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(email, password)
	            );

	            User user = (User) auth.getPrincipal();
	            System.out.println("Login User: email=" + user.getEmail() + ", role=" + user.getRole() + ", userType=" + user.getUserType());

	            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
	            String userTypeStr = user.getUserType() != null ? user.getUserType().name() : "UNKNOWN";

	            return ResponseEntity.ok(Map.of(
	                "token", token,
	                "email", user.getEmail(),
	                "role", user.getRole().name(),
	                "userType", userTypeStr
	            ));
	        } catch (Exception e) {
	            return ResponseEntity.status(401).body("Invalid email or password");
	        }
	    }


	    }


