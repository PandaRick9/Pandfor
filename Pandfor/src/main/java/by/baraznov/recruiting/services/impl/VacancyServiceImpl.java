package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Vacancy;
import by.baraznov.recruiting.models.VacancySkill;
import by.baraznov.recruiting.repositories.VacancyRepository;
import by.baraznov.recruiting.services.VacancyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;
    @Override
    @Transactional
    public void save(Vacancy vacancy) {
        vacancyRepository.save(vacancy);
    }
}
