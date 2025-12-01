package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Usuario;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;

@Controller
public class LoginController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final static String urlBase = "http://localhost:8080/";

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String Login(Usuario usuarioForm, Model model, HttpSession session) {

        try {
            // Headers JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Usuario> request = new HttpEntity<>(usuarioForm, headers);

            ResponseEntity<Result> response = restTemplate.exchange(
                    urlBase + "api/login",
                    HttpMethod.POST,
                    request,
                    Result.class
            );

            Result result = response.getBody();

            if (result != null && result.correct) {
                String token = result.Object.toString();
                session.setAttribute("token", token);

                return "redirect:/usuario";
            }
            else {
                model.addAttribute("error", 
                        result != null 
                        ? result.errorMessage 
                        : "Usuario o contraseña incorrectos");

                model.addAttribute("usuario", new Usuario()); 
                return "login";
            }

        } catch (Exception ex) {
            model.addAttribute("error", "No se pudo conectar al API");
            model.addAttribute("usuario", new Usuario()); 
            return "login";
        }
    }
}
