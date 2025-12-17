package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

// Controlador para verificar la cuenta del usuario mediante el token enviado por correo
@Controller
public class VerificationController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String urlBase = "http://localhost:8080/";

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String tokenVerify, Model model) {
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                    urlBase + "api/usuario/verify?token=" + tokenVerify,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result>() {}
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("success", "Cuenta verificada, inicia sesión.");
            } else {
                model.addAttribute("error", "No se pudo verificar la cuenta.");
            }
        } catch (Exception ex) {
            model.addAttribute("error", "Token inválido o expirado.");
        }
        return "login";
    }
}