package by.baraznov.recruiting.services;

import by.baraznov.recruiting.dto.resumePage.ResumeDto;
import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.models.enums.EmploymentType;
import by.baraznov.recruiting.models.enums.ExperienceYear;
import by.baraznov.recruiting.models.enums.Schedule;
import by.baraznov.recruiting.models.enums.WorkFormat;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface ResumeService {
    void save(Resume resume);
    Resume findOne(Integer id);
    List<Resume> findAll();
    List<Resume> findAllActive();
    void delete(Integer id);
    ResumeDto getResumeById(Integer id);
    List<Resume> searchResumes(String search);

    List<Resume> findByFilters(List<WorkFormat> workFormats, List<Schedule> schedules, ExperienceYear experience, List<EmploymentType> employmentTypes, String title, String city, Integer minSalary, Integer maxSalary);


    void updateResume(Integer id, ResumeDto resumeDto) throws IOException;
}
