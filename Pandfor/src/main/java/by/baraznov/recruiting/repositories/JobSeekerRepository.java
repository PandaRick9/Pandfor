package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.dto.JobSeekerBasicInfoDto;
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
                pi.firstName, 
                pi.lastName, 
                pi.email, 
                pi.phone, 
                pi.city, 
                pi.workExperienceSummary,
                pi.photo.id
            )
            FROM JobSeeker js
            JOIN js.person p
            JOIN PersonalInfo pi ON pi.resume = (SELECT r FROM Resume r WHERE r.jobSeeker = js)
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
}

