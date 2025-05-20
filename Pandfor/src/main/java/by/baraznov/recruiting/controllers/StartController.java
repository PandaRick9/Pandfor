package by.baraznov.recruiting.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StartController {

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
    public String mainJobSeeker() {
        return "jobPage";
    }

    @GetMapping("/company")
    public String mainCompany() {
        return "companyPage";
    }
}
