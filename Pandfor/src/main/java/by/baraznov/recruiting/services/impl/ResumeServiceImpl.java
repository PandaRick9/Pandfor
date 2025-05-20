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
import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.models.ResumeSkill;
import by.baraznov.recruiting.repositories.ResumesRepository;
import by.baraznov.recruiting.services.ResumeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumesRepository resumesRepository;
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
