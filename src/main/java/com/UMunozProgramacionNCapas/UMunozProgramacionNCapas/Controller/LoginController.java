package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;

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

    RestTemplate restTemplate = new RestTemplate();

    private final static String urlBase = "http://localhost:8080/";

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String Login(Usuario usuarioForm, Model model) {

        try {
            HttpEntity<Usuario> request = new HttpEntity<>(usuarioForm);

            ResponseEntity<Result> response = restTemplate.exchange(
                    "http://localhost:8080/api/login", 
                    HttpMethod.POST,
                    request,
                    Result.class);

            Result result = response.getBody();

            if (result != null && result.Correct) { 
                return "redirect:/usuario"; 
            } else {
                model.addAttribute("error", "Usuario o contraseña incorrectos");
                return "login";
            }

        } catch (Exception ex) {
            model.addAttribute("error", "No se pudo conectar al API");
            return "login";
        }
    }

}