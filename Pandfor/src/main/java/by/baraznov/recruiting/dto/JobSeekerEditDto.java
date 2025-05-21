package by.baraznov.recruiting.dto;

public record JobSeekerEditDto(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String city,
        String photoUrl
) {
}
