package com.LostFound.MainProject.Dto;

public class UserDto {
	private Long id;
	private String name;
	private String email;
	private String role;
	private String userType;

	public UserDto() {

	}

	public UserDto(Long id, String name, String email, String role,String userType) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.userType = userType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	 public String getUserType() {
	        return userType;
	    }

	    public void setUserType(String userType) {
	        this.userType = userType;
	    }


}
