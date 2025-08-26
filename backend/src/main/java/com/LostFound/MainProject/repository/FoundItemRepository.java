package com.LostFound.MainProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LostFound.MainProject.Entities.FoundItem;
import com.LostFound.MainProject.Entities.User;

public interface FoundItemRepository  extends JpaRepository<FoundItem, Long> {
	 List<FoundItem> findByUser(User user);

}
