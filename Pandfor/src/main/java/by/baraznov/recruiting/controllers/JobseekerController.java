package by.baraznov.recruiting.controllers;


import by.baraznov.recruiting.dto.CompanyDTO;
import by.baraznov.recruiting.dto.JobSeekerDto;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Employer;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.models.Photo;
import by.baraznov.recruiting.services.JobSeekerService;
import by.baraznov.recruiting.services.PhotoService;
import by.baraznov.recruiting.services.impl.CurrentUserProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/jobseeker")
@AllArgsConstructor
public class JobseekerController {

    private final CurrentUserProvider currentUserProvider;
    private final JobSeekerService jobSeekerService;
    private final PhotoService photoService;


    @GetMapping("/new")
    public String newJobSeeker() {
        Person person = currentUserProvider.getCurrentPerson().getPerson();
        JobSeeker jobSeeker = jobSeekerService.findByPerson(person).orElse(null);
        if(jobSeeker == null || jobSeeker.getFirstName() == null) {
            return "addJobSeekerPage";
        }else{
            return "redirect:/resume/new";
        }
    }


    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createJobSeeker(
            @RequestPart("seekerData") String seekerDataJson,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile) {

        try {
            Integer userId = currentUserProvider.getCurrentPerson().getPerson().getUserId();
            JobSeekerDto seekerDto = new ObjectMapper().readValue(seekerDataJson, JobSeekerDto.class);

            JobSeeker jobSeeker = jobSeekerService.createJobSeeker(userId, seekerDto);
            savePhoto(photoFile, jobSeeker);
            jobSeekerService.save(jobSeeker);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "/resume/new")
                    .build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating profile: " + e.getMessage());
        }
    }
    private void savePhoto(MultipartFile photoFile, JobSeeker jobSeeker) throws IOException {
        if (photoFile != null && !photoFile.isEmpty()) {
            Photo photo = new Photo();
            photo.setFileName(photoFile.getOriginalFilename());
            photo.setContentType(photoFile.getContentType());
            photo.setData(photoFile.getBytes());
            jobSeeker.setPhoto(photo);
            photoService.save(photo);
        }
    }
}
