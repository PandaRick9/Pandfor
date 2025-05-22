package by.baraznov.recruiting.services;

import by.baraznov.recruiting.models.Attachment;

import java.util.Optional;

public interface AttachmentService {
    Optional<Attachment> findByReaction_ResumeId(Integer resumeId);
}
