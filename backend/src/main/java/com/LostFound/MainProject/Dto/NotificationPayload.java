package com.LostFound.MainProject.Dto;

public record NotificationPayload(Long receiverId,
	    String title,
	    String message,
	    String createdAt) {

}
