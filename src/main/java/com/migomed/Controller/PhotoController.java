package com.migomed.Controller;

import com.migomed.Entity.Photo;
import com.migomed.Service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    private final PhotoService photoService;
    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Photo> createPhoto(@RequestBody Photo photo) {
        Photo savedPhoto = photoService.savePhoto(photo);
        return ResponseEntity.ok(savedPhoto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Photo> updatePhoto(@PathVariable Long id, @RequestBody Photo photo) {
        Photo updatedPhoto = photoService.updatePhoto(id, photo);
        return ResponseEntity.ok(updatedPhoto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<Photo>> getAllPhotos() {
        List<Photo> photos = photoService.getAllPhotos();
        return ResponseEntity.ok(photos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Photo> getPhotoById(@PathVariable Long id) {
        Photo photo = photoService.getPhotoById(id);
        return ResponseEntity.ok(photo);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Photo>> searchPhotosBySection(@RequestParam String section) {
        List<Photo> photos = photoService.searchPhotosBySection(section);
        return ResponseEntity.ok(photos);
    }
}
