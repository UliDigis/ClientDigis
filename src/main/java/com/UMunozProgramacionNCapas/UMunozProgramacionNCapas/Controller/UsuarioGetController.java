
package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Direccion;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Pais;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Rol;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Usuario;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

//Controlador para todos los Get de Usuario
@Controller
@RequestMapping("/api/v1/usuario")
public class UsuarioGetController {
    
    private static final String urlBase="http://localhost:8080/";
    
    
//  Obtener una lista con todos los usuarios registrados
//  GetAll
    @GetMapping
    public String GetAll(Model model, HttpSession session) {

        RestTemplate restTemplate = new RestTemplate();

        String token = (String) session.getAttribute("JWT_TOKEN");

        if (token == null) {
            model.addAttribute("errorMessage", "Debe iniciar sesion para acceder");
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Result<List<Usuario>>> responseEntity = restTemplate.exchange(urlBase + "api/usuario",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Usuario>>>() {
            });

            if (responseEntity.getStatusCode().value() == 200) {
                Result result = responseEntity.getBody();
                model.addAttribute("usuarios", result.Object);
                model.addAttribute("token", token);
            } else {
                model.addAttribute("errorMessage", "fallo");
            }
        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error: " + ex.getMessage());
        }
        return "index";
    }

    
//    Obtener toda la informacion de un usuario por ID
//    GetById
    @GetMapping("/detail/")
    public String Detail(@RequestParam("IdUsuario") int IdUsuario, Model model, HttpSession session) {

        RestTemplate restTemplate = new RestTemplate();

        String token = (String) session.getAttribute("JWT_TOKEN");

        if (token == null) {
            model.addAttribute("errorMessage", "Debe iniciar sesion para acceder");
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(
                    urlBase + "api/usuario/?id=" + IdUsuario,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<Usuario>>() {
            }
            );

            ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(
                    urlBase + "api/rol",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Rol>>>() {
            }
            );

            ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(
                    urlBase + "api/pais",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Pais>>>() {
            }
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()
                    && responseEntityRol.getStatusCode().is2xxSuccessful()
                    && responseEntityPais.getStatusCode().is2xxSuccessful()) {

                Result<Usuario> resultUsuario = responseEntity.getBody();
                Result<List<Rol>> resultRol = responseEntityRol.getBody();
                Result<List<Pais>> resultPais = responseEntityPais.getBody();

                Usuario usuario = (resultUsuario != null) ? resultUsuario.Object : null;
                List<Rol> roles = (resultRol != null) ? resultRol.Object : null;
                List<Pais> paises = (resultPais != null) ? resultPais.Object : null;

                model.addAttribute("usuario", usuario);
                model.addAttribute("rol", roles);
                model.addAttribute("paises", paises);
                model.addAttribute("token", token);

                model.addAttribute("Direccion", new Direccion());

            } else {
                model.addAttribute("errorMessage", "Error al cargar los datos");
                model.addAttribute("Direccion", new Direccion());
            }

        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error: " + ex.getMessage());
            model.addAttribute("Direccion", new Direccion());
        }

        return "UsuarioDetail";
    }
    
//    Cierre de controlador
}
