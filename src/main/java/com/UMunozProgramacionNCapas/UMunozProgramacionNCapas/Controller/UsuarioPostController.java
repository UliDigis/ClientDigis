package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;

import java.util.Base64;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Direccion;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.EmailRequest;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Usuario;

import jakarta.servlet.http.HttpSession;

//Controlador para todos los Post de Usuario
@Controller
@RequestMapping("usuario")
public class UsuarioPostController {

    private static final String urlBase = "http://localhost:8080/";

    @PostMapping("/sendEmail")
    public String SendEmail(@RequestParam("email") String email,
            @RequestParam("asunto") String asunto,
            @RequestParam("mensaje") String mensaje,
            Model model,
            HttpSession session) {

        String token = (String) session.getAttribute("JWT_TOKEN");

        if (token == null) {
            return "redirect:/login";
        }

        RestTemplate restTemplate = new RestTemplate();

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.set("Content-Type", "application/json");

            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(email);
            emailRequest.setSubject(asunto);
            emailRequest.setBody(mensaje);

            HttpEntity<EmailRequest> entity = new HttpEntity<>(emailRequest, headers);

            ResponseEntity<Result> responseEntity = restTemplate.exchange(
                    urlBase + "api/email/send",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Result>() {
            });

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("successMessage", "Correo enviado");
            } else {
                model.addAttribute("errorMessage", "No se mando el correo");
            }

        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error: " + ex.getMessage());
        }
        return "redirect:/usaurio";
    }

    @PostMapping("/add")
    public String Add(@ModelAttribute Usuario usuario,
            @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
            Model model, HttpSession session) {

        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token == null) {
            return "redirect:/login";
        }

        RestTemplate restTemplate = new RestTemplate();

        try {
            if (imagenFile != null && !imagenFile.isEmpty()) {
                String nombre = imagenFile.getOriginalFilename();
                String ext = nombre.substring(nombre.lastIndexOf('.') + 1);
                if (!ext.equalsIgnoreCase("jpg") && !ext.equalsIgnoreCase("png")) {
                    model.addAttribute("Error", "Formato de imagen no válido (solo JPG y PNG)");
                    return "UsuarioForm";
                }
                String base64 = Base64.getEncoder().encodeToString(imagenFile.getBytes());
                usuario.setImagen(base64);
            }

            usuario.setStatus(true);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Usuario> entity = new HttpEntity<>(usuario, headers);

            ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(
                    urlBase + "api/usuario/add",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Result<Usuario>>() {
            });

            if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
                model.addAttribute("errorMessage", "No se pudo crear el usuario");
                return "UsuarioForm";
            }

            Usuario usuarioCreado = responseEntity.getBody().Object;
            if (usuarioCreado == null || usuarioCreado.getToken() == null || usuarioCreado.getToken().isEmpty()) {
                model.addAttribute("errorMessage", "No se recibió token de verificación");
                return "UsuarioForm";
            }

            String verifyUrl = "http://localhost:8081/verify?token=" + usuarioCreado.getToken();

            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(usuarioCreado.getEmail());
            emailRequest.setSubject("Verifica tu cuenta");
            emailRequest.setBody("Haz clic en el siguiente enlace para verificar tu cuenta: " + verifyUrl);

            HttpHeaders headersEmail = new HttpHeaders();
            headersEmail.setBearerAuth(token);
            headersEmail.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<EmailRequest> entityEmail = new HttpEntity<>(emailRequest, headersEmail);

            restTemplate.exchange(
                    urlBase + "api/email/send",
                    HttpMethod.POST,
                    entityEmail,
                    new ParameterizedTypeReference<Result>() {
            });

            return "redirect:/usuario";

        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "UsuarioForm";
        }
    }
    
    @PostMapping("/addDireccion")
    public String gestionarDireccion(
            @ModelAttribute Direccion direccion,
            @RequestParam int IdUsuario,
            @RequestParam(value = "IdDireccion", required = false) Integer IdDireccion,
            @RequestParam("IdColonia") int IdColonia,
            Model model,
            HttpSession session) {

        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token == null) {
            return "redirect:/login";
        }

        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            int idDir = (IdDireccion == null ? 0 : IdDireccion);

            java.util.Map<String, Object> payload = new java.util.LinkedHashMap<>();
            payload.put("idDireccion", idDir); 
            payload.put("calle", direccion.getCalle());
            payload.put("numeroExterior", direccion.getNumeroExterior());
            payload.put("numeroInterior", direccion.getNumeroInterior());

            java.util.Map<String, Object> colonia = new java.util.LinkedHashMap<>();
            colonia.put("idColonia", IdColonia);
            payload.put("colonia", colonia);

            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);

            String url;
            HttpMethod method;

            if (idDir == 0) {
                url = urlBase + "api/direccion/add/" + IdUsuario;
                method = HttpMethod.POST;
            } else {
                url = urlBase + "api/usuario/" + IdUsuario + "/direccion/" + idDir;
                method = HttpMethod.PUT;
            }

            ResponseEntity<Result> responseEntity = restTemplate.exchange(
                    url,
                    method,
                    entity,
                    new ParameterizedTypeReference<Result>() {
            }
            );

            if (!responseEntity.getStatusCode().is2xxSuccessful()
                    || responseEntity.getBody() == null
                    || !responseEntity.getBody().correct) {
                String msg = (responseEntity.getBody() != null ? responseEntity.getBody().errorMessage : "No se guardó la dirección");
                model.addAttribute("errorMessage", msg);
            }

        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error: " + ex.getMessage());
        }

        return "redirect:/usuario/detail/?IdUsuario=" + IdUsuario;
    }

    @PostMapping("update")
    public String Update(
            @ModelAttribute Usuario usuario,
            @RequestParam(value = "ImagenFile", required = false) org.springframework.web.multipart.MultipartFile imagenFile,
            HttpSession session) {
        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token == null) {
            return "redirect:/login";
        }

        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            if (imagenFile != null && !imagenFile.isEmpty()) {

                String contentType = imagenFile.getContentType();
                long size = imagenFile.getSize();
                if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                    return "redirect:/usuario/detail/?IdUsuario=" + usuario.getIdUsuario();
                }
                if (size > 2_000_000) {
                    return "redirect:/usuario/detail/?IdUsuario=" + usuario.getIdUsuario();
                }

                byte[] bytes = imagenFile.getBytes();
                String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
                usuario.setImagen(base64);

            } else {
                HttpEntity<Void> entityGet = new HttpEntity<>(headers);

                ResponseEntity<Result<Usuario>> responseGet = restTemplate.exchange(
                        urlBase + "api/usuario/?id=" + usuario.getIdUsuario(),
                        HttpMethod.GET,
                        entityGet,
                        new ParameterizedTypeReference<Result<Usuario>>() {
                });

                if (responseGet.getStatusCode().is2xxSuccessful()
                        && responseGet.getBody() != null
                        && responseGet.getBody().Object != null) {
                    Usuario actual = responseGet.getBody().Object;
                    usuario.setImagen(actual.getImagen());
                }
            }

            HttpEntity<Usuario> entityPut = new HttpEntity<>(usuario, headers);

            restTemplate.exchange(
                    urlBase + "api/usuario/update/" + usuario.getIdUsuario(),
                    HttpMethod.PUT,
                    entityPut,
                    new ParameterizedTypeReference<Result>() {
            });

        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
        }

        return "redirect:/usuario/detail/?IdUsuario=" + usuario.getIdUsuario();
    }

    @PostMapping("updateDireccion")
    public String UpdateDireccion(@ModelAttribute Direccion direccion, int IdUsuario, Model model,
            HttpSession session) {

        String token = (String) session.getAttribute("JWT_TOKEN");

        if (token == null) {
            return "redirect:/login";
        }

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Direccion> entity = new HttpEntity<>(direccion, headers);

            ResponseEntity<Result<List<Direccion>>> responseEntity = restTemplate.exchange(urlBase,
                    HttpMethod.PUT,
                    entity,
                    new ParameterizedTypeReference<Result<List<Direccion>>>() {
            });

            if (responseEntity.getStatusCode().value() == 200 || IdUsuario > 0) {

                result = responseEntity.getBody();
                model.addAttribute("direccion", result.Object);
            }

        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", result.errorMessage);
        }

        return ("redirect:/usuario/detail/?IdUsuario=" + IdUsuario);
    }

}
