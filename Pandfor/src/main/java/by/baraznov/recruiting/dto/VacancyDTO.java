package by.baraznov.recruiting.dto;

import lombok.Data;

import java.util.List;

@Data
public class VacancyDTO {
    private String title;
    private String description;
    private Integer salary;
    private JobConditionDTO jobCondition;
    private List<VacancySkillDTO> skills;
}
