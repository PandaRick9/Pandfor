package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.JobSeekerBasicInfoDto;
import by.baraznov.recruiting.dto.JobSeekerDto;
import by.baraznov.recruiting.dto.JobSeekerProfileDto;
import by.baraznov.recruiting.dto.ResumeAccountPageDTO;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.models.Photo;
import by.baraznov.recruiting.repositories.JobSeekerRepository;
import by.baraznov.recruiting.repositories.PeopleRepository;
import by.baraznov.recruiting.services.JobSeekerService;
import by.baraznov.recruiting.services.PhotoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class JobSeekerServiceImpl implements JobSeekerService {
    private final JobSeekerRepository jobSeekerRepository;
    private final PeopleRepository personRepository;


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
                photoUrl,
                resumes
        );

    }

    @Override
    @Transactional
    public JobSeeker createJobSeeker(Integer userId, JobSeekerDto seekerDto) {
        Person person = personRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setFirstName(seekerDto.firstName());
        jobSeeker.setLastName(seekerDto.lastName());
        jobSeeker.setEmail(seekerDto.email());
        jobSeeker.setPhone(seekerDto.phone());
        jobSeeker.setCity(seekerDto.city());
        jobSeeker.setPerson(person);
        return jobSeeker;
    }
}
