package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.services.impl.RegistrationService;
import by.baraznov.recruiting.util.PasswordValidator;
import by.baraznov.recruiting.util.PersonValidator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final PersonValidator personValidator;
    private final PasswordValidator passwordValidator;
    private final RegistrationService registrationService;


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

    @GetMapping("/login")
    public String loginPage(){
        return "signInPage";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "signUpPage";
    }


    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        passwordValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "signUpPage";
        }
        registrationService.register(person, person.getRole().equals("job_seeker"));
        return "redirect:/auth/login";
    }
}
