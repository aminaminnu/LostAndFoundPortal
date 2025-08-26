package com.LostFound.MainProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LostFound.MainProject.Entities.Claim;
import com.LostFound.MainProject.Entities.ClaimStatus;
import com.LostFound.MainProject.Entities.User;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByUser(User user);

    List<Claim> findByStatus(ClaimStatus status);

    List<Claim> findByUserIdAndStatus(Long userId, ClaimStatus status);

    List<Claim> findByLostItemId(Long lostItemId);

    List<Claim> findByFoundItemId(Long foundItemId);

    // ✅ Custom method for finding all claims by user ID
    List<Claim> findByUserId(Long userId);
}
