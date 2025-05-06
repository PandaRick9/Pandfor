package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.ResumeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeSkillRepository extends JpaRepository<ResumeSkill, Integer> {
}
