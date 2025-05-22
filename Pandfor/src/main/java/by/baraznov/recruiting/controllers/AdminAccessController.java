package by.baraznov.recruiting.controllers;

import by.baraznov.recruiting.services.impl.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/secret-admin-access")
@AllArgsConstructor
public class AdminAccessController {

    private final String SECRET_TOKEN = "abc123xyz";

    private final PasswordEncoder passwordEncoder;
    private final RegistrationService registrationService;


    @GetMapping("/{token}")
    public ResponseEntity<String> accessAdminViaUrl(
            @PathVariable String token,
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam(defaultValue = "login") String type,
            HttpServletRequest request
    ) {
        if (!SECRET_TOKEN.equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid access token");
        }

        if ("register".equalsIgnoreCase(type)) {
            boolean created = registrationService.registerAdmin(login, password);
            if (created) {
                return ResponseEntity.ok("Admin registered successfully");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Admin already exists");
            }
        } else if ("login".equalsIgnoreCase(type)) {
            try {
                request.login(login, password);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, "/admin?login=" + URLEncoder.encode(login, StandardCharsets.UTF_8))
                        .build();
            } catch (ServletException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
            }
        }

        return ResponseEntity.badRequest().body("Unknown operation type");
    }


}

