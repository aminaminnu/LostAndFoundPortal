package com.LostFound.MainProject.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.LostFound.MainProject.Config.NotificationPublisher;
import com.LostFound.MainProject.Dao.ClaimDAO;
import com.LostFound.MainProject.Dto.ClaimDto;
import com.LostFound.MainProject.Entities.Claim;
import com.LostFound.MainProject.Entities.ClaimStatus;
import com.LostFound.MainProject.Entities.FoundItem;
import com.LostFound.MainProject.Entities.LostItem;
import com.LostFound.MainProject.Entities.User;

@Service
@Transactional
public class ClaimServiceImpl implements ClaimService {

    private final ClaimDAO claimDAO;
    private final EmailService emailService;
    private final NotificationPublisher notifier;

    @Autowired
    public ClaimServiceImpl(ClaimDAO claimDAO, EmailService emailService, NotificationPublisher notifier) {
        this.claimDAO = claimDAO;
        this.emailService = emailService;
        this.notifier = notifier;
    }

    private static Long idOrNull(Object o) {
        if (o == null) return null;
        if (o instanceof User u) return u.getId();
        if (o instanceof LostItem l) return l.getId();
        if (o instanceof FoundItem f) return f.getId();
        return null;
    }

    private ClaimDto toDto(Claim c) {
        if (c == null) return null;
        ClaimDto d = new ClaimDto();
        d.setId(c.getId());
        d.setClaimDate(c.getClaimDate());
        d.setUserId(idOrNull(c.getUser()));
        d.setLostItemId(idOrNull(c.getLostItem()));
        d.setFoundItemId(idOrNull(c.getFoundItem()));
        d.setStatus(c.getStatus());
        d.setReporterQuestion(c.getReporterQuestion());
        d.setLoserAnswer(c.getLoserAnswer());
        d.setVerifiedByReporter(c.isVerifiedByReporter());
        d.setOwnerGivesContactPermission(false);
        d.setFinderGivesContactPermission(c.isFinderGivesContactPermission());
        d.setFoundItemUserId(
            c.getFoundItem() != null && c.getFoundItem().getUser() != null
                ? c.getFoundItem().getUser().getId()
                : null
        );
        return d;
    }

    private Claim toEntity(ClaimDto d) {
        if (d == null) return null;
        Claim c = new Claim();
        if (d.getId() != null && d.getId() > 0) c.setId(d.getId());
        else c.setId(null);
        c.setClaimDate(d.getClaimDate());
        if (d.getUserId() != null) c.setUser(new User(d.getUserId()));
        if (d.getLostItemId() != null) c.setLostItem(new LostItem(d.getLostItemId()));
        if (d.getFoundItemId() != null) c.setFoundItem(new FoundItem(d.getFoundItemId()));
        c.setStatus(d.getStatus());
        c.setReporterQuestion(d.getReporterQuestion());
        c.setLoserAnswer(d.getLoserAnswer());
        c.setVerifiedByReporter(Boolean.TRUE.equals(d.getVerifiedByReporter()));
        c.setOwnerGivesContactPermission(false);
        c.setFinderGivesContactPermission(Boolean.TRUE.equals(d.getFinderGivesContactPermission()));
        return c;
    }

    @Override
    public ClaimDto createClaim(ClaimDto dto) {
        Claim c = toEntity(dto);
        c.setStatus(ClaimStatus.PENDING);
        return toDto(claimDAO.save(c));
    }

    @Override
    public List<ClaimDto> findAll() {
        return claimDAO.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ClaimDto> findByUserId(Long id) {
        return claimDAO.findByUserId(id).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteClaim(Long id) {
        claimDAO.deleteById(id);
    }

    @Override
    public ClaimDto approveClaim(Long id) {
        return updateClaimStatus(id, ClaimStatus.APPROVED);
    }

    @Override
    public ClaimDto rejectClaim(Long id) {
        return updateClaimStatus(id, ClaimStatus.REJECTED);
    }

    @Override
    public ClaimDto updateClaimStatus(Long id, ClaimStatus newStatus) {
        Claim c = claimDAO.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (c.getStatus() == newStatus)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already in same status");

        c.setStatus(newStatus);
        claimDAO.save(c);
        sendStatusMail(c);
        return toDto(c);
    }

    private void sendStatusMail(Claim c) {
        if (c.getUser() == null || c.getUser().getEmail() == null) return;
        String to = c.getUser().getEmail();
        String item = c.getLostItem() != null ? c.getLostItem().getName() : "(item)";

        String body = switch (c.getStatus()) {
            case APPROVED -> "Hi %s,<br>Your claim for <b>%s</b> was approved!".formatted(c.getUser().getName(), item);
            case REJECTED -> "Hi %s,<br>Sorry, your claim for <b>%s</b> was rejected.".formatted(c.getUser().getName(), item);
            default -> null;
        };

        if (body != null) {
            emailService.sendEmail(to, "Claim " + c.getStatus(), body);
            notifier.pushToUser(c.getUser().getId(), "Claim " + c.getStatus(),
                    "Your claim for '" + item + "' is " + c.getStatus().name().toLowerCase() + ".");
        }
    }

    @Override
    public void notifyOwnerOfClaim(Long id) {
        Claim c = claimDAO.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User owner = c.getLostItem().getUser();
        User claimant = c.getUser();

        emailService.sendEmail(owner.getEmail(), "Item claimed",
                "<p>Hi " + owner.getName() + ",</p>" +
                "<p><b>" + claimant.getName() + "</b> has claimed your item: <b>" + c.getLostItem().getName() + "</b>.</p>");
        notifier.pushToUser(owner.getId(), "New Claim", "Someone claimed “" + c.getLostItem().getName() + "”.");
    }

    @Override
    public void notifyFinderOfClaim(Long id) {
        Claim c = claimDAO.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User finder = c.getFoundItem().getUser();
        User claimant = c.getUser();

        emailService.sendEmail(finder.getEmail(), "Item claimed",
                "<p>Hi " + finder.getName() + ",</p>" +
                "<p><b>" + claimant.getName() + "</b> claimed the item you found: <b>" + c.getFoundItem().getName() + "</b>.</p>");
        notifier.pushToUser(finder.getId(), "Item Claimed", "Claim submitted on item you found.");
    }

    @Override
    public void askQuestionToClaimant(Long id) {
        Claim c = claimDAO.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User u = c.getUser();
        emailService.sendEmail(u.getEmail(), "Question about your claim",
                "<p>Hi " + u.getName() + ",</p>" +
                "<p>To verify your claim, please describe your lost item in detail.</p>");
        notifier.pushToUser(u.getId(), "Verification Required", "Describe your item to proceed with the claim.");
    }

    @Override
    public ClaimDto verifyAnswerAndRequestFinderPermission(Long id) {
        Claim c = claimDAO.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        c.setVerifiedByReporter(true);
        claimDAO.save(c);

        User finder = c.getFoundItem().getUser();
        emailService.sendEmail(finder.getEmail(), "Share Contact?",
                "<p>Hi " + finder.getName() + ",</p>" +
                "<p>A verified claimant wants to contact you for item: <b>" + c.getFoundItem().getName() + "</b>.</p>");
        notifier.pushToUser(finder.getId(), "Contact Permission", "Do you approve sharing your contact?");
        return toDto(c);
    }

    @Override
    public ClaimDto setFinderPermission(Long id, boolean allow) {
        Claim c = claimDAO.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        c.setFinderGivesContactPermission(allow);
        claimDAO.save(c);
        notifier.pushToUser(c.getUser().getId(), "Finder's decision",
                "Finder " + (allow ? "allowed" : "denied") + " contact sharing.");
        return toDto(c);
    }

   
    @Override
    public Map<String, String> shareFinderContactWithLoser(Long claimId) {
        Claim claim = claimDAO.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        User finder = claim.getFoundItem().getUser();
        User loser = claim.getUser();

        if (finder == null || loser == null) {
            throw new RuntimeException("Finder or loser not found");
        }

        String finderEmail = finder.getEmail();
        String loserEmail = loser.getEmail();

        String subject = "Finder Contact Information";
        String body = "Hello " + loser.getName() + ",\n\n" +
                "The finder has approved sharing their contact information.\n" +
                "Finder Email: " + finderEmail + "\n\n" +
                "Please reach out to arrange the return of your lost item.\n\n" +
                "Regards,\nLost & Found Team";

        emailService.sendEmail(loserEmail, subject, body);

        return Map.of("message", "Finder contact emailed to loser successfully.");
    }

    



    @Override
    public Map<String, String> submitLoserAnswer(Long claimId, String answer) {
        Claim c = claimDAO.findById(claimId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        c.setLoserAnswer(answer);
        claimDAO.save(c);
        return Map.of("message", "Answer submitted.");
    }
}
