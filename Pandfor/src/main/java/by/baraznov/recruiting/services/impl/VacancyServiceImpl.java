package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.MatchJobConditionDTO;
import by.baraznov.recruiting.dto.MatchJobPreferenceDTO;
import by.baraznov.recruiting.dto.MatchPercentageDTO;
import by.baraznov.recruiting.dto.MatchResumeSkillDTO;
import by.baraznov.recruiting.dto.MatchVacancySkillDTO;
import by.baraznov.recruiting.dto.VacancyCardDTO;
import by.baraznov.recruiting.dto.vacancyPage.CompanyDto;
import by.baraznov.recruiting.dto.vacancyPage.JobConditionDto;
import by.baraznov.recruiting.dto.vacancyPage.VacancyDto;
import by.baraznov.recruiting.dto.vacancyPage.VacancySkillDto;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.JobCondition;
import by.baraznov.recruiting.models.Skill;
import by.baraznov.recruiting.models.Vacancy;
import by.baraznov.recruiting.models.VacancySkill;
import by.baraznov.recruiting.models.enums.EmploymentType;
import by.baraznov.recruiting.models.enums.ExperienceYear;
import by.baraznov.recruiting.models.enums.Schedule;
import by.baraznov.recruiting.models.enums.WorkFormat;
import by.baraznov.recruiting.repositories.SkillRepository;
import by.baraznov.recruiting.repositories.VacancyRepository;
import by.baraznov.recruiting.repositories.VacancySkillRepository;
import by.baraznov.recruiting.services.VacancyService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;
    private final VacancySkillRepository vacancySkillRepository;
    private final SkillRepository skillRepository;

    @Override
    @Transactional
    public void save(Vacancy vacancy) {
        vacancyRepository.save(vacancy);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        vacancyRepository.deleteById(id);
    }

    @Override
    public Vacancy findOne(Integer id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }

    @Override
    public List<VacancyCardDTO> findAllVacancies(Integer companyId) {
        return vacancyRepository.findAllByCompany(companyId);
    }

    public List<MatchPercentageDTO> findAllMatchPercentage(Integer vacancyId) {
        List<MatchResumeSkillDTO> allCandidateSkills = vacancyRepository.findCandidateSkillsByVacancyId(vacancyId);
        List<MatchVacancySkillDTO> requiredSkills = vacancyRepository.findVacancySkills(vacancyId);
        List<MatchJobPreferenceDTO> jobPreferences = vacancyRepository.findJobPreferencesByVacancyId(vacancyId);
        MatchJobConditionDTO jobCondition = vacancyRepository.findJobConditionByVacancyId(vacancyId);

        Map<Integer, List<MatchResumeSkillDTO>> skillsByResume = allCandidateSkills.stream()
                .collect(Collectors.groupingBy(MatchResumeSkillDTO::resumeId));

        Map<Integer, MatchJobPreferenceDTO> prefsByResumeId = jobPreferences.stream()
                .collect(Collectors.toMap(MatchJobPreferenceDTO::resumeId, jp -> jp));

        List<MatchPercentageDTO> result = new ArrayList<>();

        for (Map.Entry<Integer, List<MatchResumeSkillDTO>> entry : skillsByResume.entrySet()) {
            Integer resumeId = entry.getKey();
            List<MatchResumeSkillDTO> candidateSkills = entry.getValue();
            MatchJobPreferenceDTO jobPref = prefsByResumeId.get(resumeId);

            result.add(new MatchPercentageDTO(candidateSkills, requiredSkills, jobPref, jobCondition));
        }

        return result;
    }

    @Override
    public VacancyDto getVacancyPageDetails(Integer vacancyId) {
        VacancyDto vacancy = vacancyRepository.findBasicVacancyInfoById(vacancyId)
                .orElseThrow(() -> new EntityNotFoundException("Вакансия не найдена"));

        CompanyDto company = vacancyRepository.findCompanyDetailsByVacancyId(vacancyId).orElse(null);
        JobConditionDto jobCondition = vacancyRepository.findJobConditionsByVacancyId(vacancyId).orElse(null);
        List<VacancySkillDto> skills = vacancyRepository.findRequiredSkillsByVacancyId(vacancyId);

        return new VacancyDto(
                vacancy.vacancyId(),
                vacancy.title(),
                vacancy.description(),
                vacancy.salary(),
                vacancy.createdAt(),
                company,
                jobCondition,
                skills
        );
    }

    @Override
    public List<Vacancy> searchVacancy(String search) {
        return vacancyRepository.findByTitleStartingWith(search);
    }

    private final EntityManager entityManager;

    @Override
    public List<Vacancy> findByFilters(
            List<WorkFormat> workFormats,
            List<Schedule> schedules,
            ExperienceYear experience,
            List<EmploymentType> employmentTypes,
            String companyName,
            String city,
            Integer minSalary,
            Integer maxSalary) {

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT v.* FROM vacancy v " +
                        "JOIN company c ON v.company_id = c.company_id " +
                        "LEFT JOIN jobconditions jc ON v.vacancy_id = jc.vacancy_id " +
                        "WHERE 1=1 "
        );

        Map<String, Object> params = new HashMap<>();

        if (workFormats != null && !workFormats.isEmpty()) {
            sql.append("AND jc.work_format IN (:workFormats) ");
            params.put("workFormats", workFormats.stream().map(Enum::name).collect(Collectors.toList()));
        }

        if (schedules != null && !schedules.isEmpty()) {
            sql.append("AND jc.schedule IN (:schedules) ");
            params.put("schedules", schedules.stream().map(Enum::name).collect(Collectors.toList()));
        }

        if (experience != null) {
            sql.append("AND jc.required_experience_years = :experience ");
            params.put("experience", experience.name());
        }

        if (employmentTypes != null && !employmentTypes.isEmpty()) {
            sql.append("AND jc.employment_type IN (:employmentTypes) ");
            params.put("employmentTypes", employmentTypes.stream().map(Enum::name).collect(Collectors.toList()));
        }

        if (companyName != null && !companyName.isBlank()) {
            sql.append("AND LOWER(c.name) LIKE LOWER(:companyName) ");
            params.put("companyName", "%" + companyName + "%");
        }

        if (city != null && !city.isBlank()) {
            sql.append("AND LOWER(c.city) = LOWER(:city) ");
            params.put("city", city.toLowerCase());
        }

        if (minSalary != null) {
            sql.append("AND v.salary >= :minSalary ");
            params.put("minSalary", minSalary);
        }

        if (maxSalary != null) {
            sql.append("AND v.salary <= :maxSalary ");
            params.put("maxSalary", maxSalary);
        }

        Query query = entityManager.createNativeQuery(sql.toString(), Vacancy.class);

        params.forEach((key, value) -> {
            if (value instanceof List<?>) {
                query.setParameter(key, (List<?>) value);
            } else {
                query.setParameter(key, value);
            }
        });

        return query.getResultList();
    }

    @Override
    @Transactional
    public void updateVacancy(Integer vacancyId, VacancyDto vacancyDto) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new EntityNotFoundException("Vacancy not found"));

        updateVacancyFields(vacancy, vacancyDto);
        vacancyRepository.save(vacancy);
    }

    @Override
    public List<Vacancy> findRecommended() {
        List<Vacancy> allVacancies = vacancyRepository.findAll();
        List<Vacancy> topVacancies = allVacancies.stream()
                .sorted((v1, v2) -> Integer.compare(v2.getReactions().size(), v1.getReactions().size()))
                .limit(10)
                .collect(Collectors.toList());
        if (topVacancies.size() <= 3) {
            return topVacancies;
        }

        Collections.shuffle(topVacancies);
        return topVacancies.subList(0, 3);
    }

    private void updateVacancyFields(Vacancy vacancy, VacancyDto dto) {
        vacancy.setTitle(dto.title());
        vacancy.setSalary(dto.salary());
        vacancy.setDescription(dto.description());


        if (vacancy.getJobCondition() != null && dto.jobCondition() != null) {
            updateJobCondition(vacancy.getJobCondition(), dto.jobCondition());
        }

        updateSkills(vacancy, dto.skills());
    }



    private void updateJobCondition(JobCondition jobCondition, JobConditionDto dto) {
        jobCondition.setSchedule(dto.schedule());
        jobCondition.setEmploymentType(dto.employmentType());
        jobCondition.setWorkFormat(dto.workFormat());
        jobCondition.setRequiredExperienceYears(dto.requiredExperienceYears());
    }

    private void updateSkills(Vacancy vacancy, List<VacancySkillDto> skillDtos) {
        vacancySkillRepository.deleteAllByVacancyId(vacancy.getVacancyId());

        skillDtos.forEach(skillDto -> {
            if (skillDto.skillName() == null || skillDto.skillName().isBlank()) {
                return;
            }

            Skill skill = skillRepository.findByName(skillDto.skillName())
                    .orElseGet(() -> {
                        Skill newSkill = new Skill();
                        newSkill.setName(skillDto.skillName());
                        return skillRepository.save(newSkill);
                    });

            VacancySkill vacancySkill = new VacancySkill();
            vacancySkill.setRequiredLevel(skillDto.requiredLevel());
            vacancySkill.setSkill(skill);
            vacancySkill.setVacancy(vacancy);
            vacancy.getVacancySkills().add(vacancySkill);
        });
    }

}


