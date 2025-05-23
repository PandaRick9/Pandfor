package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.models.ResumeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ResumeSkillRepository extends JpaRepository<ResumeSkill, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM ResumeSkill rs WHERE rs.resume.resumeId = :resumeId")
    void deleteAllByResumeId(@Param("resumeId") Integer resumeId);
}
