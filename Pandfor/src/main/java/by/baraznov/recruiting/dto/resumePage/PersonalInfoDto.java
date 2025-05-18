package by.baraznov.recruiting.dto.resumePage;

import by.baraznov.recruiting.models.enums.Gender;

import java.time.LocalDate;

public record PersonalInfoDto(
        String firstName,
        String lastName,
        String email,
        String phone,
        String city,
        LocalDate birthDate,
        Gender gender,
        String workExperienceSummary,
        PhotoDto photo
) {
}
