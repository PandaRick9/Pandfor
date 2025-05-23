package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.dto.CompanyEditProfileDto;
import by.baraznov.recruiting.dto.JobSeekerBasicInfoDto;
import by.baraznov.recruiting.dto.JobSeekerEditDto;
import by.baraznov.recruiting.dto.JobSeekerProfileDto;
import by.baraznov.recruiting.dto.ResumeAccountPageDTO;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeeker, Integer> {
    Optional<JobSeeker> findByPerson(Person person);

    @Query("""
            SELECT new by.baraznov.recruiting.dto.JobSeekerBasicInfoDto(
                js.firstName, 
                js.lastName, 
                js.email, 
                js.phone, 
                js.city, 
                js.photo.id
            )
            FROM JobSeeker js
            JOIN js.person p
            
            WHERE p.userId = :userId
            """)
    Optional<JobSeekerBasicInfoDto> findBasicInfoByUserId(Integer userId);

    @Query("""
        SELECT new by.baraznov.recruiting.dto.ResumeAccountPageDTO(
            r.resumeId, 
            r.title, 
            r.dateCreated, 
            r.isActive, 
            COUNT(react.reactionId)
        )
        FROM Resume r 
        LEFT JOIN r.reactions react 
        WHERE r.jobSeeker = (SELECT js FROM JobSeeker js JOIN js.person p WHERE p.userId = :userId)
        GROUP BY r.resumeId, r.title, r.dateCreated, r.isActive
        """)
    List<ResumeAccountPageDTO> findResumesByUserId(Integer userId);


    @Query("SELECT new by.baraznov.recruiting.dto.JobSeekerEditDto(" +
            "c.seekerId,c.firstName, c.lastName, c.email, c.phone, c.city, " +
            "CONCAT('/photos/', FUNCTION('STR', c.photo.id))" +
            ") " +
            "FROM JobSeeker c WHERE c.seekerId = :id")
    JobSeekerEditDto findJobProfileById(@Param("id") Integer id);
}

