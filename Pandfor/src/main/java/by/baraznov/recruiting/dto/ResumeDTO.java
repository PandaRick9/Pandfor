package by.baraznov.recruiting.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResumeDTO {
    private String title;
    private PersonalInfoDTO personalInfo;
    private JobPreferenceDTO jobPreference;
    private List<EducationDTO> educationList;
    private List<ResumeSkillDTO> skills;
}
