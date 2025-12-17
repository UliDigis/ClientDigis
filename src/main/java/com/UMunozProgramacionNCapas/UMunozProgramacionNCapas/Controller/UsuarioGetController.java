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
import org.springframework.web.util.UriComponentsBuilder;

//Controlador para todos los Get de Usuario
@Controller
@RequestMapping("usuario")
public class UsuarioGetController {

    private static final String urlBase = "http://localhost:8080/";

    // Obtener una lista con todos los usuarios registrados
    // GetAll
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
            // Usuarios
            ResponseEntity<Result<List<Usuario>>> responseEntity = restTemplate.exchange(
                    urlBase + "api/usuario",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Usuario>>>() {
                    });
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Result<List<Usuario>> result = responseEntity.getBody();
                model.addAttribute("usuarios", result != null ? result.Object : null);
                model.addAttribute("token", token);
            } else {
                model.addAttribute("errorMessage", "fallo");
            }

            // Roles (para el select de filtros)
            ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(
                    urlBase + "api/rol",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Rol>>>() {
                    });
            if (responseEntityRol.getStatusCode().is2xxSuccessful()) {
                Result<List<Rol>> resultRol = responseEntityRol.getBody();
                model.addAttribute("roles", resultRol != null ? resultRol.Object : null);
            }

        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error: " + ex.getMessage());
        }
        return "index";
    }

    // Obtener toda la informacion de un usuario por ID
    // GetById
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
                    });

            ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(
                    urlBase + "api/rol",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Rol>>>() {
                    });

            ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(
                    urlBase + "api/pais",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Pais>>>() {
                    });

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

    // Mostrar el formulario para registrar un nuevo usuario
    // Add
    @GetMapping("/add")
    public String Add(Model model, HttpSession session) {

        String token = (String) session.getAttribute("JWT_TOKEN");

        if (token == null) {
            return "redirect:/login";
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        Result result = new Result();

        try {

            ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(urlBase + "api/pais",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Pais>>>() {
                    });
            if (responseEntityPais.getStatusCode().value() == 200) {
                result = responseEntityPais.getBody();
                model.addAttribute("paises", result.Object);
                model.addAttribute("token", token);
            }
            ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(urlBase + "api/rol",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Rol>>>() {
                    });

            if (responseEntityRol.getStatusCode().value() == 200) {
                result = responseEntityRol.getBody();
                model.addAttribute("roles", result.Object);
            }
            Usuario usuario = new Usuario();
            model.addAttribute("usuario", usuario);
        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
            result.correct = false;
        }

        return "usuarioForm";
    }

    // Eliminar un usuario registrado por ID
    // Delete
    @GetMapping("delete")
    public String Delete(@RequestParam("IdUsuario") int IdUsuario, Model model, HttpSession session) {

        String token = (String) session.getAttribute("JWT_TOKEN");

        if (token == null) {
            return "redirect:/login";
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                    urlBase + "api/usuario/delete?IdUsuario=" + IdUsuario,
                    HttpMethod.DELETE,
                    entity,
                    Void.class);
        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
            model.addAttribute("error", "Error: " + ex.getMessage());
        }

        return "redirect:/usuario";
    }

    // Realiza una búsqueda de usuarios por filtros opcionales
    @GetMapping("/search")
    public String searchUsuarios(Model model,
            HttpSession session,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellidoPaterno,
            @RequestParam(required = false) String apellidoMaterno,
            @RequestParam(required = false) Integer idRol,
            @RequestParam(required = false) Boolean status) {

        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token == null)
            return "redirect:/login";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            // Normalizar entradas para evitar espacios al inicio/fin en filtros
            nombre = (nombre != null) ? nombre.trim() : null;
            apellidoPaterno = (apellidoPaterno != null) ? apellidoPaterno.trim() : null;
            apellidoMaterno = (apellidoMaterno != null) ? apellidoMaterno.trim() : null;

            boolean soloRol = (idRol != null && idRol > 0)
                    && (nombre == null || nombre.isBlank())
                    && (apellidoPaterno == null || apellidoPaterno.isBlank())
                    && (apellidoMaterno == null || apellidoMaterno.isBlank())
                    && status == null;

            ResponseEntity<Result<List<Usuario>>> resp;
            if (soloRol) {
                // El endpoint de búsqueda no está devolviendo resultados solo por rol,
                // así que pedimos todo y filtramos localmente.
                resp = restTemplate.exchange(
                        urlBase + "api/usuario",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<Result<List<Usuario>>>() {
                        });
            } else {
                var builder = UriComponentsBuilder.fromUriString(urlBase + "api/usuario/search");
                if (nombre != null && !nombre.isBlank()) {
                    builder.queryParam("nombre", nombre);
                }
                if (apellidoPaterno != null && !apellidoPaterno.isBlank()) {
                    builder.queryParam("apellidoPaterno", apellidoPaterno);
                }
                if (apellidoMaterno != null && !apellidoMaterno.isBlank()) {
                    builder.queryParam("apellidoMaterno", apellidoMaterno);
                }
                if (idRol != null && idRol > 0) {
                    builder.queryParam("idRol", idRol);
                }
                if (status != null) {
                    builder.queryParam("status", status);
                }

                resp = restTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<Result<List<Usuario>>>() {
                        });
            }

            if (resp.getStatusCode().is2xxSuccessful()) {
                Result<List<Usuario>> r = resp.getBody();
                List<Usuario> lista = (r != null && r.Object != null) ? r.Object : java.util.Collections.emptyList();
                if (idRol != null && idRol > 0) {
                    List<Usuario> filtrados = new java.util.ArrayList<>();
                    for (Usuario u : lista) {
                        if (u != null && u.getRol() != null && u.getRol().getIdRol() == idRol.intValue()) {
                            filtrados.add(u);
                        }
                    }
                    model.addAttribute("usuarios", filtrados);
                } else {
                    model.addAttribute("usuarios", lista);
                }
            } else {
                model.addAttribute("errorMessage", "No se pudieron obtener usuarios");
            }

            // roles para el select (cargar aunque falle la búsqueda)
            ResponseEntity<Result<List<Rol>>> respRol = restTemplate.exchange(
                    urlBase + "api/rol",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Rol>>>() {
                    });
            if (respRol.getStatusCode().is2xxSuccessful()) {
                Result<List<Rol>> rRol = respRol.getBody();
                model.addAttribute("roles", rRol != null ? rRol.Object : null);
            } else {
                model.addAttribute("errorMessage", "No se pudieron cargar los roles");
            }

            model.addAttribute("token", token);
        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error: " + ex.getMessage());
        }
        return "index";
    }

}// Cierre de controlador
