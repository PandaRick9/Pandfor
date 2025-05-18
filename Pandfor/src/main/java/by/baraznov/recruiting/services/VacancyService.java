package by.baraznov.recruiting.services;

import by.baraznov.recruiting.dto.MatchPercentageDTO;
import by.baraznov.recruiting.dto.VacancyCardDTO;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Vacancy;

import java.util.List;

public interface VacancyService {
    void save(Vacancy vacancy);
    void delete(Integer id);
    Vacancy findOne(Integer id);
    List<Vacancy> findAll();
    List<VacancyCardDTO> findAllVacancies(Integer companyId);
    List<MatchPercentageDTO> findAllMatchPercentage(Integer vacancyId);
}
