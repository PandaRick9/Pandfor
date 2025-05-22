package by.baraznov.recruiting.services;


import by.baraznov.recruiting.dto.CompanyEditProfileDto;
import by.baraznov.recruiting.dto.CompanyProfileDto;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Employer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    Company save(Company company);
    Optional<Company> findByEmployers(Employer employer);
    CompanyProfileDto getProfile(Integer userId);
    CompanyEditProfileDto getCompanyProfileById(Integer companyId);
    void updateCompanyProfile(Integer id,MultipartFile logoFile, CompanyEditProfileDto companyDto);

    List<Company> findTopCompany();
}
