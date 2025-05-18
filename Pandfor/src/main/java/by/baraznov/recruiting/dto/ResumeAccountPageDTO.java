package by.baraznov.recruiting.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ResumeAccountPageDTO(
        Integer id,
        String title,
        LocalDateTime updatedDate,
        Boolean isActive,
        Long viewsCount
) {
}
