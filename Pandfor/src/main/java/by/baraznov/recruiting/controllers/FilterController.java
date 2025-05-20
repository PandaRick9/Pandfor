package by.baraznov.recruiting.controllers;


import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.models.Vacancy;
import by.baraznov.recruiting.models.enums.EmploymentType;
import by.baraznov.recruiting.models.enums.ExperienceYear;
import by.baraznov.recruiting.models.enums.Schedule;
import by.baraznov.recruiting.models.enums.WorkFormat;
import by.baraznov.recruiting.services.JobSeekerService;
import by.baraznov.recruiting.services.ResumeService;
import by.baraznov.recruiting.services.VacancyService;
import by.baraznov.recruiting.services.impl.CurrentUserProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/filters")
@AllArgsConstructor
public class FilterController {

    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final CurrentUserProvider currentUserProvider;
    private final JobSeekerService jobSeekerService;

    @ModelAttribute("role")
    public String addRoleToModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("ROLE_GUEST");
        }
        return "ROLE_GUEST";
    }

    @GetMapping("/resume")
    public String getFilteredResumes(
            @RequestParam(name = "workFormats", required = false) List<WorkFormat> workFormats,
            @RequestParam(name = "schedules", required = false) List<Schedule> schedules,
            @RequestParam(name = "experience", required = false) ExperienceYear experience,
            @RequestParam(name = "employmentTypes", required = false) List<EmploymentType> employmentTypes,
            @RequestParam(name = "companyName", required = false) String companyName,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "minSalary", required = false) Integer minSalary,
            @RequestParam(name = "maxSalary", required = false) Integer maxSalary,
            Model model) {


        if (experience != null && experience.equals(ExperienceYear.IT_DOESNT_METTER)) {
            experience = null;
        }
        if (minSalary != null && maxSalary != null && minSalary > maxSalary) {
            model.addAttribute("error", "Минимальная зарплата не может быть больше максимальной");
        } else {
            List<Resume> resumes = resumeService.findByFilters(
                    workFormats, schedules, experience, employmentTypes,
                    companyName, city, minSalary, maxSalary
            );
            model.addAttribute("resumes", resumes);
        }
        model.addAttribute("allWorkFormats", WorkFormat.values());
        model.addAttribute("allSchedules", Schedule.values());
        model.addAttribute("allExperienceLevels", ExperienceYear.values());
        model.addAttribute("allEmploymentTypes", EmploymentType.values());

        return "allResumePage";
    }





    @GetMapping("/vacancy")
    public String getFilteredVacancies(
            @RequestParam(name = "workFormats", required = false) List<WorkFormat> workFormats,
            @RequestParam(name = "schedules", required = false) List<Schedule> schedules,
            @RequestParam(name = "experience", required = false) ExperienceYear experience,
            @RequestParam(name = "employmentTypes", required = false) List<EmploymentType> employmentTypes,
            @RequestParam(name = "companyName", required = false) String companyName,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "minSalary", required = false) Integer minSalary,
            @RequestParam(name = "maxSalary", required = false) Integer maxSalary,
            Model model) {


        if (experience != null && experience.equals(ExperienceYear.IT_DOESNT_METTER)) {
            experience = null;
        }
        if (minSalary != null && maxSalary != null && minSalary > maxSalary) {
            model.addAttribute("error", "Минимальная зарплата не может быть больше максимальной");
        } else {
            List<Vacancy> vacancies = vacancyService.findByFilters(
                    workFormats, schedules, experience, employmentTypes,
                    companyName, city, minSalary, maxSalary
            );
            model.addAttribute("vacancies", vacancies);
        }
        model.addAttribute("allWorkFormats", WorkFormat.values());
        model.addAttribute("allSchedules", Schedule.values());
        model.addAttribute("allExperienceLevels", ExperienceYear.values());
        model.addAttribute("allEmploymentTypes", EmploymentType.values());
        Person person = currentUserProvider.getCurrentPerson().getPerson();
        Optional<JobSeeker> jobSeekerOptional = jobSeekerService.findByPerson(person);
        if (jobSeekerOptional.isPresent()) {
            JobSeeker jobSeeker = jobSeekerOptional.get();
            model.addAttribute("resumes", jobSeeker.getResumes());
            model.addAttribute("hasResumes", !jobSeeker.getResumes().isEmpty());
        } else {
            model.addAttribute("hasResumes", false);
        }
        return "allVacancyPage";
    }
}
