package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.JobSeekerBasicInfoDto;
import by.baraznov.recruiting.dto.JobSeekerProfileDto;
import by.baraznov.recruiting.dto.ResumeAccountPageDTO;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.repositories.JobSeekerRepository;
import by.baraznov.recruiting.services.JobSeekerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class JobSeekerServiceImpl implements JobSeekerService {
    private final JobSeekerRepository jobSeekerRepository;


    @Override
    @Transactional
    public JobSeeker save(JobSeeker jobSeeker) {
        return jobSeekerRepository.save(jobSeeker);
    }

    @Override
    public Optional<JobSeeker> findByPerson(Person person) {
        return jobSeekerRepository.findByPerson(person);
    }

    public JobSeekerProfileDto getProfile(Integer userId) {
        JobSeekerBasicInfoDto basicInfo = jobSeekerRepository.findBasicInfoByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        List<ResumeAccountPageDTO> resumes = jobSeekerRepository.findResumesByUserId(userId);

        String photoUrl = basicInfo.photoId() != null
                ? "/photos/" + basicInfo.photoId()
                : null;

        return new JobSeekerProfileDto(
                basicInfo.firstName(),
                basicInfo.lastName(),
                basicInfo.email(),
                basicInfo.phone(),
                basicInfo.city(),
                basicInfo.workExperienceSummary(),
                photoUrl,
                resumes
        );

    }
}
