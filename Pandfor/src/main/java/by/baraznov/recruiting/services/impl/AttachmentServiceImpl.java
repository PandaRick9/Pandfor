package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Attachment;
import by.baraznov.recruiting.repositories.AttachmentRepository;
import by.baraznov.recruiting.services.AttachmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    @Override
    public Optional<Attachment> findByReaction_ResumeId(Integer resumeId) {
        return attachmentRepository.findByReaction_ResumeId(resumeId);
    }
}
