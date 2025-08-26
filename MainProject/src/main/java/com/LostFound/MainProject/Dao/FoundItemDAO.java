package com.LostFound.MainProject.Dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.LostFound.MainProject.Entities.FoundItem;

public interface FoundItemDAO {

	    Optional<FoundItem> findById(Long id);

	    List<FoundItem> findByUserId(Long userId);

	    List<FoundItem> findAll();

	    List<FoundItem> findByName(String name);

	    List<FoundItem> findByLocation(String location);

	    // ⚠️ Better to move to service layer
	    // FoundItem updateFoundItem(Long id, FoundItem foundItem);

	    FoundItem save(FoundItem foundItem);
	    FoundItem updateFoundItem(Long id, FoundItem updatedFoundItem);

	    void deleteById(Long id);

	    Optional<FoundItem> findByNameAndLocationAndFoundDateAndUserId(String name, String location, LocalDate foundDate, Long userId);



}
