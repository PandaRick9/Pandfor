package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.dto.CompanyDTO;
import by.baraznov.recruiting.dto.CompanyEditProfileDto;
import by.baraznov.recruiting.dto.MatchJobConditionDTO;
import by.baraznov.recruiting.dto.MatchJobPreferenceDTO;
import by.baraznov.recruiting.dto.MatchPercentageDTO;
import by.baraznov.recruiting.dto.MatchResumeSkillDTO;
import by.baraznov.recruiting.dto.MatchVacancySkillDTO;
import by.baraznov.recruiting.dto.ReactionShortDTO;
import by.baraznov.recruiting.dto.resumePage.ResumeDto;
import by.baraznov.recruiting.mappers.CompanyMapper;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Employer;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.MatchWeights;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.models.PersonalInfo;
import by.baraznov.recruiting.models.Photo;
import by.baraznov.recruiting.services.CompanyService;
import by.baraznov.recruiting.services.EmployerService;
import by.baraznov.recruiting.services.MatchWeightsService;
import by.baraznov.recruiting.services.PhotoService;
import by.baraznov.recruiting.services.ReactionService;
import by.baraznov.recruiting.services.ResumeService;
import by.baraznov.recruiting.services.VacancyService;
import by.baraznov.recruiting.services.impl.CurrentUserProvider;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyMapper companyMapper;
    private final PhotoService photoService;
    private final CompanyService companyService;
    private final CurrentUserProvider currentUserProvider;
    private final EmployerService employerService;
    private final VacancyService vacancyService;
    private final ReactionService reactionService;
    private final MatchWeightsService matchWeightsService;

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

    @GetMapping("/edit/{id}")
    public String editCompanyForm(@PathVariable Integer id, Model model) {

        model.addAttribute("companyId", vacancyService.findOne(id).getCompany().getCompanyId());

        return "editCompanyPage";
    }
    @PostMapping("/update/{id}")
    public String updateResume(
            @PathVariable Integer id,
            @RequestParam("logo") MultipartFile logoFile,
            @ModelAttribute CompanyEditProfileDto companyEditProfileDto)  {
        companyService.updateCompanyProfile(id, logoFile, companyEditProfileDto);
        return "redirect:/account/company";
    }




    @GetMapping("/new")
    public String newCompany() {
        if(currentUserProvider.getCurrentPerson().getPerson().getEmployer() == null) {
            return "addCompanyPage";
        }else{
            return "redirect:/vacancy/new";
        }
    }
    @GetMapping("/vacancy")
    public String companyVacancies(Model model){
        Person person = currentUserProvider.getCurrentPerson().getPerson();
        Employer employer = employerService.findByPerson(person).orElse(null);
        Company company = Objects.requireNonNull(employer).getCompany();
        model.addAttribute("vacancies", vacancyService.findAllVacancies(company.getCompanyId()));
        model.addAttribute("company", company);
        return "companyVacancyPage";
    }
    @GetMapping("/vacancy/{id}")
    public String vacancyReactions(@PathVariable Integer id, Model model) {
        List<ReactionShortDTO> reactions = reactionService.findAllByVacancy(id);
        List<Integer> matchPercentages = calculateMatchPercentage(vacancyService.findAllMatchPercentage(id));
        Map<Integer, Integer> reactionToMatch = buildReactionToMatchMap(reactions, matchPercentages);

        sortReactions(reactions, reactionToMatch);
        List<Integer> sortedPercentages = extractSortedMatchPercentages(reactions, reactionToMatch);

        populateModel(model, id, reactions, sortedPercentages);

        return "reactionPage";
    }




    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitCompany(
            @RequestPart("companyData") CompanyDTO companyData,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {
        Company company = companyMapper.toEntity(companyData);
        company.setEmail(companyData.getEmail());
        company.setPhone(companyData.getPhone());
        savePhoto(photo, company);
        company = companyService.save(company);

        final Company savedCompany = company;
        Person person = currentUserProvider.getCurrentPerson().getPerson();
        Employer employer = employerService.findByPerson(person)
                .orElseGet(() -> {
                    Employer newEmployer = new Employer();
                    newEmployer.setPerson(person);
                    newEmployer.setCompany(savedCompany);
                    return employerService.save(newEmployer);
                });

        company.setEmployers(employer);

        companyService.save(company);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/vacancy/new")
                .build();
    }
    private void savePhoto(MultipartFile photoFile, Company company) throws IOException {
        if (photoFile != null && !photoFile.isEmpty()) {
            Photo photo = new Photo();
            photo.setFileName(photoFile.getOriginalFilename());
            photo.setContentType(photoFile.getContentType());
            photo.setData(photoFile.getBytes());
            company.setPhoto(photo);
            photoService.save(photo);
        }
    }




    private List<Integer> calculateMatchPercentage(List<MatchPercentageDTO> matchDTOs) {
        MatchWeights weights = matchWeightsService.getWeights();

        List<Integer> percentages = new ArrayList<>();

        for (MatchPercentageDTO dto : matchDTOs) {
            int skillMatch = calculateSkillMatch(dto.candidateSkills(), dto.requiredSkills());
            int conditionMatch = calculateConditionMatch(dto.jobPreference(), dto.jobCondition());
            int totalMatch = (int) (skillMatch * weights.getSkillWeight() +
                    conditionMatch * weights.getConditionWeight());
            percentages.add(totalMatch);
        }

        return percentages;
    }

    private int calculateSkillMatch(List<MatchResumeSkillDTO> candidateSkills, List<MatchVacancySkillDTO> requiredSkills) {
        if (requiredSkills.isEmpty()) return 100;
        long matchCount = requiredSkills.stream()
                .filter(req -> candidateSkills.stream()
                        .anyMatch(cand -> cand.name().equalsIgnoreCase(req.name())))
                .count();
        return (int) ((matchCount * 100.0) / requiredSkills.size());
    }

    private int calculateConditionMatch(MatchJobPreferenceDTO pref, MatchJobConditionDTO cond) {
        int totalChecks = 4;
        int matches = 0;

        if (pref.schedule() == cond.schedule()) matches++;
        if (pref.employmentType() == cond.employmentType()) matches++;
        if (pref.workFormat() == cond.workFormat()) matches++;
        if (pref.experienceYear() == cond.requiredExperienceYears()) matches++;
        matches += (pref.desiredSalary() == null || cond.salary() == null ||
                cond.salary() >= pref.desiredSalary() * 0.8) ? 1 : 0;
        totalChecks++;

        return (int) ((matches * 100.0) / totalChecks);
    }

    private int getStatusPriority(String status) {
        return switch (status) {
            case "Не просмотрено" -> 0;
            case "Приглашение" -> 1;
            case "Отказ" -> 2;
            default -> 3;
        };
    }




    private Map<Integer, Integer> buildReactionToMatchMap(List<ReactionShortDTO> reactions, List<Integer> percentages) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < reactions.size(); i++) {
            map.put(reactions.get(i).reactionId(), percentages.get(i));
        }
        return map;
    }

    private void sortReactions(List<ReactionShortDTO> reactions, Map<Integer, Integer> reactionToMatch) {
        reactions.sort(Comparator
                .comparingInt((ReactionShortDTO r) -> getStatusPriority(String.valueOf(r.status())))
                .thenComparing(r -> reactionToMatch.getOrDefault(r.reactionId(), 0), Comparator.reverseOrder())
        );
    }

    private List<Integer> extractSortedMatchPercentages(List<ReactionShortDTO> sortedReactions, Map<Integer, Integer> reactionToMatch) {
        List<Integer> sortedList = new ArrayList<>();
        for (ReactionShortDTO r : sortedReactions) {
            sortedList.add(reactionToMatch.getOrDefault(r.reactionId(), 0));
        }
        return sortedList;
    }

    private void populateModel(Model model, Integer vacancyId, List<ReactionShortDTO> reactions, List<Integer> matchPercentages) {
        model.addAttribute("reactions", reactions);
        model.addAttribute("matchPercentage", matchPercentages);
        model.addAttribute("title", vacancyService.findOne(vacancyId).getTitle());
        model.addAttribute("vacancyId", vacancyId);
    }
}
