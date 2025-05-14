package com.migomed.Service;

import com.migomed.Entity.Photo;
import com.migomed.Repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photo savePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public Photo updatePhoto(Long id, Photo updatedPhoto) {
        Photo existing = photoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Photo not found with id " + id));
        existing.setPhotoPath(updatedPhoto.getPhotoPath());
        existing.setSection(updatedPhoto.getSection());
        return photoRepository.save(existing);
    }

    public void deletePhoto(Long id) {
        Photo existing = photoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Photo not found with id " + id));
        photoRepository.delete(existing);
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Photo not found with id " + id));
    }

    public List<Photo> searchPhotosBySection(String section) {
        return photoRepository.findBySectionContainingIgnoreCase(section);
    }
}
