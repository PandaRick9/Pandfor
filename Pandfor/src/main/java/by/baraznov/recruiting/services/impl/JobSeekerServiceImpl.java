package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.JobSeekerBasicInfoDto;
import by.baraznov.recruiting.dto.JobSeekerDto;
import by.baraznov.recruiting.dto.JobSeekerEditDto;
import by.baraznov.recruiting.dto.JobSeekerProfileDto;
import by.baraznov.recruiting.dto.ResumeAccountPageDTO;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.models.Photo;
import by.baraznov.recruiting.repositories.JobSeekerRepository;
import by.baraznov.recruiting.repositories.PeopleRepository;
import by.baraznov.recruiting.repositories.PhotoRepository;
import by.baraznov.recruiting.services.JobSeekerService;
import by.baraznov.recruiting.services.PhotoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class JobSeekerServiceImpl implements JobSeekerService {
    private final JobSeekerRepository jobSeekerRepository;
    private final PeopleRepository personRepository;
    private final PhotoRepository photoRepository;


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

    @Override
    public JobSeekerEditDto getJobSeekerProfileById(Integer id) {
        return jobSeekerRepository.findJobProfileById(id);
    }

    @Override
    @Transactional
    public void updateJobSeeker(Integer id, MultipartFile logo ,JobSeekerEditDto jobSeekerDto) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job seeker not found with id: " + id));

        updateJobSeekerFields(jobSeeker, jobSeekerDto);

        if (logo != null && !logo.isEmpty()) {
            updateJobSeekerPhoto(jobSeeker, logo);
        }

        jobSeekerRepository.save(jobSeeker);
    }

    private void updateJobSeekerFields(JobSeeker jobSeeker, JobSeekerEditDto dto) {
        jobSeeker.setFirstName(dto.firstName());
        jobSeeker.setLastName(dto.lastName());
        jobSeeker.setEmail(dto.email());
        jobSeeker.setPhone(dto.phone());
        jobSeeker.setCity(dto.city());

    }

    private void updateJobSeekerPhoto(JobSeeker jobSeeker, MultipartFile photoFile) {
        try {
            Photo photo;
            if (jobSeeker.getPhoto() != null) {
                photo = jobSeeker.getPhoto();
                photo.setFileName(photoFile.getOriginalFilename());
                photo.setContentType(photoFile.getContentType());
                photo.setData(photoFile.getBytes());
            } else {
                photo = new Photo();
                photo.setFileName(photoFile.getOriginalFilename());
                photo.setContentType(photoFile.getContentType());
                photo.setData(photoFile.getBytes());
                photo = photoRepository.save(photo);
            }
            jobSeeker.setPhoto(photo);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload photo", e);
        }
    }
}
