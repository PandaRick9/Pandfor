package by.baraznov.recruiting.services;

import by.baraznov.recruiting.dto.ReactionShortDTO;
import by.baraznov.recruiting.models.Reaction;
import by.baraznov.recruiting.models.Vacancy;

import java.util.List;

public interface ReactionService {
    void save(Reaction reaction);
    List<ReactionShortDTO> findAllByVacancy(Integer vacancyId);

    void acceptReaction(Integer reactionId);

    void rejectReaction(Integer reactionId);
}
