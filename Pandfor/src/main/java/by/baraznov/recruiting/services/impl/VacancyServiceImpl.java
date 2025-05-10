package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Vacancy;
import by.baraznov.recruiting.models.VacancySkill;
import by.baraznov.recruiting.repositories.VacancyRepository;
import by.baraznov.recruiting.services.VacancyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public Vacancy findOne(Integer id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }

    @Override
    public List<Vacancy> findAllVacancies(Company company) {
        return vacancyRepository.findAllByCompany(company);
    }
}
