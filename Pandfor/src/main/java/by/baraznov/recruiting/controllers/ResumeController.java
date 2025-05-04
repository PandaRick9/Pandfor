package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.dto.ResumeDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
@Controller
@RequestMapping("/resume")
public class ResumeController {


    @GetMapping("/new")
    public String addResume() {
        return "addResumePage";
    }

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> submitResume(
            @RequestPart("resumeData") ResumeDTO resumeRequest,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile) {

        System.out.println("Получено резюме:");
        System.out.println(resumeRequest);

        if (photoFile != null && !photoFile.isEmpty()) {
            System.out.println("Фото: " + photoFile.getOriginalFilename() +
                    " (" + photoFile.getSize() + " байт)");
            // Здесь можно сохранить фото, например, в БД или на диск
        }

        // Здесь — логика сохранения resumeRequest + фото

        return ResponseEntity.ok("Резюме успешно сохранено");
    }
}
