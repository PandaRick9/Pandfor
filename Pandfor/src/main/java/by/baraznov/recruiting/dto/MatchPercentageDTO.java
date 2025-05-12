package by.baraznov.recruiting.dto;

import java.util.List;

public record MatchPercentageDTO(
        List<MatchResumeSkillDTO> candidateSkills,
        List<MatchVacancySkillDTO> requiredSkills,
        MatchJobPreferenceDTO jobPreference,
        MatchJobConditionDTO jobCondition
) {
}
