package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.repositories.JobSeekerRepository;
import by.baraznov.recruiting.services.JobSeekerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
