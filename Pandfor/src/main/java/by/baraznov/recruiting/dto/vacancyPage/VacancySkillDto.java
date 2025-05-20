package by.baraznov.recruiting.dto.vacancyPage;

import by.baraznov.recruiting.models.enums.SkillLevel;

public record VacancySkillDto(
        String skillName,
        SkillLevel requiredLevel
) {
}
