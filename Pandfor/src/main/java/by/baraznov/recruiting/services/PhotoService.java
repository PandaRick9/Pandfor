package by.baraznov.recruiting.services;

import by.baraznov.recruiting.dto.PhotoDTO;
import by.baraznov.recruiting.models.Photo;

import java.util.Optional;

public interface PhotoService {
    void save(Photo photo);
    Optional<PhotoDTO> getPhotoById(Integer photoId);
}
