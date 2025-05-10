package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.dto.CompanyDTO;
import by.baraznov.recruiting.mappers.CompanyMapper;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Employer;
import by.baraznov.recruiting.models.JobSeeker;
import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.models.PersonalInfo;
import by.baraznov.recruiting.models.Photo;
import by.baraznov.recruiting.services.CompanyService;
import by.baraznov.recruiting.services.EmployerService;
import by.baraznov.recruiting.services.PhotoService;
import by.baraznov.recruiting.services.VacancyService;
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
        model.addAttribute("vacancies", vacancyService.findAllVacancies(company));
        model.addAttribute("company", company);
        return "companyVacancyPage";
    }


    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitCompany(
            @RequestPart("companyData") CompanyDTO companyData,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {
        Company company = companyMapper.toEntity(companyData);
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
}
