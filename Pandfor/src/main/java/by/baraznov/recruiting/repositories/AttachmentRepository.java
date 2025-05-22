package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {

    @Query("SELECT a FROM Attachment a WHERE a.reaction.resume.resumeId = :resumeId")
    Optional<Attachment> findByReaction_ResumeId(@Param("resumeId") Integer resumeId);
}
