package by.baraznov.recruiting.services;

import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Vacancy;

import java.util.List;

public interface VacancyService {
    void save(Vacancy vacancy);
    List<Vacancy> findAll();
    List<Vacancy> findAllVacancies(Company company);
}
