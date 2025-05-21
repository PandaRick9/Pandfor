package by.baraznov.recruiting.services;

import by.baraznov.recruiting.dto.MatchPercentageDTO;
import by.baraznov.recruiting.dto.VacancyCardDTO;
import by.baraznov.recruiting.dto.vacancyPage.VacancyDto;

import by.baraznov.recruiting.models.Vacancy;
import by.baraznov.recruiting.models.enums.EmploymentType;
import by.baraznov.recruiting.models.enums.ExperienceYear;
import by.baraznov.recruiting.models.enums.Schedule;
import by.baraznov.recruiting.models.enums.WorkFormat;
import jakarta.validation.Valid;


import java.util.List;

public interface VacancyService {
    void save(Vacancy vacancy);
    void delete(Integer id);
    Vacancy findOne(Integer id);
    List<Vacancy> findAll();
    List<VacancyCardDTO> findAllVacancies(Integer companyId);
    List<MatchPercentageDTO> findAllMatchPercentage(Integer vacancyId);
    VacancyDto getVacancyPageDetails(Integer vacancyId);
    List<Vacancy> searchVacancy(String search);
    List<Vacancy> findByFilters(
            List<WorkFormat> workFormats,
            List<Schedule> schedules,
            ExperienceYear experience,
            List<EmploymentType> employmentTypes,
            String companyName,
            String city,
            Integer minSalary,
            Integer maxSalary) ;

    void updateVacancy(Integer vacancyId, VacancyDto vacancyDto);
}
