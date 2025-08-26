package com.LostFound.MainProject.Dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LostItemDto {
	private Long id;
	private String name;
	private String description;
	private String location;
	private LocalDate lostDate;
	private Long userId;
	private String imagePath; // ✅ Add this line

	public LostItemDto() {
		super();
	}

	public LostItemDto(Long id, String name, String description, String location, LocalDate lostDate, Long userId, String imagePath) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.location = location;
		this.lostDate = lostDate;
		this.userId = userId;
		this.imagePath = imagePath;
	}

	// Getters and setters
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDate getLostDate() {
		return lostDate;
	}

	public void setLostDate(LocalDate lostDate) {
		this.lostDate = lostDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@JsonProperty("imagePath")
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
