package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeeker, Integer> {
    Optional<JobSeeker> findByPerson(Person person);
}
