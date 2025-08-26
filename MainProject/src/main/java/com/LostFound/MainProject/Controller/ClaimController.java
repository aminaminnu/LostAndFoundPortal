package com.LostFound.MainProject.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.LostFound.MainProject.Dto.ClaimDto;
import com.LostFound.MainProject.Entities.ClaimStatus;
import com.LostFound.MainProject.Entities.User;
import com.LostFound.MainProject.Service.ClaimService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    // USER: Create a claim
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ClaimDto> createClaim(@AuthenticationPrincipal User user,
                                                @RequestBody ClaimDto claimDto) {
        if (claimDto.getId() != null && claimDto.getId() <= 0) claimDto.setId(null);
        claimDto.setUserId(user.getId());
        return ResponseEntity.ok(claimService.createClaim(claimDto));
    }

    // USER or REPORTER: View own claims
    @PreAuthorize("hasAnyRole('USER','REPORTER')")
    @GetMapping("/my-claims")
    public ResponseEntity<List<ClaimDto>> getMyClaims(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(claimService.findByUserId(user.getId()));
    }

    // REPORTER or ADMIN: View all claims
    @PreAuthorize("hasAnyRole('REPORTER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClaimDto>> getAllClaims() {
        return ResponseEntity.ok(claimService.findAll());
    }

    // REPORTER or ADMIN: Mark pending
    @PreAuthorize("hasAnyRole('REPORTER','ADMIN')")
    @PutMapping("/{id}/mark-pending")
    public ResponseEntity<ClaimDto> markPending(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.updateClaimStatus(id, ClaimStatus.PENDING));
    }

    // REPORTER or ADMIN: Approve claim
    @PreAuthorize("hasAnyRole('REPORTER','ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<ClaimDto> approveClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.approveClaim(id));
    }

    // REPORTER or ADMIN: Reject claim
    @PreAuthorize("hasAnyRole('REPORTER','ADMIN')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<ClaimDto> rejectClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.rejectClaim(id));
    }

    // USER: Delete own claim
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }

    // USER: Answer reporter's question
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/answer")
    public ResponseEntity<Map<String, String>> submitAnswer(@PathVariable Long id,
                                                            @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(claimService.submitLoserAnswer(id, request.get("answer")));
    }

    // REPORTER: Verify answer & ask finder permission in one step
    @PreAuthorize("hasRole('REPORTER')")
    @PostMapping("/{id}/verify-and-ask-finder")
    public ResponseEntity<ClaimDto> verifyAndAskFinder(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.verifyAnswerAndRequestFinderPermission(id));
    }

    // USER (Finder): Give or deny contact sharing permission
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/finder-permission")
    public ResponseEntity<ClaimDto> setFinderPermission(@PathVariable Long id,
                                                        @RequestBody Map<String, Boolean> body) {
        boolean allow = Boolean.TRUE.equals(body.get("allow"));
        return ResponseEntity.ok(claimService.setFinderPermission(id, allow));
    }

    @PostMapping("/{id}/share-finder-contact")
    public ResponseEntity<Map<String, String>> shareFinderContact(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.shareFinderContactWithLoser(id));
    }

    // REPORTER: Notify item owner
    @PreAuthorize("hasRole('REPORTER')")
    @PostMapping("/{id}/notify-owner")
    public ResponseEntity<Map<String, String>> notifyOwner(@PathVariable Long id) {
        claimService.notifyOwnerOfClaim(id);
        return ResponseEntity.ok(Map.of("message", "Owner notified."));
    }

    // REPORTER or USER: Notify finder
    @PreAuthorize("hasAnyRole('REPORTER','USER')")
    @PostMapping("/{id}/notify-finder")
    public ResponseEntity<Map<String, String>> notifyFinder(@PathVariable Long id) {
        claimService.notifyFinderOfClaim(id);
        return ResponseEntity.ok(Map.of("message", "Finder notified."));
    }

    // REPORTER: Ask question to claimant
    @PreAuthorize("hasRole('REPORTER')")
    @PostMapping("/{id}/ask-question")
    public ResponseEntity<Map<String, String>> askQuestion(@PathVariable Long id) {
        claimService.askQuestionToClaimant(id);
        return ResponseEntity.ok(Map.of("message", "Verification question sent to claimant."));
    }
}
