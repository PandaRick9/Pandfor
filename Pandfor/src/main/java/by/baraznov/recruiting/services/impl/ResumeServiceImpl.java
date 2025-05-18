package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.resumePage.EducationDto;
import by.baraznov.recruiting.dto.resumePage.JobPreferenceDto;
import by.baraznov.recruiting.dto.resumePage.PersonalInfoDto;
import by.baraznov.recruiting.dto.resumePage.PhotoDto;
import by.baraznov.recruiting.dto.resumePage.ResumeDto;
import by.baraznov.recruiting.dto.resumePage.ResumeSkillDto;
import by.baraznov.recruiting.models.JobPreference;
import by.baraznov.recruiting.models.PersonalInfo;
import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.repositories.ResumesRepository;
import by.baraznov.recruiting.services.ResumeService;
import jakarta.persistence.EntityNotFoundException;
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
    public Resume findOne(Integer id) {
        return resumesRepository.findById(id).orElse(null);
    }

    @Override
    public List<Resume> findAll() {
        return resumesRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        resumesRepository.deleteById(id);
    }


}
