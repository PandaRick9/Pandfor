package by.baraznov.recruiting.dto.resumePage;

import by.baraznov.recruiting.models.enums.Gender;

import java.time.LocalDate;
import java.util.List;

public record PersonalInfoDto(
        Integer id,
        LocalDate birthDate,
        Gender gender,
        String workExperienceSummary,
        List<EducationDto> education
) {
}
