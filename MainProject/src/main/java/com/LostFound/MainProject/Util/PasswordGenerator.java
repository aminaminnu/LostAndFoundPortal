package com.LostFound.MainProject.Util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
	public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String adminHash = encoder.encode("admin@123");
        String reporterHash = encoder.encode("reporter@123");

        System.out.println("Encoded Admin Password: " + adminHash);
        System.out.println("Encoded Reporter Password: " + reporterHash);
    }

}
