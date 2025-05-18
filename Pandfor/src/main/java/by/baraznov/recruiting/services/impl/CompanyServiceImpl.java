package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.CompanyProfileDto;
import by.baraznov.recruiting.dto.JobConditionProfileDto;
import by.baraznov.recruiting.dto.VacancyProfileDto;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Employer;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.models.Vacancy;
import by.baraznov.recruiting.repositories.CompanyRepository;
import by.baraznov.recruiting.repositories.VacancyRepository;
import by.baraznov.recruiting.services.CompanyService;
import by.baraznov.recruiting.services.VacancyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final VacancyRepository vacancyRepository;

    @Override
    @Transactional
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Optional<Company> findByEmployers(Employer employer) {
        return companyRepository.findByEmployers(employer);
    }

    @Override
    public CompanyProfileDto getProfile(Integer userId) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        List<Vacancy> vacancies = vacancyRepository.findAllByCompanyWithDetails(company);

        List<VacancyProfileDto> vacancyDtos = vacancies.stream()
                .map(v -> new VacancyProfileDto(
                        v.getVacancyId(),
                        v.getTitle(),
                        v.getSalary(),
                        v.getCreatedAt(),
                        (long) v.getReactions().size(),
                        new JobConditionProfileDto(
                                v.getJobCondition().getSchedule(),
                                v.getJobCondition().getEmploymentType(),
                                v.getJobCondition().getWorkFormat(),
                                v.getJobCondition().getRequiredExperienceYears()
                        )
                ))
                .toList();

        return new CompanyProfileDto(
                company.getName(),
                company.getDescription(),
                company.getCity(),
                company.getEmail(),
                company.getPhone(),
                company.getPhoto() != null ? company.getPhoto().getId() : null,
                vacancyDtos
        );
    }

}
