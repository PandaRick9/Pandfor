package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.VacancySkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface VacancySkillRepository extends JpaRepository<VacancySkill, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM VacancySkill vs WHERE vs.vacancy.vacancyId = :vacancyId")
    void deleteAllByVacancyId(@Param("vacancyId") Integer vacancyId);
}
