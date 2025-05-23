package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.dto.ResumeDTO;
import by.baraznov.recruiting.dto.resumePage.ResumeDto;
import by.baraznov.recruiting.mappers.ResumeMapper;
import by.baraznov.recruiting.models.Education;
import by.baraznov.recruiting.models.JobPreference;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.models.PersonalInfo;
import by.baraznov.recruiting.models.Photo;
import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.models.ResumeSkill;
import by.baraznov.recruiting.models.Skill;
import by.baraznov.recruiting.services.EducationService;
import by.baraznov.recruiting.services.JobSeekerService;
import by.baraznov.recruiting.services.PhotoService;
import by.baraznov.recruiting.services.ResumeService;
import by.baraznov.recruiting.services.SkillService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/resume")
@AllArgsConstructor
public class ResumeController {

    private final ResumeMapper resumeMapper;
    private final ResumeService resumeService;
    private final SkillService skillService;
    private final EducationService educationService;
    private final CurrentUserProvider currentUserProvider;
    private final JobSeekerService jobSeekerService;
    private final PhotoService photoService;

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

    @GetMapping
    public String allResume(Model model) {
        List<Resume> resumes = resumeService.findAllActive();
        List<Resume> validResumes = resumes.stream()
                .filter(r -> r.getJobSeeker() != null)
                .collect(Collectors.toList());

        model.addAttribute("resumes", validResumes);
        return "allResumePage";
    }

    @GetMapping("/{id}")
    public String resumeView(@PathVariable Integer id,  Model model){
        ResumeDto resume = resumeService.getResumeById(id);
        model.addAttribute("fromVacancy", null);
        model.addAttribute("resume", resume);
        return "resumePage";
    }

    @GetMapping("/edit/{id}")
    public String editResumeForm(@PathVariable Integer id, Model model) {
        ResumeDto resume = resumeService.getResumeById(id);
        model.addAttribute("resume", resume);
        return "editResumePage";
    }
    @PostMapping("/update/{id}")
    public String updateResume(
            @PathVariable Integer id,
            @ModelAttribute("resume") @Valid ResumeDto resumeDto) throws IOException {
        resumeService.updateResume(id, resumeDto);
        return "redirect:/account/job";
    }


    @GetMapping("/new")
    public String addResume() {
        return "addResumePage";
    }

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitResume(
            @RequestPart("resumeData") ResumeDTO resumeRequest) throws IOException {

        Resume resume = resumeMapper.toEntity(resumeRequest);

        PersonalInfo info = resume.getPersonalInfo();
        resume.setPersonalInfo(info);
        info.setResume(resume);

        JobPreference pref = resume.getJobPreference();
        pref.setResume(resume);

        resume.setResumeSkills(saveSkills(resumeRequest,resume));


        Person person = currentUserProvider.getCurrentPerson().getPerson();

        JobSeeker jobSeeker = jobSeekerService.findByPerson(person)
                .orElseGet(() -> {
                    JobSeeker newSeeker = new JobSeeker();
                    newSeeker.setPerson(person);
                    return jobSeekerService.save(newSeeker);
                });

        resume.setJobSeeker(jobSeeker);
        resume.setPersonalInfo(info);
        resumeService.save(resume);

        List<Education> educationList = resumeMapper.toEducationList(resumeRequest.getEducationList());
        educationList.forEach(edu -> {
            edu.setPersonalInfo(info);
            educationService.save(edu);
        });
        info.setEducation(educationList);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/vacancy")
                .build();
    }
    private List<ResumeSkill> saveSkills(ResumeDTO resumeRequest, Resume resume) {
       return resumeRequest.getSkills().stream()
                .map(skillDTO -> {
                    Skill skill = skillService.findByName(skillDTO.getName())
                            .orElseGet(() -> {
                                Skill newSkill = new Skill();
                                newSkill.setName(skillDTO.getName());
                                System.out.println(skillDTO.getProficiencyLevel());
                                return skillService.save(newSkill);
                            });

                    ResumeSkill resumeSkill = resumeMapper.toEntity(skillDTO);
                    resumeSkill.setSkill(skill);
                    resumeSkill.setResume(resume);
                    return resumeSkill;
                })
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public String deleteResume(@PathVariable Integer id) {
        resumeService.delete(id);
        return "redirect:/account/job";
    }


}
