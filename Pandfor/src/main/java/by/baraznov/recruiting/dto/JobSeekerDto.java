package by.baraznov.recruiting.dto;

public record JobSeekerDto(
        String firstName,
        String lastName,
        String patronymic,
        String email,
        String phone,
        String city
) {
}
