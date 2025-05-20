package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.dto.ReactionShortDTO;
import by.baraznov.recruiting.models.Reaction;
import by.baraznov.recruiting.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    @Query("""
    select new by.baraznov.recruiting.dto.ReactionShortDTO(
        r.resume.jobSeeker.firstName,
         r.resume.jobSeeker.lastName,
        r.status,
        r.createdAt,
        r.coverLetter,
        r.resume.resumeId,
        r.reactionId
    )
    from Reaction r
    where r.vacancy.vacancyId = :vacancyId
    """)
    List<ReactionShortDTO> findAllByVacancyId(Integer vacancyId);
}
