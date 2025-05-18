package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.dto.CompanyProfileDto;
import by.baraznov.recruiting.dto.JobSeekerProfileDto;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.services.CompanyService;
import by.baraznov.recruiting.services.JobSeekerService;
import by.baraznov.recruiting.services.impl.CurrentUserProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final JobSeekerService jobSeekerService;
    private final CurrentUserProvider currentUserProvider;
    private final CompanyService companyService;

    @GetMapping("/job")
    public String getJobProfile(Model model) {
        Person person = currentUserProvider.getCurrentPerson().getPerson();
        Integer userId = person.getUserId();
        JobSeekerProfileDto profile = jobSeekerService.getProfile(userId);
        System.out.println(profile);
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
