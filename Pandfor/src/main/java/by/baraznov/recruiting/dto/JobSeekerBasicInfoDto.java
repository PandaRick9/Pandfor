package by.baraznov.recruiting.dto;

public record JobSeekerBasicInfoDto(
        String firstName,
        String lastName,
        String email,
        String phone,
        String city,
        Integer photoId
) {
}
