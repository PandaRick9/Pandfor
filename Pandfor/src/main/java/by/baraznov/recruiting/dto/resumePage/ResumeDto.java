package by.baraznov.recruiting.dto.resumePage;

import java.time.LocalDateTime;
import java.util.List;

public record ResumeDto(
        Integer id,
        String title,
        LocalDateTime dateCreated,
        Boolean isActive,
        JobSeekerDto jobSeeker,
        PersonalInfoDto personalInfo,
        JobPreferenceDto jobPreference,
        List<ResumeSkillDto> resumeSkills
) {}
