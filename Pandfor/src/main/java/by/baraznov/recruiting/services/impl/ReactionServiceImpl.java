package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.ReactionShortDTO;
import by.baraznov.recruiting.models.Reaction;
import by.baraznov.recruiting.models.Vacancy;
import by.baraznov.recruiting.models.enums.ReactionStatus;
import by.baraznov.recruiting.repositories.ReactionRepository;
import by.baraznov.recruiting.services.ReactionService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    @Override
    @Transactional
    public void save(Reaction reaction) {
        reaction.setStatus(ReactionStatus.NOT_VIEWED);
        reaction.setCreatedAt(LocalDateTime.now());
        reactionRepository.save(reaction);
    }

    @Override
    public List<ReactionShortDTO> findAllByVacancy(Integer vacancyId) {
        return reactionRepository.findAllByVacancyId(vacancyId);
    }
}
