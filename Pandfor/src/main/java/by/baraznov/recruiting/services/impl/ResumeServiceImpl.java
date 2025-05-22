package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.resumePage.EducationDto;
import by.baraznov.recruiting.dto.resumePage.JobPreferenceDto;
import by.baraznov.recruiting.dto.resumePage.JobSeekerDto;
import by.baraznov.recruiting.dto.resumePage.PersonalInfoDto;
import by.baraznov.recruiting.dto.resumePage.PhotoDto;
import by.baraznov.recruiting.dto.resumePage.ResumeDto;
import by.baraznov.recruiting.dto.resumePage.ResumeSkillDto;
import by.baraznov.recruiting.models.Education;
import by.baraznov.recruiting.models.JobPreference;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.PersonalInfo;
import by.baraznov.recruiting.models.Photo;
import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.models.ResumeSkill;
import by.baraznov.recruiting.models.Skill;
import by.baraznov.recruiting.models.enums.EmploymentType;
import by.baraznov.recruiting.models.enums.ExperienceYear;
import by.baraznov.recruiting.models.enums.Schedule;
import by.baraznov.recruiting.models.enums.WorkFormat;
import by.baraznov.recruiting.repositories.JobSeekerRepository;
import by.baraznov.recruiting.repositories.PhotoRepository;
import by.baraznov.recruiting.repositories.ResumeSkillRepository;
import by.baraznov.recruiting.repositories.ResumesRepository;
import by.baraznov.recruiting.repositories.SkillRepository;
import by.baraznov.recruiting.services.ResumeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumesRepository resumesRepository;
    private final EntityManager entityManager;
    private final SkillRepository skillRepository;
    private final ResumeSkillRepository resumeSkillRepository;
    @Override
    @Transactional
    public void save(Resume resume) {
        resume.setDateCreated(LocalDateTime.now());
        resume.setIsActive(true);
        resumesRepository.save(resume);
    }

    @Override
    public Resume findOne(Integer id) {
        return resumesRepository.findById(id).orElse(null);
    }

    @Override
    public List<Resume> findAll() {
        return resumesRepository.findAll();
    }

    @Override
    public List<Resume> findAllActive() {
        return resumesRepository.findAllByIsActive(true);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        resumesRepository.deleteById(id);
    }


    @Override
    public ResumeDto getResumeById(Integer id) {
        Resume resume = resumesRepository.findByIdWithBasicDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Resume not found"));

        List<Education> educations = resume.getPersonalInfo() != null ?
                resumesRepository.findEducationsByPersonalInfoId(resume.getPersonalInfo().getInfoId()) :
                Collections.emptyList();

        List<ResumeSkill> skills = resumesRepository.findSkillsByResumeId(id);

        if (resume.getPersonalInfo() != null) {
            resume.getPersonalInfo().setEducation(educations);
        }
        resume.setResumeSkills(skills);

        return mapToDto(resume);
    }

    @Override
    public List<Resume> searchResumes(String search) {
        return resumesRepository.findByTitleStartingWith(search);
    }

    @Override
    public List<Resume> findByFilters(List<WorkFormat> workFormats,
                                      List<Schedule> schedules, ExperienceYear experience,
                                      List<EmploymentType> employmentTypes, String title,
                                      String city, Integer minSalary, Integer maxSalary) {
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT r.* FROM resume r " +
                        "JOIN jobseeker js ON r.seeker_id = js.seeker_id " +
                        "LEFT JOIN jobpreferences jp ON r.resume_id = jp.resume_id " +
                        "LEFT JOIN personalinfo pi ON r.resume_id = pi.resume_id " +
                        "WHERE r.is_active = true "
        );

        Map<String, Object> params = new HashMap<>();

        if (workFormats != null && !workFormats.isEmpty()) {
            sql.append("AND jp.work_format IN (:workFormats) ");
            params.put("workFormats", workFormats.stream().map(Enum::name).collect(Collectors.toList()));
        }

        if (schedules != null && !schedules.isEmpty()) {
            sql.append("AND jp.schedule IN (:schedules) ");
            params.put("schedules", schedules.stream().map(Enum::name).collect(Collectors.toList()));
        }

        if (experience != null) {
            sql.append("AND jp.experience_years = :experience ");
            params.put("experience", experience.name());
        }

        if (employmentTypes != null && !employmentTypes.isEmpty()) {
            sql.append("AND jp.employment_type IN (:employmentTypes) ");
            params.put("employmentTypes", employmentTypes.stream().map(Enum::name).collect(Collectors.toList()));
        }

        if (minSalary != null) {
            sql.append("AND jp.desired_salary >= :minSalary ");
            params.put("minSalary", minSalary);
        }

        if (maxSalary != null) {
            sql.append("AND jp.desired_salary <= :maxSalary ");
            params.put("maxSalary", maxSalary);
        }

        if (city != null && !city.isBlank()) {
            sql.append("AND LOWER(js.city) = LOWER(:city) ");
            params.put("city", city.toLowerCase());
        }

        if (title != null && !title.isBlank()) {
            sql.append("AND LOWER(r.title) LIKE LOWER(:title) ");
            params.put("title", "%" + title + "%");
        }

        Query query = entityManager.createNativeQuery(sql.toString(), Resume.class);

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
    public void updateResume(Integer resumeId, ResumeDto resumeDto) throws IOException {
        Resume resume = resumesRepository.findById(resumeId)
                .orElseThrow(() -> new EntityNotFoundException("Resume not found"));

        updateResumeFields(resume, resumeDto);
        resumesRepository.save(resume);
    }


    private void updateResumeFields(Resume resume, ResumeDto dto) {
        resume.setTitle(dto.title());
        resume.setIsActive(dto.isActive());

        if (resume.getPersonalInfo() != null) {
            updatePersonalInfo(resume.getPersonalInfo(), dto.personalInfo());
        }

        if (resume.getJobPreference() != null) {
            updateJobPreference(resume.getJobPreference(), dto.jobPreference());
        }

        updateSkills(resume, dto.resumeSkills());
        if (resume.getJobSeeker() != null && dto.jobSeeker() != null) {
            updateJobSeeker(resume.getJobSeeker(), dto.jobSeeker());
        }
    }
    private void updateJobSeeker(JobSeeker jobSeeker, JobSeekerDto dto) {
        jobSeeker.setFirstName(dto.firstName());
        jobSeeker.setLastName(dto.lastName());
        jobSeeker.setPatronymic(dto.patronymic());
        jobSeeker.setPhone(dto.phone());
        jobSeeker.setEmail(dto.email());
        jobSeeker.setCity(dto.city());
    }
    private void updatePersonalInfo(PersonalInfo personalInfo, PersonalInfoDto dto) {
        personalInfo.setBirthDate(dto.birthDate());
        personalInfo.setGender(dto.gender());
        personalInfo.setWorkExperienceSummary(dto.workExperienceSummary());

        personalInfo.getEducation().clear();
        dto.education().forEach(eduDto -> {
            Education education = new Education();
            education.setInstitution(eduDto.institution());
            education.setSpecialization(eduDto.specialization());
            education.setGraduationDate(eduDto.graduationDate());
            education.setPersonalInfo(personalInfo);
            personalInfo.getEducation().add(education);
        });
    }

    private void updateJobPreference(JobPreference jobPreference, JobPreferenceDto dto) {
        jobPreference.setSchedule(dto.schedule());
        jobPreference.setEmploymentType(dto.employmentType());
        jobPreference.setWorkFormat(dto.workFormat());
        jobPreference.setDesiredSalary(dto.desiredSalary());
        jobPreference.setExperienceYear(dto.experienceYear());

    }

    private void updateSkills(Resume resume, List<ResumeSkillDto> skillDtos) {

        resumeSkillRepository.deleteAllByResumeId(resume.getResumeId());
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

            ResumeSkill resumeSkill = new ResumeSkill();
            resumeSkill.setProficiencyLevel(skillDto.proficiencyLevel());
            resumeSkill.setSkill(skill);
            resumeSkill.setResume(resume);
            resume.getResumeSkills().add(resumeSkill);
        });
    }


    private ResumeDto mapToDto(Resume resume) {
        return new ResumeDto(
                resume.getResumeId(),
                resume.getTitle(),
                resume.getDateCreated(),
                resume.getIsActive(),
                mapToJobSeekerDto(resume.getJobSeeker()),
                mapToPersonalInfoDto(resume.getPersonalInfo()),
                mapToJobPreferenceDto(resume.getJobPreference()),
                mapToResumeSkillDtos(resume.getResumeSkills())
        );
    }
    private JobSeekerDto mapToJobSeekerDto(JobSeeker jobSeeker) {
        return new JobSeekerDto(
                jobSeeker.getSeekerId(),
                jobSeeker.getFirstName(),
                jobSeeker.getLastName(),
                jobSeeker.getPatronymic(),
                jobSeeker.getPhone(),
                jobSeeker.getEmail(),
                jobSeeker.getCity(),
                jobSeeker.getPhoto() != null ?
                        new PhotoDto(jobSeeker.getPhoto().getId(), jobSeeker.getPhoto().getContentType()) :
                        null
        );
    }
    private PersonalInfoDto mapToPersonalInfoDto(PersonalInfo personalInfo) {
        if (personalInfo == null) return null;

        return new PersonalInfoDto(
                personalInfo.getInfoId(),
                personalInfo.getBirthDate(),
                personalInfo.getGender(),
                personalInfo.getWorkExperienceSummary(),
                personalInfo.getEducation() != null ?
                        mapToEducationDtos(personalInfo.getEducation()) :
                        Collections.emptyList()
        );
    }
    private List<EducationDto> mapToEducationDtos(List<Education> educations) {
        return educations.stream()
                .map(edu -> new EducationDto(
                        edu.getEducationId(),
                        edu.getInstitution(),
                        edu.getSpecialization(),
                        edu.getGraduationDate()
                ))
                .toList();
    }
    private JobPreferenceDto mapToJobPreferenceDto(JobPreference jobPreference) {
        if (jobPreference == null) return null;

        return new JobPreferenceDto(
                jobPreference.getPreferenceId(),
                jobPreference.getSchedule(),
                jobPreference.getEmploymentType(),
                jobPreference.getWorkFormat(),
                jobPreference.getDesiredSalary(),
                jobPreference.getExperienceYear()
        );
    }
    private List<ResumeSkillDto> mapToResumeSkillDtos(List<ResumeSkill> resumeSkills) {
        return resumeSkills.stream()
                .map(skill -> new ResumeSkillDto(
                        skill.getResumeSkillId(),
                        skill.getSkill().getName(),
                        skill.getProficiencyLevel()
                ))
                .toList();
    }


}
