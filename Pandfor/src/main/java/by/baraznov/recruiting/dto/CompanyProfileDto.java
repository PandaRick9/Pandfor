package by.baraznov.recruiting.dto;

import java.util.List;

public record CompanyProfileDto(
        String name,
        String description,
        String city,
        String email,
        String phone,
        Integer photoId,
        List<VacancyProfileDto> vacancies
) {
}
