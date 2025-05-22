package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.AdminStats;
import by.baraznov.recruiting.repositories.PeopleRepository;
import by.baraznov.recruiting.repositories.ReactionRepository;
import by.baraznov.recruiting.repositories.ResumesRepository;
import by.baraznov.recruiting.repositories.VacancyRepository;
import by.baraznov.recruiting.services.AdminStatsService;
import by.baraznov.recruiting.services.ReactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AdminStatsServiceImpl implements AdminStatsService {
    private final PeopleRepository peopleRepository;
    private final ResumesRepository resumesRepository;
    private final VacancyRepository vacancyRepository;
    private final ReactionRepository reactionRepository;
    @Override
    public AdminStats getStats() {
        AdminStats adminStats = new AdminStats(
                (int) peopleRepository.count(),
                (int) resumesRepository.count(),
                (int) vacancyRepository.count(),
                (int) resumesRepository.count()
        );
        return adminStats;
    }
}
