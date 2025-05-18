package by.baraznov.recruiting.dto.resumePage;

import java.time.LocalDateTime;
import java.util.List;

public record ResumeDto(
        Integer id,
        String title,
        LocalDateTime dateCreated,
        PersonalInfoDto personalInfo,
        JobPreferenceDto jobPreference,
        List<EducationDto> educations,
        List<ResumeSkillDto> resumeSkills
) {}
