package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.services.CompanyService;
import by.baraznov.recruiting.services.JobSeekerService;
import by.baraznov.recruiting.services.VacancyService;
import by.baraznov.recruiting.services.impl.CurrentUserProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class StartController {

    private final CurrentUserProvider currentUserProvider;
    private final VacancyService vacancyService;
    private final JobSeekerService jobSeekerService;
    private final CompanyService companyService;

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

    @GetMapping("/job")
    public String mainJobSeeker(Model model) {
        model.addAttribute("vacancies", vacancyService.findRecommended());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        if (isAuthenticated) {
            Person person = currentUserProvider.getCurrentPerson().getPerson();
            Optional<JobSeeker> jobSeekerOptional = jobSeekerService.findByPerson(person);

            if (jobSeekerOptional.isPresent()) {
                JobSeeker jobSeeker = jobSeekerOptional.get();
                model.addAttribute("resumes", jobSeeker.getResumes());
                model.addAttribute("hasResumes", !jobSeeker.getResumes().isEmpty());
            } else {
                model.addAttribute("hasResumes", false);
            }
        } else {
            model.addAttribute("hasResumes", false);
        }
        return "jobPage";
    }

    @GetMapping("/company")
    public String mainCompany(Model model) {
        model.addAttribute("topCompanies", companyService.findTopCompany());
        return "companyPage";
    }
}
