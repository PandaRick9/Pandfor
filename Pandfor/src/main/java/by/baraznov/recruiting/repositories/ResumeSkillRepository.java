package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.models.ResumeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeSkillRepository extends JpaRepository<ResumeSkill, Integer> {
}
