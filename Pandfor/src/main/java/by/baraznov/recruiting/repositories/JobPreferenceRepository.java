package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.JobPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPreferenceRepository extends JpaRepository<JobPreference, Integer> {
}
