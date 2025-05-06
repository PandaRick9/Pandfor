package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumesRepository extends JpaRepository<Resume, Integer> {
}
