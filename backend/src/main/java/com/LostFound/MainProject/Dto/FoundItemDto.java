package com.LostFound.MainProject.Dto;

import java.time.LocalDate;

public class FoundItemDto {
	private Long id;
	private String name;
	private String description;
	private String location;
	private LocalDate foundDate;
	private Long userId;
	private String imagePath;  // ✅ Add this line

	// Getters and Setters
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

	public LocalDate getFoundDate() {
		return foundDate;
	}
	public void setFoundDate(LocalDate foundDate) {
		this.foundDate = foundDate;
	}

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	// Constructors
	public FoundItemDto(Long id, String name, String description, String location,
	                    LocalDate foundDate, Long userId, String imagePath) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.location = location;
		this.foundDate = foundDate;
		this.userId = userId;
		this.imagePath = imagePath;
	}

	public FoundItemDto() {
		super();
	}
}
