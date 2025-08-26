package com.LostFound.MainProject.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.LostFound.MainProject.Dto.LostItemDto;
import com.LostFound.MainProject.Entities.User;

public interface LostItemService {
    LostItemDto saveLostItem(LostItemDto lostItemDto, User user);
    LostItemDto updateLostItem(Long id, LostItemDto updatedLostItemDto, User user);
    Optional<LostItemDto> findById(Long id);
    List<LostItemDto> findByUserId(Long userId);
    List<LostItemDto> findAll();
    List<LostItemDto> findByName(String name);
    List<LostItemDto> findByLocation(String location);
    void deleteLostItem(Long id);
	LostItemDto saveLostItemWithImage(LostItemDto dto, MultipartFile image, User user);
	LostItemDto updateLostItemWithImage(Long id, LostItemDto dto, MultipartFile image, User user);
}
