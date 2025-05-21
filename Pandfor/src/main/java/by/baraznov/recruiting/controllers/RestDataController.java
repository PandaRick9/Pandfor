package by.baraznov.recruiting.controllers;


import by.baraznov.recruiting.dto.CompanyEditProfileDto;
import by.baraznov.recruiting.dto.JobSeekerEditDto;
import by.baraznov.recruiting.dto.resumePage.ResumeDto;
import by.baraznov.recruiting.dto.vacancyPage.VacancyDto;
import by.baraznov.recruiting.services.CompanyService;
import by.baraznov.recruiting.services.JobSeekerService;
import by.baraznov.recruiting.services.ResumeService;
import by.baraznov.recruiting.services.VacancyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RestDataController {
    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final CompanyService companyService;
    private final JobSeekerService jobSeekerService;

    @GetMapping("/resume/getdata/{id}")
    public ResponseEntity<ResumeDto> getResumeData(@PathVariable Integer id) {
        ResumeDto resume = resumeService.getResumeById(id);
        if (resume != null) {
            return ResponseEntity.ok(resume);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/vacancy/getdata/{id}")
    public ResponseEntity<VacancyDto> getVacancyData(@PathVariable Integer id) {
        VacancyDto vacancyDto = vacancyService.getVacancyPageDetails(id);
        System.out.println(vacancyDto);
        if (vacancyDto != null) {
            return ResponseEntity.ok(vacancyDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/company/getdata/{id}")
    public ResponseEntity<CompanyEditProfileDto> getCompanyData(@PathVariable Integer id) {
        CompanyEditProfileDto companyDto = companyService.getCompanyProfileById(id);
        System.out.println(companyDto);
        return ResponseEntity.ok(companyDto);
    }
    @GetMapping("jobseeker/getdata/{id}")
    public ResponseEntity<JobSeekerEditDto> getJobSeekerData(@PathVariable Integer id) {
        JobSeekerEditDto jobSeekerDto = jobSeekerService.getJobSeekerProfileById(id);
        return ResponseEntity.ok(jobSeekerDto);
    }

}
