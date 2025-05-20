package by.baraznov.recruiting.dto.vacancyPage;

import by.baraznov.recruiting.models.enums.EmploymentType;
import by.baraznov.recruiting.models.enums.ExperienceYear;
import by.baraznov.recruiting.models.enums.Schedule;
import by.baraznov.recruiting.models.enums.WorkFormat;

public record JobConditionDto(
        Schedule schedule,
        EmploymentType employmentType,
        WorkFormat workFormat,
        ExperienceYear requiredExperienceYears
) {
}
