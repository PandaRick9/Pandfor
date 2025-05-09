package by.baraznov.recruiting.services;

import by.baraznov.recruiting.models.Employer;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;

import java.util.Optional;

public interface EmployerService {
    Employer save(Employer emp);
    Optional<Employer> findByPerson(Person person);
}
