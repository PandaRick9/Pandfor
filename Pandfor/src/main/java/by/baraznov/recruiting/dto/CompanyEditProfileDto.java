package by.baraznov.recruiting.dto;

public record CompanyEditProfileDto(
        Integer companyId,
        String name,
        String description,
        String city,
        String email,
        String phone,
        String logoUrl
) {
}
