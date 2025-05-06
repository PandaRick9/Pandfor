package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Photo;
import by.baraznov.recruiting.repositories.PhotoRepository;
import by.baraznov.recruiting.services.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
