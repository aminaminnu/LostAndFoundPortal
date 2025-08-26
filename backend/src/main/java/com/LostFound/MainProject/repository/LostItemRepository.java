package com.LostFound.MainProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LostFound.MainProject.Entities.LostItem;
import com.LostFound.MainProject.Entities.User;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {
	 List<LostItem> findByUser(User user);

}
