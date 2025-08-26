package com.LostFound.MainProject.Entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class LostItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private String location;
	private LocalDate lostDate = LocalDate.now();

	private String imagePath; // New field to store image file path or URL

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "lostItem", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Claim> claims;

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
	public LocalDate getLostDate() {
		return lostDate;
	}
	public void setLostDate(LocalDate lostDate) {
		this.lostDate = lostDate;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Claim> getClaims() {
		return claims;
	}
	public void setClaims(List<Claim> claims) {
		this.claims = claims;
	}

	// Constructors
	public LostItem(Long id, String name, String description, String location, LocalDate lostDate, String imagePath,
			User user, List<Claim> claims) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.location = location;
		this.lostDate = lostDate;
		this.imagePath = imagePath;
		this.user = user;
		this.claims = claims;
	}
	public LostItem() {
		super();
	}

	public LostItem(Long id) {        // set only the PK
	    this.id = id;
	}
	@Override
	public String toString() {
		return "LostItem [id=" + id + ", name=" + name + ", description=" + description + ", location=" + location
				+ ", lostDate=" + lostDate + ", imagePath=" + imagePath + ", user=" + user + ", claims=" + claims + "]";
	}
	
}
