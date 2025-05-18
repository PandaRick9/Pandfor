package by.baraznov.recruiting.dto.resumePage;

import by.baraznov.recruiting.models.enums.EmploymentType;
import by.baraznov.recruiting.models.enums.ExperienceYear;
import by.baraznov.recruiting.models.enums.Schedule;
import by.baraznov.recruiting.models.enums.WorkFormat;

public record JobPreferenceDto(
        Integer id,
        Schedule schedule,
        EmploymentType employmentType,
        WorkFormat workFormat,
        Integer desiredSalary,
        ExperienceYear experienceYear
) {
}
