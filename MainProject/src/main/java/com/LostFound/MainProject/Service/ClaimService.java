package com.LostFound.MainProject.Service;

import java.util.List;
import java.util.Map;

import com.LostFound.MainProject.Dto.ClaimDto;
import com.LostFound.MainProject.Entities.ClaimStatus;

public interface ClaimService {

    // Create a new claim
    ClaimDto createClaim(ClaimDto dto);

    // Get all claims (REPORTER / ADMIN)
    List<ClaimDto> findAll();

    // Get claims for a specific user
    List<ClaimDto> findByUserId(Long id);

    // Delete a claim
    void deleteClaim(Long id);

    // Status updates
    ClaimDto approveClaim(Long id);
    ClaimDto rejectClaim(Long id);
    ClaimDto updateClaimStatus(Long id, ClaimStatus newStatus);

    // Notifications
    void notifyOwnerOfClaim(Long id);
    void notifyFinderOfClaim(Long id);

    // Reporter asks question to claimant
    void askQuestionToClaimant(Long id);

    // Reporter verifies answer and asks finder for permission
    ClaimDto verifyAnswerAndRequestFinderPermission(Long id);

    // Finder sets permission to share contact
    ClaimDto setFinderPermission(Long id, boolean allow);

    // Reporter shares finder contact with claimant
    Map<String, String> shareFinderContactWithLoser(Long id);

    // Claimant submits answer
    Map<String, String> submitLoserAnswer(Long claimId, String answer);
}
