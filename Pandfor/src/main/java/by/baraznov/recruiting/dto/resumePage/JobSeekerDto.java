package by.baraznov.recruiting.dto.resumePage;

public record JobSeekerDto(
        Integer id,
        String firstName,
        String lastName,
        String patronymic,
        String phone,
        String email,
        String city,
        PhotoDto photo
) {
}
