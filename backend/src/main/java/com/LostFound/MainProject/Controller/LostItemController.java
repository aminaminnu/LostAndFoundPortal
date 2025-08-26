package com.LostFound.MainProject.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.LostFound.MainProject.Dto.LostItemDto;
import com.LostFound.MainProject.Entities.User;
import com.LostFound.MainProject.Service.LostItemService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/lost-items")
public class LostItemController {


    @Autowired
    private LostItemService lostItemService;


    private String saveImage(MultipartFile imageFile) throws IOException {
        String originalName = imageFile.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String cleanName = originalName.substring(0, originalName.lastIndexOf(".")).replaceAll("\\s+", "_");

        // Removed UPLOAD_TYPE here ✅
        String filename = UUID.randomUUID().toString() + "_" + cleanName + extension;

        Path uploadPath = Paths.get("uploads/lost-images");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("✅ Saved to: " + filePath.toAbsolutePath());

        // This must match your WebConfig mapping
        return "/uploads/lost-images/" + filename;

    }




    // 🔹 Save lost item with image (FormData)
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<LostItemDto> uploadLostItemWithImage(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("lostDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lostDate,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {

        LostItemDto lostItemDto = new LostItemDto();
        lostItemDto.setName(name);
        lostItemDto.setDescription(description);
        lostItemDto.setLocation(location);
        lostItemDto.setLostDate(lostDate);

        if (imageFile != null && !imageFile.isEmpty()) {
            lostItemDto.setImagePath(saveImage(imageFile));
        }

        LostItemDto saved = lostItemService.saveLostItem(lostItemDto, user);
        return ResponseEntity.ok(saved);
    }
    
    
    @GetMapping("/my-items")
    public ResponseEntity<List<LostItemDto>> getItemsForCurrentUser(@AuthenticationPrincipal User user) {
        List<LostItemDto> items = lostItemService.findByUserId(user.getId());
        return ResponseEntity.ok(items);
    }


    // 🔹 Get all lost items
    @GetMapping
    public ResponseEntity<List<LostItemDto>> getAllLostItems() {
        return ResponseEntity.ok(lostItemService.findAll());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('REPORTER')")
    public ResponseEntity<List<LostItemDto>> getAllForReporter() {
        return ResponseEntity.ok(lostItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LostItemDto> getLostItemById(@PathVariable Long id) {
        return lostItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Update lost item without image
    @PutMapping("/{id}")
    public ResponseEntity<LostItemDto> updateLostItem(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @RequestBody LostItemDto lostItemDto
    ) {
        LostItemDto updated = lostItemService.updateLostItem(id, lostItemDto, user);
        return ResponseEntity.ok(updated);
    }

    // 🔹 Update lost item with image (FormData)
    @PutMapping(value = "/upload/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<LostItemDto> updateLostItemWithImage(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("lostDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lostDate,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {

        LostItemDto lostItemDto = new LostItemDto();
        lostItemDto.setName(name);
        lostItemDto.setDescription(description);
        lostItemDto.setLocation(location);
        lostItemDto.setLostDate(lostDate);

        if (imageFile != null && !imageFile.isEmpty()) {
            lostItemDto.setImagePath(saveImage(imageFile));
        }

        LostItemDto updated = lostItemService.updateLostItem(id, lostItemDto, user);
        return ResponseEntity.ok(updated);
    }

    // 🔹 Delete lost item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLostItem(@PathVariable Long id) {
        lostItemService.deleteLostItem(id);
        return ResponseEntity.noContent().build();
    }
}
