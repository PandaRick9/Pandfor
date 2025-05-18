package by.baraznov.recruiting.dto;

import lombok.Data;

import java.util.List;

public record JobSeekerProfileDto(
        String firstName,
        String lastName,
        String email,
        String phone,
        String city,
        String workExperienceSummary,
        String photoUrl,
        List<ResumeAccountPageDTO> resumes
) {
}
