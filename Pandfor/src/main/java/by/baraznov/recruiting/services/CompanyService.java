package by.baraznov.recruiting.services;


import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Employer;

import java.util.Optional;

public interface CompanyService {
    Company save(Company company);
    Optional<Company> findByEmployers(Employer employer);
}
