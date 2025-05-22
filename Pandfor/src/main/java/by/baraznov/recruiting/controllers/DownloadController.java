package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.models.Attachment;
import by.baraznov.recruiting.services.AttachmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class DownloadController {

    private final AttachmentService attachmentService;

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadResume(@PathVariable Integer id) {
        Optional<Attachment> attachmentOptional = attachmentService.findByReaction_ResumeId(id);

        if (attachmentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Attachment attachment = attachmentOptional.get();
        String originalFileName = attachment.getFileName();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);


        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + encodeAsciiFallback(originalFileName) + "\"; filename*=UTF-8''" + encodeUtf8(originalFileName));

        return new ResponseEntity<>(attachment.getData(), headers, HttpStatus.OK);
    }
    private String encodeUtf8(String fileName) {
        try {
            return URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        } catch (Exception e) {
            return "file.pdf";
        }
    }

    private String encodeAsciiFallback(String fileName) {
        return fileName.replaceAll("[^\\x20-\\x7E]", "_");
    }


}
