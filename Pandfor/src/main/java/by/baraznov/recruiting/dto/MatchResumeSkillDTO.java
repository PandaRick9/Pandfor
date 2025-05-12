package by.baraznov.recruiting.dto;

import by.baraznov.recruiting.models.enums.SkillLevel;

public record MatchResumeSkillDTO(
        Integer resumeId,
        String name,
        SkillLevel proficiencyLevel
) {
}
