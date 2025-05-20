package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.dto.ReactionDTO;
import by.baraznov.recruiting.dto.resumePage.ResumeDto;
import by.baraznov.recruiting.models.Attachment;
import by.baraznov.recruiting.models.Reaction;
import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.services.ReactionService;
import by.baraznov.recruiting.services.ResumeService;
import by.baraznov.recruiting.services.VacancyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/reaction")
@AllArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;
    private final VacancyService vacancyService;
    private final ResumeService resumeService;

    @GetMapping("/{id}")
    public String reactionView(@PathVariable Integer id, @RequestParam(required = false) Integer fromVacancy, Model model){
        ResumeDto resume = resumeService.getResumeById(id);
        model.addAttribute("fromVacancy", fromVacancy);
        model.addAttribute("resume", resume);
        return "resumePage";
    }

    @PostMapping("/accept")
    public String reactionAccept(
            @RequestParam(required = false) Integer reactionId,
            @RequestParam(required = false) Integer fromVacancy) {
        reactionService.acceptReaction(reactionId);
        return "redirect:/company/vacancy/" + (fromVacancy != null ? fromVacancy : "");
    }

    @PostMapping("/reject")
    public String reactionReject(
            @RequestParam(required = false) Integer reactionId,
            @RequestParam(required = false) Integer fromVacancy) {
        reactionService.rejectReaction(reactionId);
        return "redirect:/company/vacancy/" + (fromVacancy != null ? fromVacancy : "");
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitReaction(
            @RequestPart("reactionData") ReactionDTO reactionDTO,
            @RequestPart("resumeFile") MultipartFile resumeFile) throws IOException {
        Reaction reaction = new Reaction();
        reaction.setVacancy(vacancyService.findOne(reactionDTO.getVacancyId()));
        reaction.setCoverLetter(reactionDTO.getCoverLetter());
        reaction.setResume(resumeService.findOne(reactionDTO.getSelectedResumeId()));

        if (resumeFile != null && !resumeFile.isEmpty()) {
            try {
                Attachment attachment = new Attachment();
                attachment.setFileName(resumeFile.getOriginalFilename());
                attachment.setData(resumeFile.getBytes());
                attachment.setReaction(reaction);

                reaction.setAttachment(attachment);

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ошибка при сохранении файла: " + e.getMessage());
            }
        }

        reactionService.save(reaction);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/vacancy")
                .build();
    }
}
