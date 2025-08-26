package com.LostFound.MainProject.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LostFound.MainProject.Dao.LostItemDAO;
import com.LostFound.MainProject.Dto.LostItemDto;
import com.LostFound.MainProject.Entities.LostItem;
import com.LostFound.MainProject.Entities.User;

@Service
public class LostItemServiceImpl implements LostItemService {

    private final LostItemDAO lostItemDAO;

    @Autowired
    public LostItemServiceImpl(LostItemDAO lostItemDAO) {
        this.lostItemDAO = lostItemDAO;
    }

    // Convert Entity to DTO
    private LostItemDto toDto(LostItem lostItem) {
        return new LostItemDto(
            lostItem.getId(),
            lostItem.getName(),
            lostItem.getDescription(),
            lostItem.getLocation(),
            lostItem.getLostDate(),
            lostItem.getUser() != null ? lostItem.getUser().getId() : null,
            lostItem.getImagePath() // Add this
        );
    }


    // Convert DTO to Entity
    private LostItem toEntity(LostItemDto dto, User user) {
        LostItem lostItem = new LostItem();
        lostItem.setId(dto.getId());
        lostItem.setName(dto.getName());
        lostItem.setDescription(dto.getDescription());
        lostItem.setLocation(dto.getLocation());
        lostItem.setLostDate(dto.getLostDate());
        lostItem.setUser(user);
        lostItem.setImagePath(dto.getImagePath()); // Add this
        return lostItem;
    }

    private String saveImage(org.springframework.web.multipart.MultipartFile image) throws Exception {
        if (image == null || image.isEmpty()) {
			return null;
		}

        String uploadDir = "uploads/lost-images/";
       // or absolute path like "C:/LostFoundImages/"
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);

        if (!java.nio.file.Files.exists(uploadPath)) {
            java.nio.file.Files.createDirectories(uploadPath);
        }

        java.nio.file.Path filePath = uploadPath.resolve(fileName);
        image.transferTo(filePath.toFile());

        return "/images/" + fileName;

         

    }



    @Override
    public Optional<LostItemDto> findById(Long id) {
        return lostItemDAO.findById(id).map(this::toDto);
    }

    @Override
    public List<LostItemDto> findByUserId(Long userId) {
        return lostItemDAO.findByUserId(userId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<LostItemDto> findAll() {
        return lostItemDAO.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<LostItemDto> findByName(String name) {
        return lostItemDAO.findByName(name).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<LostItemDto> findByLocation(String location) {
        return lostItemDAO.findByLocation(location).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LostItemDto saveLostItem(LostItemDto lostItemDto, User user) {
        Optional<LostItem> existingItem = lostItemDAO.findByNameAndLocationAndLostDateAndUserId(
                lostItemDto.getName(), lostItemDto.getLocation(), lostItemDto.getLostDate(), user.getId()
        );

        if (existingItem.isPresent()) {
            throw new RuntimeException("Lost item already exists.");
        }

        LostItem entity = toEntity(lostItemDto, user);
        LostItem saved = lostItemDAO.save(entity);
        return toDto(saved);
    }
    @Override
    @Transactional
    public LostItemDto saveLostItemWithImage(LostItemDto dto, org.springframework.web.multipart.MultipartFile image, User user) {
        try {
            String imagePath = saveImage(image);
            dto.setImagePath(imagePath);
            return saveLostItem(dto, user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }


    @Override
    @Transactional
    public LostItemDto updateLostItem(Long id, LostItemDto updatedLostItemDto, User user) {
        LostItem existing = lostItemDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Lost item not found."));

        existing.setName(updatedLostItemDto.getName());
        existing.setDescription(updatedLostItemDto.getDescription());
        existing.setLocation(updatedLostItemDto.getLocation());
        existing.setLostDate(updatedLostItemDto.getLostDate());
        existing.setUser(user);
        existing.setImagePath(updatedLostItemDto.getImagePath()); // ✅ Add this line

        LostItem updated = lostItemDAO.save(existing);
        return toDto(updated);
    }
    @Override
    @Transactional
    public LostItemDto updateLostItemWithImage(Long id, LostItemDto dto, org.springframework.web.multipart.MultipartFile image, User user) {
        try {
            LostItem existing = lostItemDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Lost item not found"));

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setLocation(dto.getLocation());
            existing.setLostDate(dto.getLostDate());
            existing.setUser(user);

            if (image != null && !image.isEmpty()) {
                String imagePath = saveImage(image);
                existing.setImagePath(imagePath);
            }

            LostItem updated = lostItemDAO.save(existing);
            return toDto(updated);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update image", e);
        }
    }


    @Override
    @Transactional
    public void deleteLostItem(Long id) {
        lostItemDAO.deleteById(id);
    }
}
