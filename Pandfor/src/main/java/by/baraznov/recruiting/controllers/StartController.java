package by.baraznov.recruiting.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StartController {

    @GetMapping("/job")
    public String mainJobSeeker() {
        return "jobPage";
    }

    @GetMapping("/company")
    public String mainCompany() {
        return "companyPage";
    }
}
