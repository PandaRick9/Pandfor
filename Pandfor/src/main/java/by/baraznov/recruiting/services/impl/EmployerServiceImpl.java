package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Employer;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.repositories.EmployerRepository;
import by.baraznov.recruiting.services.EmployerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class EmployerServiceImpl implements EmployerService {
    private final EmployerRepository employerRepository;

    @Override
    @Transactional
    public Employer save(Employer emp) {
        return employerRepository.save(emp);
    }

    @Override
    public Optional<Employer> findByPerson(Person person) {
        return employerRepository.findByPerson(person);
    }
}
