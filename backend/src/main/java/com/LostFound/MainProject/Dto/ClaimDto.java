package com.LostFound.MainProject.Dto;

import java.time.LocalDate;

import com.LostFound.MainProject.Entities.ClaimStatus;

public class ClaimDto {
    private Long id;
    private LocalDate claimDate;

    private Long userId;          // claimant (loser)
    private Long lostItemId;
    private Long foundItemId;

    private ClaimStatus status;

    private String reporterQuestion;
    private String loserAnswer;
    private Boolean verifiedByReporter;

    private Boolean ownerGivesContactPermission;
    private Boolean finderGivesContactPermission;   // NEW

    private Long foundItemUserId;                   // NEW (for UI: who is the finder?)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getClaimDate() { return claimDate; }
    public void setClaimDate(LocalDate claimDate) { this.claimDate = claimDate; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getLostItemId() { return lostItemId; }
    public void setLostItemId(Long lostItemId) { this.lostItemId = lostItemId; }

    public Long getFoundItemId() { return foundItemId; }
    public void setFoundItemId(Long foundItemId) { this.foundItemId = foundItemId; }

    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }

    public String getReporterQuestion() { return reporterQuestion; }
    public void setReporterQuestion(String reporterQuestion) { this.reporterQuestion = reporterQuestion; }

    public String getLoserAnswer() { return loserAnswer; }
    public void setLoserAnswer(String loserAnswer) { this.loserAnswer = loserAnswer; }

    public Boolean getVerifiedByReporter() { return verifiedByReporter; }
    public void setVerifiedByReporter(Boolean verifiedByReporter) { this.verifiedByReporter = verifiedByReporter; }

    public Boolean getOwnerGivesContactPermission() { return ownerGivesContactPermission; }
    public void setOwnerGivesContactPermission(Boolean ownerGivesContactPermission) { this.ownerGivesContactPermission = ownerGivesContactPermission; }

    public Boolean getFinderGivesContactPermission() { return finderGivesContactPermission; }
    public void setFinderGivesContactPermission(Boolean finderGivesContactPermission) { this.finderGivesContactPermission = finderGivesContactPermission; }

    public Long getFoundItemUserId() { return foundItemUserId; }
    public void setFoundItemUserId(Long foundItemUserId) { this.foundItemUserId = foundItemUserId; }
}
