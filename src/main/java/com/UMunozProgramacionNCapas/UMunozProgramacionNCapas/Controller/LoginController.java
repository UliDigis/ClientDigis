package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.LoginDTO;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String urlBase = "http://localhost:8080/";

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("usuario", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String Login(@ModelAttribute("usuario") LoginDTO loginForm, Model model, HttpSession session) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<LoginDTO> request = new HttpEntity<>(loginForm, headers);

            ResponseEntity<Result<LoginDTO>> response = restTemplate.exchange(
                    urlBase + "api/login",
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Result<LoginDTO>>() {
            }
            );

            Result<LoginDTO> result = response.getBody();

            if (result != null && result.correct && result.Object != null) {

                LoginDTO dataResponse = result.Object;

                session.setAttribute("JWT_TOKEN", dataResponse.getToken());
                session.setAttribute("SESSION_ROLE", dataResponse.getRole());
                session.setAttribute("SESSION_USER", dataResponse.getUserName());

                return "redirect:" + dataResponse.getRedirectUrl();
            }

            String msg = (result != null && result.errorMessage != null)
                    ? result.errorMessage : "Credenciales incorrectas";

            model.addAttribute("error", msg);
            model.addAttribute("usuario", loginForm);
            return "login";

        } catch (HttpClientErrorException ex) {
            model.addAttribute("error", "Usuario o contraseña inválidos.");
            model.addAttribute("usuario", loginForm);
            return "login";

        } catch (Exception ex) {
            model.addAttribute("error", "Error de conexión: " + ex.getMessage());
            model.addAttribute("usuario", loginForm);
            return "login";
        }
    }

}
