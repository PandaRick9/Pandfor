package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumesRepository extends JpaRepository<Resume, Integer> {
    @Query("""
        SELECT r FROM Resume r
        LEFT JOIN FETCH r.personalInfo pi
        LEFT JOIN FETCH pi.photo
        LEFT JOIN FETCH r.jobPreference
        LEFT JOIN FETCH r.personalInfo.education
        LEFT JOIN FETCH r.resumeSkills rs
        LEFT JOIN FETCH rs.skill
        WHERE r.resumeId = :id
        """)
    Optional<Object> findByIdWithDetails(Integer id);
}
