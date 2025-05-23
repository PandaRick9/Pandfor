package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.dto.VacancyCardDTO;
import by.baraznov.recruiting.dto.VacancyDTO;
import by.baraznov.recruiting.dto.resumePage.ResumeDto;
import by.baraznov.recruiting.dto.vacancyPage.VacancyDto;
import by.baraznov.recruiting.mappers.VacancyMapper;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Employer;
import by.baraznov.recruiting.models.JobCondition;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.models.Skill;
import by.baraznov.recruiting.models.Vacancy;
import by.baraznov.recruiting.models.VacancySkill;
import by.baraznov.recruiting.services.CompanyService;
import by.baraznov.recruiting.services.EmployerService;
import by.baraznov.recruiting.services.JobSeekerService;
import by.baraznov.recruiting.services.ResumeService;
import by.baraznov.recruiting.services.SkillService;
import by.baraznov.recruiting.services.VacancyService;
import by.baraznov.recruiting.services.impl.CurrentUserProvider;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vacancy")
@AllArgsConstructor
public class VacancyController {
    private final VacancyMapper vacancyMapper;
    private final SkillService skillService;
    private final VacancyService vacancyService;
    private final CurrentUserProvider currentUserProvider;
    private final EmployerService employerService;
    private final CompanyService companyService;
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


    @GetMapping
    public String allVacancy(Model model) {
        Person person = currentUserProvider.getCurrentPerson().getPerson();
        model.addAttribute("vacancies", vacancyService.findAll());
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

    @GetMapping("/{id}")
    public String vacancy(@PathVariable Integer id, Model model) {
        model.addAttribute("vacancy", vacancyService.getVacancyPageDetails(id));
        return "vacancyPage";
    }

    @GetMapping("/edit/{id}")
    public String editVacancyForm(@PathVariable Integer id, Model model) {
        return "editVacancyPage";
    }
    @PostMapping("/update/{id}")
    public String updateVacancy(
            @PathVariable Integer id,
            @ModelAttribute("resume") @Valid VacancyDto vacancyDto)  {
        vacancyService.updateVacancy(id, vacancyDto);
        return "redirect:/account/company";
    }




    @GetMapping("/new")
    public String newVacancy() {
        return "addVacancyPage";
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitVacancy(
            @RequestPart("vacancyData") VacancyDTO vacancyDTO) {
        Vacancy vacancy = vacancyMapper.toEntity(vacancyDTO);

        JobCondition jobCondition = vacancy.getJobCondition();
        jobCondition.setVacancy(vacancy);
        vacancy.setVacancySkills(saveSkills(vacancyDTO, vacancy));
        Person person = currentUserProvider.getCurrentPerson().getPerson();
        Employer employer = employerService.findByPerson(person).orElse(null);
        Company company = companyService.findByEmployers(employer).orElse(null);
        vacancy.setCompany(company);
        vacancy.setEmployer(employer);

        vacancyService.save(vacancy);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/resume")
                .build();
    }
    private List<VacancySkill> saveSkills(VacancyDTO vacancyDTO, Vacancy vacancy) {
        return vacancyDTO.getSkills().stream()
                .map(skillDTO -> {
                    Skill skill = skillService.findByName(skillDTO.getName())
                            .orElseGet(() -> {
                                Skill newSkill = new Skill();
                                newSkill.setName(skillDTO.getName());
                                return skillService.save(newSkill);
                            });

                    VacancySkill vacancySkill = vacancyMapper.toEntity(skillDTO);
                    vacancySkill.setSkill(skill);
                    vacancySkill.setVacancy(vacancy);
                    return vacancySkill;
                })
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public String deleteVacancy(@PathVariable Integer id) {
        vacancyService.delete(id);
        return "redirect:/account/company";
    }
}
