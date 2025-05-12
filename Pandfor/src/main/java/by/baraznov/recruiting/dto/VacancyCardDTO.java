package by.baraznov.recruiting.dto;

import by.baraznov.recruiting.models.enums.EmploymentType;
import by.baraznov.recruiting.models.enums.ExperienceYear;

public record VacancyCardDTO(
        Integer vacancyId,
        String title,
        Integer salary,
        EmploymentType employmentType,
        ExperienceYear requiredExperienceYears
) {
}
