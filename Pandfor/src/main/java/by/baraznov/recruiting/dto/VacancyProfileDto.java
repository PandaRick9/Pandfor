package by.baraznov.recruiting.dto;

import java.time.LocalDateTime;

public record VacancyProfileDto(
        Integer id,
        String title,
        Integer salary,
        LocalDateTime createdAt,
        Long responsesCount,
        JobConditionProfileDto jobCondition
) {
}
