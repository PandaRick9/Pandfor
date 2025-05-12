package by.baraznov.recruiting.dto;

import by.baraznov.recruiting.models.enums.SkillLevel;

public record MatchVacancySkillDTO(
        Integer vacancyId,
        String name,
        SkillLevel requiredLevel
) {
}
