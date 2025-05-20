package by.baraznov.recruiting.dto.vacancyPage;

public record CompanyDto(
        Integer companyId,
        String name,
        String description,
        String city,
        String email,
        String phone,
        String photoUrl
) {
}
