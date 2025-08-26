package com.LostFound.MainProject.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.LostFound.MainProject.Dto.FoundItemDto;
import com.LostFound.MainProject.Entities.User;

public interface FoundItemService {
	Optional<FoundItemDto> findById(Long id);

    List<FoundItemDto> findByUserId(Long userId);

    List<FoundItemDto> findAll();

    List<FoundItemDto> findByName(String name);

    List<FoundItemDto> findByLocation(String location);
    
    List<FoundItemDto> findSuggestedItemsForUser(Long userId);


    FoundItemDto saveFoundItem(FoundItemDto foundItemDto, User user);

    FoundItemDto updateFoundItem(Long id, FoundItemDto updatedFoundItemDto, User user);

    FoundItemDto saveFoundItemWithImage(FoundItemDto dto, MultipartFile image, User user);

    FoundItemDto updateFoundItemWithImage(Long id, FoundItemDto dto, MultipartFile image, User user);



    void deleteFoundItem(Long id);





}
