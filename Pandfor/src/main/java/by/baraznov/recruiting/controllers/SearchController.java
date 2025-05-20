package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.services.JobSeekerService;
import by.baraznov.recruiting.services.ResumeService;
import by.baraznov.recruiting.services.VacancyService;
import by.baraznov.recruiting.services.impl.CurrentUserProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {
    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final CurrentUserProvider currentUserProvider;
    private final JobSeekerService jobSeekerService;

    @PostMapping("/resume")
    public String searchResume(@RequestParam(value = "query") String search, Model model){
        List<Resume> resumes = resumeService.searchResumes(search);
        List<Resume> validResumes = resumes.stream()
                .filter(r -> r.getJobSeeker() != null)
                .collect(Collectors.toList());

        model.addAttribute("resumes", validResumes);
        return "allResumePage";
    }
    @PostMapping("/vacancy")
    public String searchVacancy(@RequestParam(value = "query") String search, Model model){
        Person person = currentUserProvider.getCurrentPerson().getPerson();
        model.addAttribute("vacancies", vacancyService.searchVacancy(search));
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
}
