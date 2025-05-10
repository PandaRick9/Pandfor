package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Reaction;
import by.baraznov.recruiting.models.enums.ReactionStatus;
import by.baraznov.recruiting.repositories.ReactionRepository;
import by.baraznov.recruiting.services.ReactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    @Override
    @Transactional
    public void save(Reaction reaction) {
        reaction.setStatus(ReactionStatus.NOT_VIEWED);
        System.out.println("я туту");
        reactionRepository.save(reaction);
    }
}
