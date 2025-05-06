package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.dto.ResumeDTO;
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
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private final PhotoService photoService;
    private final SkillService skillService;
    private final EducationService educationService;
    private final CurrentUserProvider currentUserProvider;
    private final JobSeekerService jobSeekerService;


    @GetMapping
    public String allResume() {
        return "resumePage";
    }
    @GetMapping("/new")
    public String addResume() {
        return "addResumePage";
    }

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitResume(
            @RequestPart("resumeData") ResumeDTO resumeRequest,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile) throws IOException {

        Resume resume = resumeMapper.toEntity(resumeRequest);

        PersonalInfo info = resume.getPersonalInfo();
        resume.setPersonalInfo(info);
        info.setResume(resume);

        JobPreference pref = resume.getJobPreference();
        pref.setResume(resume);

        resume.setResumeSkills(saveSkills(resumeRequest,resume));

        savePhoto(photoFile,info);

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
                .header(HttpHeaders.LOCATION, "/resume")
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
    private void savePhoto(MultipartFile photoFile, PersonalInfo info) throws IOException {
        if (photoFile != null && !photoFile.isEmpty()) {
            Photo photo = new Photo();
            photo.setFileName(photoFile.getOriginalFilename());
            photo.setContentType(photoFile.getContentType());
            photo.setData(photoFile.getBytes());
            info.setPhoto(photo);
            photoService.save(photo);
        }
    }
}
