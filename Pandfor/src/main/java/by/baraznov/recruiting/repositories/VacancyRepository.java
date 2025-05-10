package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Integer> {
    List<Vacancy> findAllByCompany(Company company);
}
