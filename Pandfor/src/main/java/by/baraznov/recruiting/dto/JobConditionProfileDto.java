package by.baraznov.recruiting.dto;

import by.baraznov.recruiting.models.enums.EmploymentType;
import by.baraznov.recruiting.models.enums.ExperienceYear;
import by.baraznov.recruiting.models.enums.Schedule;
import by.baraznov.recruiting.models.enums.WorkFormat;

public record JobConditionProfileDto(
        Schedule schedule,
        EmploymentType employmentType,
        WorkFormat workFormat,
        ExperienceYear requiredExperienceYears
) {
}
