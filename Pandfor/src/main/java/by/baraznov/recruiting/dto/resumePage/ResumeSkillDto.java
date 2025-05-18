package by.baraznov.recruiting.dto.resumePage;

import by.baraznov.recruiting.models.enums.SkillLevel;

public record ResumeSkillDto(
        Integer id,
        String skillName,
        SkillLevel proficiencyLevel
) {
}
