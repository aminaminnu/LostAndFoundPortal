package com.LostFound.MainProject.Dao;

import java.util.List;
import java.util.Optional;

import com.LostFound.MainProject.Entities.Claim;
import com.LostFound.MainProject.Entities.ClaimStatus;

public interface ClaimDAO {

    Optional<Claim> findById(Long id);

    List<Claim> findByUserId(Long userId);

    List<Claim> findByFoundItemId(Long foundItemId);

    List<Claim> findByLostItemId(Long lostItemId);

    List<Claim> findByStatus(ClaimStatus status);

    List<Claim> findByUserIdAndStatus(Long userId, ClaimStatus status);

    List<Claim> findMatchingClaimsByNameAndLocation();

    List<Claim> findAll();

    Claim save(Claim claim);

    void deleteById(Long id);

    boolean existsById(Long id);
}
