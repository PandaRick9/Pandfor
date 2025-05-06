package by.baraznov.recruiting.dto;

import by.baraznov.recruiting.models.enums.SkillLevel;
import lombok.Data;

@Data
public class ResumeSkillDTO {
    private String name;
    private String proficiencyLevel;
}