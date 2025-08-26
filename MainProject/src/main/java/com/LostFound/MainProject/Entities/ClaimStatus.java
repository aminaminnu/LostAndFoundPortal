package com.LostFound.MainProject.Entities;

public enum ClaimStatus {
	PENDING,
	APPROVED,
	REJECTED;

	public static ClaimStatus fromString(String status) {

		for(ClaimStatus claimStatus : ClaimStatus.values()) {
			if (claimStatus.name().equalsIgnoreCase(status)) {
				return claimStatus;

			}
		}
	  throw new IllegalArgumentException("Unkown status: " + status);


	}

}
