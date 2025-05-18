package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.PhotoDTO;
import by.baraznov.recruiting.models.Photo;
import by.baraznov.recruiting.repositories.PhotoRepository;
import by.baraznov.recruiting.services.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    @Override
    @Transactional
    public void save(Photo photo) {
        photoRepository.save(photo);
    }

    @Override
    public Optional<PhotoDTO> getPhotoById(Integer photoId) {
        return photoRepository.findById(photoId)
                .map(photo -> new PhotoDTO(
                        photo.getContentType(),
                        photo.getData()
                ));
    }
}
