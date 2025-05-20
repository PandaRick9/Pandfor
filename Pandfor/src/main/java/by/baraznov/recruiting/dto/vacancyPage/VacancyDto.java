package by.baraznov.recruiting.dto.vacancyPage;

import java.time.LocalDateTime;
import java.util.List;

public record VacancyDto(
        Integer vacancyId,
        String title,
        String description,
        Integer salary,
        LocalDateTime createdAt,
        CompanyDto company,
        JobConditionDto jobCondition,
        List<VacancySkillDto> skills
) {
}
