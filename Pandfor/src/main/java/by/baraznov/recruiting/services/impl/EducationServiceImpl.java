package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Education;
import by.baraznov.recruiting.repositories.EducationRepository;
import by.baraznov.recruiting.services.EducationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class EducationServiceImpl implements EducationService {
    private final EducationRepository educationRepository;
    @Override
    @Transactional
    public void save(Education education) {
        educationRepository.save(education);
    }
}
