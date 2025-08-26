package com.LostFound.MainProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LostFound.MainProject.Entities.User;
import com.LostFound.MainProject.Service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {
	 @Autowired
	    private UserService userService;

	    // Get current logged-in user details
	    @GetMapping("/me")
	    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
	        return ResponseEntity.ok(user);
	    }

	    // Update user profile
	    @PutMapping("/me")
	    public ResponseEntity<User> updateUser(@AuthenticationPrincipal User currentUser, @RequestBody User updatedUser) {
	        User user = userService.updateUser(currentUser.getId(), updatedUser);
	        return ResponseEntity.ok(user);
	    }
	    
	    @PostMapping("/log-exit")
	    public ResponseEntity<String> logUserExit(@AuthenticationPrincipal User user) {
	        System.out.println("User exited: " + user.getEmail() + " at " + java.time.LocalDateTime.now());
	        return ResponseEntity.ok("User exit logged successfully.");
	    }



}
