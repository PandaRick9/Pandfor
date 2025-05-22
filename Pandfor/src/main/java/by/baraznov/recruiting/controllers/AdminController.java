package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.dto.AdminStats;
import by.baraznov.recruiting.services.AdminStatsService;
import by.baraznov.recruiting.services.PeopleService;
import by.baraznov.recruiting.services.impl.PersonService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//GET /secret-admin-access/abc123xyz?login=admin&password=1234&type=register
//GET /secret-admin-access/abc123xyz?login=admin&password=1234&type=login
@Controller
@AllArgsConstructor
public class AdminController {
    private final AdminStatsService adminStatsService;
    private final PeopleService peopleService;


    @GetMapping("/admin")
    public String admin(@RequestParam(required = false) String login,
                        Model model,
                        HttpSession session) {
        if (login != null) {
            session.setAttribute("adminLogin", login);
        } else {
            login = (String) session.getAttribute("adminLogin");
            if (login == null) login = ""; // безопасное значение
        }

        model.addAttribute("login", login); // ← теперь всегда есть
        model.addAttribute("stats", adminStatsService.getStats());
        model.addAttribute("users", peopleService.getAllUsers(login));
        return "adminPage";
    }

    @PostMapping("/admin/user/{id}/toggle-block")
    public String toggleUser(@PathVariable Integer id,  @RequestParam String login) {
        peopleService.toggleActive(id);
        return "redirect:/admin?login=" + login;
    }
}
