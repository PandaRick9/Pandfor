package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.dto.CompanyEditProfileDto;
import by.baraznov.recruiting.dto.CompanyProfileDto;
import by.baraznov.recruiting.dto.VacancyProfileDto;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Employer;
import by.baraznov.recruiting.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByEmployers(Employer employers);

    @Query("""
        SELECT c FROM Company c
        JOIN c.employers e
        JOIN e.person p
        WHERE p.userId = :userId
        """)
    Optional<Company> findByUserId(Integer userId);

    @Query("SELECT new by.baraznov.recruiting.dto.CompanyEditProfileDto(" +
            "c.companyId, c.name, c.description, c.city, c.email, c.phone, " +
            "CONCAT('/photos/', CAST(c.photo.id AS string)))" +
            "FROM Company c WHERE c.companyId = :companyId")
    CompanyEditProfileDto findCompanyProfileById(@Param("companyId") Integer companyId);
    @Query("""
    SELECT c FROM Company c
    JOIN c.vacancies v
    JOIN v.reactions r
    GROUP BY c
    ORDER BY COUNT(r) DESC
    """)
    List<Company> findTopCompaniesByReactions();
}
