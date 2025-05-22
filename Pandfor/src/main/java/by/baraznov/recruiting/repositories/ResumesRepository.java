package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.Education;
import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.models.ResumeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumesRepository extends JpaRepository<Resume, Integer> {

    List<Resume> findByTitleStartingWith(String search);

    @Query("""
        SELECT r FROM Resume r
        LEFT JOIN FETCH r.jobSeeker js
        LEFT JOIN FETCH js.photo
        LEFT JOIN FETCH r.personalInfo pi
        LEFT JOIN FETCH r.jobPreference
        WHERE r.resumeId = :id
        """)
    Optional<Resume> findByIdWithBasicDetails(Integer id);

    @Query("""
        SELECT e FROM Education e
        WHERE e.personalInfo.infoId = :personalInfoId
        ORDER BY e.graduationDate DESC
        """)
    List<Education> findEducationsByPersonalInfoId(Integer personalInfoId);

    @Query("""
        SELECT rs FROM ResumeSkill rs
        JOIN FETCH rs.skill
        WHERE rs.resume.resumeId = :resumeId
        ORDER BY rs.skill.name
        """)
    List<ResumeSkill> findSkillsByResumeId(Integer resumeId);

    List<Resume> findAllByIsActive(Boolean isActive);
}
