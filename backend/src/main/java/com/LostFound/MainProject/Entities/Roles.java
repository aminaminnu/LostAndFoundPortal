package com.LostFound.MainProject.Entities;

public enum Roles {

	ROLE_USER,
	ROLE_REPORTER,
	ROLE_ADMIN;

	// Implement equalsIgnoreCase properly (optional, usually not needed for enums)
    public boolean equalsIgnoreCase(String other) {
        if (other == null) {
			return false;
		}
        return this.name().equalsIgnoreCase(other);
    }

    // Parse a String to a Roles enum, case-insensitive, add ROLE_ prefix if missing
    public static Roles fromString(String role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        // Add ROLE_ prefix if not present
        if (!role.toUpperCase().startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        for (Roles r : Roles.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }

        throw new IllegalArgumentException("No enum constant for role: " + role);
    }

}
