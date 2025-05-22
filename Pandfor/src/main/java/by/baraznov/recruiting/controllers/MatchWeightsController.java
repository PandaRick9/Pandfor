package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.services.MatchWeightsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MatchWeightsController {

    private final MatchWeightsService matchWeightsService;


    @PostMapping("/admin/weights")
    public String saveWeights(@RequestParam double skillWeight,
                              @RequestParam double conditionWeight,
                              @RequestParam String login) {
        matchWeightsService.updateWeights(skillWeight, conditionWeight);
        return "redirect:/admin?login=" + login;
    }
}
