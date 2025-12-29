package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;

import java.util.Base64;

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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Direccion;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.EmailRequest;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Usuario;

import jakarta.servlet.http.HttpSession;

// Controlador para todos los Post de Usuario
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
            headers.setContentType(MediaType.APPLICATION_JSON);

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

            if (responseEntity.getStatusCode().is2xxSuccessful()
                    && responseEntity.getBody() != null
                    && responseEntity.getBody().correct) {
                model.addAttribute("successMessage", "Correo enviado");
            } else {
                model.addAttribute("errorMessage", "No se mandó el correo");
            }

        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            return "redirect:/login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error: " + ex.getMessage());
        }

        return "redirect:/usuario";
    }

    @PostMapping("/add")
    public String Add(@ModelAttribute Usuario usuario,
            @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token == null) {
            redirectAttributes.addFlashAttribute("swalIcon", "warning");
            redirectAttributes.addFlashAttribute("swalTitle", "Sesión expirada");
            redirectAttributes.addFlashAttribute("swalText", "Vuelve a iniciar sesión.");
            return "redirect:/login";
        }

        RestTemplate restTemplate = new RestTemplate();

        try {
            if (imagenFile != null && !imagenFile.isEmpty()) {
                String contentType = imagenFile.getContentType();
                if (contentType == null
                        || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                    model.addAttribute("errorMessage", "Formato de imagen no válido (solo JPG y PNG)");
                    return "UsuarioForm";
                }
                String base64 = Base64.getEncoder().encodeToString(imagenFile.getBytes());
                usuario.setImagen(base64);
            }

            usuario.setStatus(true);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Usuario> entity = new HttpEntity<>(usuario, headers);

            ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(
                    urlBase + "api/usuario/add",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Result<Usuario>>() {
                    });

            Result<Usuario> body = responseEntity.getBody();

            if (!responseEntity.getStatusCode().is2xxSuccessful() || body == null || !body.correct) {
                String msg = (body != null && body.errorMessage != null && !body.errorMessage.isBlank())
                        ? body.errorMessage
                        : "No se pudo crear el usuario";
                model.addAttribute("errorMessage", msg);
                return "UsuarioForm";
            }

            Usuario usuarioCreado = body.Object;
            if (usuarioCreado == null) {
                model.addAttribute("errorMessage", "No se pudo crear el usuario (respuesta vacía).");
                return "UsuarioForm";
            }

            // Intentar envío de correo solo si hay token
            if (usuarioCreado.getToken() != null && !usuarioCreado.getToken().isBlank()) {
                String verifyUrl = "http://localhost:8081/verify?token=" + usuarioCreado.getToken();

                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setTo(usuarioCreado.getEmail());
                emailRequest.setSubject("Verifica tu cuenta");
                emailRequest.setBody("Haz clic en el siguiente enlace para verificar tu cuenta: " + verifyUrl);

                HttpHeaders headersEmail = new HttpHeaders();
                headersEmail.setBearerAuth(token);
                headersEmail.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<EmailRequest> entityEmail = new HttpEntity<>(emailRequest, headersEmail);

                try {
                    restTemplate.exchange(
                            urlBase + "api/email/send",
                            HttpMethod.POST,
                            entityEmail,
                            new ParameterizedTypeReference<Result>() {
                            });
                } catch (Exception emailEx) {
                    // Usuario creado, correo falló
                    redirectAttributes.addFlashAttribute("swalIcon", "warning");
                    redirectAttributes.addFlashAttribute("swalTitle", "Usuario creado");
                    redirectAttributes.addFlashAttribute("swalText",
                            "Se creó el usuario, pero falló el envío del correo.");
                    return "redirect:/usuario";
                }
            }

            redirectAttributes.addFlashAttribute("swalIcon", "success");
            redirectAttributes.addFlashAttribute("swalTitle", "Registrado");
            redirectAttributes.addFlashAttribute("swalText", "Usuario creado correctamente.");
            return "redirect:/usuario";

        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            redirectAttributes.addFlashAttribute("swalIcon", "warning");
            redirectAttributes.addFlashAttribute("swalTitle", "Sesión expirada");
            redirectAttributes.addFlashAttribute("swalText", "Vuelve a iniciar sesión.");
            return "redirect:/login";

        } catch (HttpStatusCodeException ex) {
            String apiMsg = ex.getResponseBodyAsString();
            model.addAttribute("errorMessage", (apiMsg != null && !apiMsg.isBlank())
                    ? apiMsg
                    : ("Error al crear usuario. Status: " + ex.getStatusCode()));
            return "UsuarioForm";

        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error: " + ex.getMessage());
            return "UsuarioForm";
        }
    }
   @PostMapping("/addDireccion")
    public String gestionarDireccion(@ModelAttribute Direccion direccion,
            @RequestParam int IdUsuario,
            @RequestParam(value = "IdDireccion", required = false) Integer IdDireccion,
            @RequestParam("IdColonia") int IdColonia,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token == null) {
            redirectAttributes.addFlashAttribute("swalIcon", "warning");
            redirectAttributes.addFlashAttribute("swalTitle", "Sesión expirada");
            redirectAttributes.addFlashAttribute("swalText", "Vuelve a iniciar sesión.");
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
                url = urlBase + "api/direccion/add/" + IdUsuario; // tu endpoint ADD
                method = HttpMethod.POST;
            } else {
                url = urlBase + "api/usuario/" + IdUsuario + "/direccion/" + idDir; // tu endpoint UPDATE
                method = HttpMethod.PUT;
            }

            ResponseEntity<Result> responseEntity = restTemplate.exchange(
                    url,
                    method,
                    entity,
                    new ParameterizedTypeReference<Result>() {
                    });

            Result body = responseEntity.getBody();

            if (!responseEntity.getStatusCode().is2xxSuccessful() || body == null || !body.correct) {
                String msg = (body != null && body.errorMessage != null && !body.errorMessage.isBlank())
                        ? body.errorMessage
                        : "No se guardó la dirección";

                redirectAttributes.addFlashAttribute("swalIcon", "error");
                redirectAttributes.addFlashAttribute("swalTitle", "Error");
                redirectAttributes.addFlashAttribute("swalText", msg);
                return "redirect:/usuario/detail/?IdUsuario=" + IdUsuario;
            }

            redirectAttributes.addFlashAttribute("swalIcon", "success");
            redirectAttributes.addFlashAttribute("swalTitle", "Listo");
            redirectAttributes.addFlashAttribute("swalText",
                    (idDir == 0 ? "Dirección agregada correctamente" : "Dirección actualizada correctamente"));

        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            redirectAttributes.addFlashAttribute("swalIcon", "warning");
            redirectAttributes.addFlashAttribute("swalTitle", "Sesión expirada");
            redirectAttributes.addFlashAttribute("swalText", "Vuelve a iniciar sesión.");
            return "redirect:/login";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("swalIcon", "error");
            redirectAttributes.addFlashAttribute("swalTitle", "Error");
            redirectAttributes.addFlashAttribute("swalText", "Error: " + ex.getMessage());
        }

        return "redirect:/usuario/detail/?IdUsuario=" + IdUsuario;
    }

    @PostMapping("update")
    public String Update(@ModelAttribute Usuario usuario,
            @RequestParam(value = "ImagenFile", required = false) MultipartFile imagenFile,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token == null) {
            redirectAttributes.addFlashAttribute("swalIcon", "warning");
            redirectAttributes.addFlashAttribute("swalTitle", "Sesión expirada");
            redirectAttributes.addFlashAttribute("swalText", "Vuelve a iniciar sesión.");
            return "redirect:/login";
        }

        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            if (imagenFile != null && !imagenFile.isEmpty()) {

                String contentType = imagenFile.getContentType();
                long size = imagenFile.getSize();

                if (contentType == null
                        || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                    redirectAttributes.addFlashAttribute("swalIcon", "warning");
                    redirectAttributes.addFlashAttribute("swalTitle", "Imagen inválida");
                    redirectAttributes.addFlashAttribute("swalText", "Solo se permite JPG o PNG.");
                    return "redirect:/usuario/detail/?IdUsuario=" + usuario.getIdUsuario();
                }

                if (size > 2_000_000) {
                    redirectAttributes.addFlashAttribute("swalIcon", "warning");
                    redirectAttributes.addFlashAttribute("swalTitle", "Imagen grande");
                    redirectAttributes.addFlashAttribute("swalText", "Máximo 2MB.");
                    return "redirect:/usuario/detail/?IdUsuario=" + usuario.getIdUsuario();
                }

                byte[] bytes = imagenFile.getBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);
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

            ResponseEntity<Result> responsePut = restTemplate.exchange(
                    urlBase + "api/usuario/update/" + usuario.getIdUsuario(),
                    HttpMethod.PUT,
                    entityPut,
                    new ParameterizedTypeReference<Result>() {
                    });

            Result body = responsePut.getBody();

            if (!responsePut.getStatusCode().is2xxSuccessful() || body == null || !body.correct) {
                String msg = (body != null && body.errorMessage != null && !body.errorMessage.isBlank())
                        ? body.errorMessage
                        : "No se pudo actualizar el usuario";

                redirectAttributes.addFlashAttribute("swalIcon", "error");
                redirectAttributes.addFlashAttribute("swalTitle", "Error");
                redirectAttributes.addFlashAttribute("swalText", msg);
                return "redirect:/usuario/detail/?IdUsuario=" + usuario.getIdUsuario();
            }

            redirectAttributes.addFlashAttribute("swalIcon", "success");
            redirectAttributes.addFlashAttribute("swalTitle", "Actualizado");
            redirectAttributes.addFlashAttribute("swalText", "Usuario actualizado correctamente.");

        } catch (HttpClientErrorException.Unauthorized ex) {
            session.invalidate();
            redirectAttributes.addFlashAttribute("swalIcon", "warning");
            redirectAttributes.addFlashAttribute("swalTitle", "Sesión expirada");
            redirectAttributes.addFlashAttribute("swalText", "Vuelve a iniciar sesión.");
            return "redirect:/login";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("swalIcon", "error");
            redirectAttributes.addFlashAttribute("swalTitle", "Error");
            redirectAttributes.addFlashAttribute("swalText", "Error: " + ex.getMessage());
        }

        return "redirect:/usuario/detail/?IdUsuario=" + usuario.getIdUsuario();
    }
}
