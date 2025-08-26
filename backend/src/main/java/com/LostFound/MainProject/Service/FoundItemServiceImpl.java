package com.LostFound.MainProject.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import com.LostFound.MainProject.Dao.FoundItemDAO;
import com.LostFound.MainProject.Dto.FoundItemDto;
import com.LostFound.MainProject.Entities.FoundItem;
import com.LostFound.MainProject.Entities.LostItem;
import com.LostFound.MainProject.Entities.User;
import com.LostFound.MainProject.repository.LostItemRepository;
import com.LostFound.MainProject.repository.UserRepository;

@Service
public class FoundItemServiceImpl implements FoundItemService {

    private final FoundItemDAO foundItemDAO;
    
  

    @Autowired
    public FoundItemServiceImpl(FoundItemDAO foundItemDAO) {
        this.foundItemDAO = foundItemDAO;
    }
    
    @Autowired
    private LostItemRepository lostItemRepository; 
    
    @Autowired
    private UserRepository userRepository;


    // Convert Entity to DTO
    private FoundItemDto toDto(FoundItem foundItem) {
        return new FoundItemDto(
            foundItem.getId(),
            foundItem.getName(),
            foundItem.getDescription(),
            foundItem.getLocation(),
            foundItem.getFoundDate(),
            foundItem.getUser() != null ? foundItem.getUser().getId() : null,
            foundItem.getImagePath() // ✅ include imagePath
        );
    }


    // Convert DTO to Entity
    private FoundItem toEntity(FoundItemDto dto, User user) {
        FoundItem foundItem = new FoundItem();
        foundItem.setId(dto.getId());
        foundItem.setName(dto.getName());
        foundItem.setDescription(dto.getDescription());
        foundItem.setLocation(dto.getLocation());
        foundItem.setFoundDate(dto.getFoundDate());
        foundItem.setUser(user);
        foundItem.setImagePath(dto.getImagePath()); // ✅ set image path from DTO
        return foundItem;
    }
    private String saveImage(MultipartFile image) throws Exception {
        if (image == null || image.isEmpty()) {
			return null;
		}

        // Use user home or any stable directory
        String uploadDir = System.getProperty("user.home") + "/LostFoundImages/";
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);

        if (!java.nio.file.Files.exists(uploadPath)) {
            java.nio.file.Files.createDirectories(uploadPath);
        }

        java.nio.file.Path filePath = uploadPath.resolve(fileName);
        image.transferTo(filePath.toFile());

        // Return relative or full path depending on your use case
        return uploadDir + fileName;

    }

   


    @Override
    public Optional<FoundItemDto> findById(Long id) {
        return foundItemDAO.findById(id).map(this::toDto);
    }

    @Override
    public List<FoundItemDto> findByUserId(Long userId) {
        return foundItemDAO.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FoundItemDto> findSuggestedItemsForUser(Long userId) {
        // 🔍 Step 1: Get the User object
        User user = userRepository.findById(userId)
                      .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // 📦 Step 2: Get the user's lost items
        List<LostItem> lostItems = lostItemRepository.findByUser(user);

        // 📦 Step 3: Get all found items
        List<FoundItem> foundItems = foundItemDAO.findAll();

        // 🔍 Step 4: Match logic
        List<FoundItem> matched = foundItems.stream()
            .filter(found -> lostItems.stream().anyMatch(lost ->
                found.getName().equalsIgnoreCase(lost.getName()) &&
                found.getLocation().equalsIgnoreCase(lost.getLocation())
            ))
            .collect(Collectors.toList());

        // 📤 Step 5: Convert to DTOs
        return matched.stream().map(this::toDto).collect(Collectors.toList());
    }

   


    @Override
    public List<FoundItemDto> findAll() {
        return foundItemDAO.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FoundItemDto> findByName(String name) {
        return foundItemDAO.findByName(name).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FoundItemDto> findByLocation(String location) {
        return foundItemDAO.findByLocation(location).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FoundItemDto saveFoundItem(FoundItemDto foundItemDto, User user) {
        Optional<FoundItem> existingItem = foundItemDAO.findByNameAndLocationAndFoundDateAndUserId(
                foundItemDto.getName(),
                foundItemDto.getLocation(),
                foundItemDto.getFoundDate(),
                user.getId()
        );

        if (existingItem.isPresent()) {
            throw new RuntimeException("Found item already exists.");
        }

        FoundItem entity = toEntity(foundItemDto, user);
        FoundItem saved = foundItemDAO.save(entity);
        return toDto(saved);
    }


    @Override
    @Transactional
    public FoundItemDto updateFoundItem(Long id, FoundItemDto updatedFoundItemDto, User user) {
        FoundItem existing = foundItemDAO.findById(id)
            .orElseThrow(() -> new RuntimeException("Found item not found."));

        existing.setName(updatedFoundItemDto.getName());
        existing.setDescription(updatedFoundItemDto.getDescription());
        existing.setLocation(updatedFoundItemDto.getLocation());
        existing.setFoundDate(updatedFoundItemDto.getFoundDate());
        existing.setUser(user);
        existing.setImagePath(updatedFoundItemDto.getImagePath()); // ✅ Add this line

        FoundItem updated = foundItemDAO.save(existing);
        return toDto(updated);
    }

    @Override
    @Transactional
    public void deleteFoundItem(Long id) {
        foundItemDAO.deleteById(id);
    }
    @Override
    @Transactional
    public FoundItemDto saveFoundItemWithImage(FoundItemDto dto, MultipartFile image, User user) {
        try {
            String imagePath = saveImage(image);
            dto.setImagePath(imagePath);

            return saveFoundItem(dto, user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    @Override
    @Transactional
    public FoundItemDto updateFoundItemWithImage(Long id, FoundItemDto dto, MultipartFile image, User user) {
        try {
            FoundItem existing = foundItemDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setLocation(dto.getLocation());
            existing.setFoundDate(dto.getFoundDate());
            existing.setUser(user);

            if (image != null && !image.isEmpty()) {
                String imagePath = saveImage(image);
                existing.setImagePath(imagePath);
            }

            FoundItem updated = foundItemDAO.save(existing);
            return toDto(updated);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update image", e);
        }
    }





}
