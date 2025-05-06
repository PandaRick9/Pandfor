package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.JobPreference;
import by.baraznov.recruiting.repositories.JobPreferenceRepository;
import by.baraznov.recruiting.services.JobPreferenceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class JobPreferenceServiceImpl implements JobPreferenceService {
    private final JobPreferenceRepository jobPreferenceRepository;

    @Override
    @Transactional
    public void save(JobPreference jobPreference) {
        jobPreferenceRepository.save(jobPreference);
    }
}
