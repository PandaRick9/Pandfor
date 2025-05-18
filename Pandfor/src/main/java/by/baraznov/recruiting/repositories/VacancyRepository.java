package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.dto.JobConditionDTO;
import by.baraznov.recruiting.dto.MatchJobConditionDTO;
import by.baraznov.recruiting.dto.MatchJobPreferenceDTO;
import by.baraznov.recruiting.dto.MatchPercentageDTO;
import by.baraznov.recruiting.dto.MatchResumeSkillDTO;
import by.baraznov.recruiting.dto.MatchVacancySkillDTO;
import by.baraznov.recruiting.dto.ResumeSkillDTO;
import by.baraznov.recruiting.dto.VacancyCardDTO;
import by.baraznov.recruiting.dto.VacancySkillDTO;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Integer> {
    @Query("""
        select new by.baraznov.recruiting.dto.VacancyCardDTO(
            v.vacancyId,
            v.title,
            v.salary,
            v.jobCondition.employmentType,
            v.jobCondition.requiredExperienceYears
        )
        FROM Vacancy v
            where v.company.companyId = :companyId
    """)
    List<VacancyCardDTO> findAllByCompany(Integer companyId);

    @Query("""
        SELECT new by.baraznov.recruiting.dto.MatchResumeSkillDTO(
            rs.resume.resumeId,
            rs.skill.name,
            rs.proficiencyLevel
        )
        FROM ResumeSkill rs
        JOIN Reaction r ON r.resume.resumeId = rs.resume.resumeId
        WHERE r.vacancy.vacancyId = :vacancyId
    """)
    List<MatchResumeSkillDTO> findCandidateSkillsByVacancyId(Integer vacancyId);


    @Query("""
        SELECT new by.baraznov.recruiting.dto.MatchVacancySkillDTO(
            vs.vacancy.vacancyId,
            vs.skill.name,
            vs.requiredLevel
        )
        FROM VacancySkill vs
        WHERE vs.vacancy.vacancyId = :vacancyId
    """)
    List<MatchVacancySkillDTO> findVacancySkills(Integer vacancyId);

    @Query("""
    select new by.baraznov.recruiting.dto.MatchJobConditionDTO(
            jc.vacancy.vacancyId,
            jc.schedule,
            jc.employmentType,
            jc.workFormat,
            jc.requiredExperienceYears,
            v.salary
        )
        from JobCondition jc
        join jc.vacancy v
        where v.vacancyId = :vacancyId
    """)
    MatchJobConditionDTO findJobConditionByVacancyId(@Param("vacancyId") Integer vacancyId);
    @Query("""
    select new by.baraznov.recruiting.dto.MatchJobPreferenceDTO(
            jp.resume.resumeId,
            jp.schedule,
            jp.employmentType,
            jp.workFormat,
            jp.experienceYear,
            jp.desiredSalary
        )
        from JobPreference jp
        join jp.resume r
        join Reaction reaction on reaction.resume.resumeId = r.resumeId
        where reaction.vacancy.vacancyId = :vacancyId
    """)
    List<MatchJobPreferenceDTO> findJobPreferencesByVacancyId(@Param("vacancyId") Integer vacancyId);

    @Query("""
        SELECT v FROM Vacancy v
        LEFT JOIN FETCH v.reactions
        JOIN FETCH v.jobCondition
        WHERE v.company = :company
        """)
    List<Vacancy> findAllByCompanyWithDetails(Company company);


}
