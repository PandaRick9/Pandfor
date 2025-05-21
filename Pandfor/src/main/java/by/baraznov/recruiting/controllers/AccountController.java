package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.dto.CompanyProfileDto;
import by.baraznov.recruiting.dto.JobSeekerProfileDto;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.services.CompanyService;
import by.baraznov.recruiting.services.JobSeekerService;
import by.baraznov.recruiting.services.impl.CurrentUserProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final JobSeekerService jobSeekerService;
    private final CurrentUserProvider currentUserProvider;
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
    public String getJobProfile(Model model) {
        Person person = currentUserProvider.getCurrentPerson().getPerson();
        Integer userId = person.getUserId();
        JobSeekerProfileDto profile = jobSeekerService.getProfile(userId);
        model.addAttribute("profile", profile);
        return "accountJobPage";
    }

    @GetMapping("/company")
    public String getCompanyProfile(Model model) {
        Person person = currentUserProvider.getCurrentPerson().getPerson();
        Integer userId = person.getUserId();
        CompanyProfileDto profile = companyService.getProfile(userId);
        model.addAttribute("profile", profile);
        return "accountCompanyPage";
    }
}
