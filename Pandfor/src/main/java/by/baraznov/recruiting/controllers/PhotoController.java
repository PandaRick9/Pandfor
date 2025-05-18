package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.repositories.PhotoRepository;
import by.baraznov.recruiting.services.PhotoService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/photos")
@AllArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("/{photoId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Integer photoId) {
        return photoService.getPhotoById(photoId)
                .map(photo -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(photo.contentType()))
                        .body(photo.data()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}