package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.repositories.ResumesRepository;
import by.baraznov.recruiting.services.ResumeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumesRepository resumesRepository;
    @Override
    @Transactional
    public void save(Resume resume) {
        resume.setDateCreated(LocalDateTime.now());
        resume.setIsActive(true);
        resumesRepository.save(resume);
    }

    @Override
    public List<Resume> findAll() {
        return resumesRepository.findAll();
    }
}
