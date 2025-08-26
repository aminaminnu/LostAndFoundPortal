package com.LostFound.MainProject.Entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate claimDate = LocalDate.now();

    @ManyToOne
    private User user;           // claimant (loser)

    @ManyToOne
    private LostItem lostItem;

    @ManyToOne
    private FoundItem foundItem;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    // Smart verification
    private String reporterQuestion;
    private String loserAnswer;
    private boolean verifiedByReporter = false;

    // Owner (loser) permission to share owner's contact
    private boolean ownerGivesContactPermission = false;

    // NEW: Finder permission to share finder’s contact
    private boolean finderGivesContactPermission = false;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getClaimDate() { return claimDate; }
    public void setClaimDate(LocalDate claimDate) { this.claimDate = claimDate; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LostItem getLostItem() { return lostItem; }
    public void setLostItem(LostItem lostItem) { this.lostItem = lostItem; }

    public FoundItem getFoundItem() { return foundItem; }
    public void setFoundItem(FoundItem foundItem) { this.foundItem = foundItem; }

    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }

    public String getReporterQuestion() { return reporterQuestion; }
    public void setReporterQuestion(String reporterQuestion) { this.reporterQuestion = reporterQuestion; }

    public String getLoserAnswer() { return loserAnswer; }
    public void setLoserAnswer(String loserAnswer) { this.loserAnswer = loserAnswer; }

    public boolean isVerifiedByReporter() { return verifiedByReporter; }
    public void setVerifiedByReporter(boolean verifiedByReporter) { this.verifiedByReporter = verifiedByReporter; }

    public boolean isOwnerGivesContactPermission() { return ownerGivesContactPermission; }
    public void setOwnerGivesContactPermission(boolean ownerGivesContactPermission) { this.ownerGivesContactPermission = ownerGivesContactPermission; }

    public boolean isFinderGivesContactPermission() { return finderGivesContactPermission; }
    public void setFinderGivesContactPermission(boolean finderGivesContactPermission) { this.finderGivesContactPermission = finderGivesContactPermission; }

    // Constructors
    public Claim() {}

    public Claim(Long id, LocalDate claimDate, User user, LostItem lostItem, FoundItem foundItem,
                 ClaimStatus status, String reporterQuestion, String loserAnswer,
                 boolean verifiedByReporter, boolean ownerGivesContactPermission,
                 boolean finderGivesContactPermission) {
        this.id = id;
        this.claimDate = claimDate;
        this.user = user;
        this.lostItem = lostItem;
        this.foundItem = foundItem;
        this.status = status;
        this.reporterQuestion = reporterQuestion;
        this.loserAnswer = loserAnswer;
        this.verifiedByReporter = verifiedByReporter;
        this.ownerGivesContactPermission = ownerGivesContactPermission;
        this.finderGivesContactPermission = finderGivesContactPermission;
    }

    @Override
    public String toString() {
        return "Claim [id=" + id
            + ", claimDate=" + claimDate
            + ", user=" + user
            + ", lostItem=" + lostItem
            + ", foundItem=" + foundItem
            + ", status=" + status
            + ", reporterQuestion=" + reporterQuestion
            + ", loserAnswer=" + loserAnswer
            + ", verifiedByReporter=" + verifiedByReporter
            + ", ownerGivesContactPermission=" + ownerGivesContactPermission
            + ", finderGivesContactPermission=" + finderGivesContactPermission
            + "]";
    }
}
