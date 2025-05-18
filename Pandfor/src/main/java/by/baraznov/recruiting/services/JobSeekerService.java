package by.baraznov.recruiting.services;

import by.baraznov.recruiting.dto.JobSeekerProfileDto;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;

import java.util.Optional;

public interface JobSeekerService {
    JobSeeker save(JobSeeker jobSeeker);
    Optional<JobSeeker> findByPerson(Person person);
    JobSeekerProfileDto getProfile(Integer userId);
}
