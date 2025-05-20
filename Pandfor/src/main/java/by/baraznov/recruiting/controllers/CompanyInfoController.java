package by.baraznov.recruiting.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aboutus")
@AllArgsConstructor
public class CompanyInfoController {
    @GetMapping
    public String about() {
        return "aboutUs";
    }
}
