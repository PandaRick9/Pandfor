package by.baraznov.recruiting.dto;


import by.baraznov.recruiting.models.enums.ReactionStatus;

import java.time.LocalDateTime;

public record ReactionShortDTO(String firstName,
                               String lastName,
                               ReactionStatus status,
                               LocalDateTime createdAt,
                               String coverLetter,
                               Integer resumeId) {}