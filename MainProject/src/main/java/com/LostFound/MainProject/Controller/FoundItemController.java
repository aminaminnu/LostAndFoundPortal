package com.LostFound.MainProject.Controller;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.LostFound.MainProject.Dto.FoundItemDto;
import com.LostFound.MainProject.Entities.User;
import com.LostFound.MainProject.Service.FoundItemService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/found-items")
public class FoundItemController {

    @Autowired
    private FoundItemService foundItemService;

    private String saveImage(MultipartFile imageFile) throws IOException {
        String originalName = imageFile.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String cleanName = originalName.substring(0, originalName.lastIndexOf(".")).replaceAll("\\s+", "_");

        String filename = UUID.randomUUID().toString() + "_" + cleanName + extension;
        Path uploadPath = Paths.get("uploads/found-images");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("✅ Saved to: " + filePath.toAbsolutePath());

        return "/uploads/found-images/" + filename;
    }

    // 🔹 Save found item with image
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<FoundItemDto> uploadFoundItemWithImage(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("foundDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate foundDate,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {

        FoundItemDto foundItemDto = new FoundItemDto();
        foundItemDto.setName(name);
        foundItemDto.setDescription(description);
        foundItemDto.setLocation(location);
        foundItemDto.setFoundDate(foundDate);

        if (imageFile != null && !imageFile.isEmpty()) {
            foundItemDto.setImagePath(saveImage(imageFile));
        }

        FoundItemDto saved = foundItemService.saveFoundItem(foundItemDto, user);
        return ResponseEntity.ok(saved);
    }

    // 🔹 Update found item with image
    @PutMapping(value = "/upload/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<FoundItemDto> updateFoundItemWithImage(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("foundDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate foundDate,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {

        FoundItemDto foundItemDto = new FoundItemDto();
        foundItemDto.setName(name);
        foundItemDto.setDescription(description);
        foundItemDto.setLocation(location);
        foundItemDto.setFoundDate(foundDate);

        if (imageFile != null && !imageFile.isEmpty()) {
            foundItemDto.setImagePath(saveImage(imageFile));
        }

        FoundItemDto updated = foundItemService.updateFoundItem(id, foundItemDto, user);
        return ResponseEntity.ok(updated);
    }

    // 🔹 Add using JSON body
    
    
    @GetMapping("/my-items")
    public ResponseEntity<List<FoundItemDto>> getMyFoundItems(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(foundItemService.findByUserId(user.getId()));
    }

    
    @PostMapping
    public ResponseEntity<FoundItemDto> addFoundItem(
            @AuthenticationPrincipal User user,
            @RequestBody FoundItemDto foundItemDto
    ) {
        FoundItemDto saved = foundItemService.saveFoundItem(foundItemDto, user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<FoundItemDto>> getAllFoundItems() {
        return ResponseEntity.ok(foundItemService.findAll());
    }
    
    @GetMapping("/suggested")
    public ResponseEntity<List<FoundItemDto>> getSuggestedItems(@AuthenticationPrincipal User user) {
        List<FoundItemDto> suggestedItems = foundItemService.findSuggestedItemsForUser(user.getId());
        return ResponseEntity.ok(suggestedItems);
    }


 // 🔹 Get all found items for reporter
    @GetMapping("/all")
    public ResponseEntity<List<FoundItemDto>> getAllForReporter() {
        return ResponseEntity.ok(foundItemService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<FoundItemDto> getFoundItemById(@PathVariable Long id) {
        return foundItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FoundItemDto>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(foundItemService.findByUserId(userId));
    }

    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<FoundItemDto>> searchByName(@PathVariable String name) {
        return ResponseEntity.ok(foundItemService.findByName(name));
    }

    @GetMapping("/search/location/{location}")
    public ResponseEntity<List<FoundItemDto>> searchByLocation(@PathVariable String location) {
        return ResponseEntity.ok(foundItemService.findByLocation(location));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoundItemDto> updateFoundItem(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @RequestBody FoundItemDto itemDto
    ) {
        FoundItemDto updated = foundItemService.updateFoundItem(id, itemDto, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoundItem(@PathVariable Long id) {
        foundItemService.deleteFoundItem(id);
        return ResponseEntity.noContent().build();
    }
}
