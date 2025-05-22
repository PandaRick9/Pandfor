package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.CompanyEditProfileDto;
import by.baraznov.recruiting.dto.CompanyProfileDto;
import by.baraznov.recruiting.dto.JobConditionProfileDto;
import by.baraznov.recruiting.dto.VacancyProfileDto;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Employer;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.models.Photo;
import by.baraznov.recruiting.models.Vacancy;
import by.baraznov.recruiting.repositories.CompanyRepository;
import by.baraznov.recruiting.repositories.PhotoRepository;
import by.baraznov.recruiting.repositories.VacancyRepository;
import by.baraznov.recruiting.services.CompanyService;
import by.baraznov.recruiting.services.VacancyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final VacancyRepository vacancyRepository;
    private final PhotoRepository photoRepository;

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

    @Override
    public CompanyEditProfileDto getCompanyProfileById(Integer companyId) {
        return companyRepository.findCompanyProfileById(companyId);
    }

    @Override
    @Transactional
    public void updateCompanyProfile(Integer id,MultipartFile logoFile, CompanyEditProfileDto companyDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        updateCompanyFields(company, companyDto);

        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                updateCompanyLogo(company, logoFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        companyRepository.save(company);
    }

    @Override
    public List<Company> findTopCompany() {
        return companyRepository.findTopCompaniesByReactions().stream().limit(5).collect(Collectors.toList());
    }

    private void updateCompanyFields(Company company, CompanyEditProfileDto dto) {
        company.setName(dto.name());
        company.setDescription(dto.description());
        company.setCity(dto.city());
        company.setEmail(dto.email());
        company.setPhone(dto.phone());
    }

    private void updateCompanyLogo(Company company, MultipartFile logoFile) throws IOException {
        Photo photo;
        if (company.getPhoto() != null) {
            photo = company.getPhoto();
            photo.setFileName(logoFile.getOriginalFilename());
            photo.setContentType(logoFile.getContentType());
            photo.setData(logoFile.getBytes());
        } else {
            photo = new Photo();
            photo.setFileName(logoFile.getOriginalFilename());
            photo.setContentType(logoFile.getContentType());
            photo.setData(logoFile.getBytes());
            photo = photoRepository.save(photo);
        }
        company.setPhoto(photo);
    }

}
