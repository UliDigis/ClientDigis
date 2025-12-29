// package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;
//
// import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Colonia;
// import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Direccion;
// import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.EmailRequest;
// import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Municipio;
// import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Estado;
// import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Pais;
// import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
// import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Rol;
// import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Usuario;
// import jakarta.servlet.http.HttpSession;
// import jakarta.validation.constraints.Email;
//
// import java.util.Base64;
// import java.util.List;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.client.HttpClientErrorException;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.multipart.MultipartFile;
//
// @Controller
// @RequestMapping("usuario")
// public class UsuarioController {
//
//     private static final String urlBase = "http://localhost:8080/";
//
//     @GetMapping
//     public String GetAll(Model model, HttpSession session) {
//
//         RestTemplate restTemplate = new RestTemplate();
//
//         String token = (String) session.getAttribute("JWT_TOKEN");
//
//         if (token == null) {
//             model.addAttribute("errorMessage", "Debe iniciar sesion para acceder");
//             return "redirect:/login";
//         }
//
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);
//         HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//         try {
//             ResponseEntity<Result<List<Usuario>>> responseEntity = restTemplate.exchange(urlBase + "api/usuario",
//                     HttpMethod.GET,
//                     entity,
//                     new ParameterizedTypeReference<Result<List<Usuario>>>() {
//             });
//
//             if (responseEntity.getStatusCode().value() == 200) {
//                 Result result = responseEntity.getBody();
//                 model.addAttribute("usuarios", result.Object);
//                 model.addAttribute("token", token);
//             } else {
//                 model.addAttribute("errorMessage", "fallo");
//             }
//         } catch (HttpClientErrorException.Unauthorized ex) {
//             session.invalidate();
//             return "redirect:/login";
//         } catch (Exception ex) {
//             model.addAttribute("errorMessage", "Error: " + ex.getMessage());
//         }
//         return "index";
//     }
//
//     @GetMapping("/detail/")
//     public String Detail(@RequestParam("IdUsuario") int IdUsuario, Model model, HttpSession session) {
//
//         RestTemplate restTemplate = new RestTemplate();
//
//         String token = (String) session.getAttribute("JWT_TOKEN");
//
//         if (token == null) {
//             model.addAttribute("errorMessage", "Debe iniciar sesion para acceder");
//             return "redirect:/login";
//         }
//
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);
//         HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//         try {
//             ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(
//                     urlBase + "api/usuario/?id=" + IdUsuario,
//                     HttpMethod.GET,
//                     entity,
//                     new ParameterizedTypeReference<Result<Usuario>>() {
//             }
//             );
//
//             ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(
//                     urlBase + "api/rol",
//                     HttpMethod.GET,
//                     entity,
//                     new ParameterizedTypeReference<Result<List<Rol>>>() {
//             }
//             );
//
//             ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(
//                     urlBase + "api/pais",
//                     HttpMethod.GET,
//                     entity,
//                     new ParameterizedTypeReference<Result<List<Pais>>>() {
//             }
//             );
//
//             if (responseEntity.getStatusCode().is2xxSuccessful()
//                     && responseEntityRol.getStatusCode().is2xxSuccessful()
//                     && responseEntityPais.getStatusCode().is2xxSuccessful()) {
//
//                 Result<Usuario> resultUsuario = responseEntity.getBody();
//                 Result<List<Rol>> resultRol = responseEntityRol.getBody();
//                 Result<List<Pais>> resultPais = responseEntityPais.getBody();
//
//                 Usuario usuario = (resultUsuario != null) ? resultUsuario.Object : null;
//                 List<Rol> roles = (resultRol != null) ? resultRol.Object : null;
//                 List<Pais> paises = (resultPais != null) ? resultPais.Object : null;
//
//                 model.addAttribute("usuario", usuario);
//                 model.addAttribute("rol", roles);
//                 model.addAttribute("paises", paises);
//                 model.addAttribute("token", token);
//
//                 model.addAttribute("Direccion", new Direccion());
//
//             } else {
//                 model.addAttribute("errorMessage", "Error al cargar los datos");
//                 model.addAttribute("Direccion", new Direccion());
//             }
//
//         } catch (HttpClientErrorException.Unauthorized ex) {
//             session.invalidate();
//             return "redirect:/login";
//         } catch (Exception ex) {
//             model.addAttribute("errorMessage", "Error: " + ex.getMessage());
//             model.addAttribute("Direccion", new Direccion());
//         }
//
//         return "UsuarioDetail";
//     }
//
//     @GetMapping("/getDireccion")
//     @ResponseBody
//     public Direccion getDireccion(@RequestParam("IdDireccion") int IdDireccion, HttpSession session) {
//         RestTemplate restTemplate = new RestTemplate();
//
//         String token = (String) session.getAttribute("JWT_TOKEN");
//
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);
//         HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//         try {
//             ResponseEntity<Result<Direccion>> response = restTemplate.exchange(
//                     urlBase + "api/direccion/" + IdDireccion,
//                     HttpMethod.GET,
//                     entity,
//                     new ParameterizedTypeReference<Result<Direccion>>() {
//             });
//
//             if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//                 return response.getBody().Object;
//             }
//         } catch (Exception ex) {
//
//         }
//         return null;
//     }
//
//     @GetMapping("/add")
//     public String Add(Model model, HttpSession session) {
//
//         String token = (String) session.getAttribute("JWT_TOKEN");
//
//         if (token == null) {
//             return "redirect:/login";
//         }
//
//         RestTemplate restTemplate = new RestTemplate();
//
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);
//         HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//         Result result = new Result();
//
//         try {
//
//             ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(urlBase + "api/pais",
//                     HttpMethod.GET,
//                     entity,
//                     new ParameterizedTypeReference<Result<List<Pais>>>() {
//             });
//             if (responseEntityPais.getStatusCode().value() == 200) {
//                 result = responseEntityPais.getBody();
//                 model.addAttribute("paises", result.Object);
//                 model.addAttribute("token", token);
//             }
//             ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(urlBase + "api/rol",
//                     HttpMethod.GET,
//                     entity,
//                     new ParameterizedTypeReference<Result<List<Rol>>>() {
//             });
//
//             if (responseEntityRol.getStatusCode().value() == 200) {
//                 result = responseEntityRol.getBody();
//                 model.addAttribute("roles", result.Object);
//             }
//             Usuario usuario = new Usuario();
//             model.addAttribute("usuario", usuario);
//         } catch (HttpClientErrorException.Unauthorized ex) {
//             session.invalidate();
//             return "redirect:/login";
//         } catch (Exception ex) {
//             result.correct = false;
//         }
//
//         return "usuarioForm";
//     }
//
//     @PostMapping("/sendEmail")
//     public String SendEmail(@RequestParam("email") String email,
//             @RequestParam("asunto") String asunto,
//             @RequestParam("mensaje") String mensaje,
//             Model model,
//             HttpSession session) {
//
//         String token = (String) session.getAttribute("JWT_TOKEN");
//
//         if (token == null) {
//             return "redirect:/login";
//         }
//
//         RestTemplate restTemplate = new RestTemplate();
//
//         try {
//
//             HttpHeaders headers = new HttpHeaders();
//             headers.setBearerAuth(token);
//             headers.set("Content-Type", "application/json");
//
//             EmailRequest emailRequest = new EmailRequest();
//             emailRequest.setTo(email);
//             emailRequest.setSubject(asunto);
//             emailRequest.setBody(mensaje);
//
//             HttpEntity<EmailRequest> entity = new HttpEntity<>(emailRequest, headers);
//
//             ResponseEntity<Result> responseEntity = restTemplate.exchange(
//                     urlBase + "api/email/send",
//                     HttpMethod.POST,
//                     entity,
//                     new ParameterizedTypeReference<Result>() {
//             });
//
//             if (responseEntity.getStatusCode().is2xxSuccessful()) {
//                 model.addAttribute("successMessage", "Correo enviado");
//             } else {
//                 model.addAttribute("errorMessage", "No se mando el correo");
//             }
//
//         } catch (HttpClientErrorException.Unauthorized ex) {
//             session.invalidate();
//             return "redirect:/login";
//         } catch (Exception ex) {
//             model.addAttribute("errorMessage", "Error: " + ex.getMessage());
//         }
//         return "redirect:/usaurio";
//     }
//
//     @PostMapping("/add")
//     public String Add(@ModelAttribute Usuario usuario,
//             @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
//             Model model, HttpSession session) {
//
//         String token = (String) session.getAttribute("JWT_TOKEN");
//         if (token == null) {
//             return "redirect:/login";
//         }
//
//         RestTemplate restTemplate = new RestTemplate();
//
//         try {
//             if (imagenFile != null && !imagenFile.isEmpty()) {
//                 String nombre = imagenFile.getOriginalFilename();
//                 String ext = nombre.substring(nombre.lastIndexOf('.') + 1);
//                 if (!ext.equalsIgnoreCase("jpg") && !ext.equalsIgnoreCase("png")) {
//                     model.addAttribute("Error", "Formato de imagen no válido (solo JPG y PNG)");
//                     return "UsuarioForm";
//                 }
//                 String base64 = Base64.getEncoder().encodeToString(imagenFile.getBytes());
//                 usuario.setImagen(base64);
//             }
//
//             usuario.setStatus(true);
//
//             HttpHeaders headers = new HttpHeaders();
//             headers.setBearerAuth(token);
//             HttpEntity<Usuario> entity = new HttpEntity<>(usuario, headers);
//
//             ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(
//                     urlBase + "api/usuario/add",
//                     HttpMethod.POST,
//                     entity,
//                     new ParameterizedTypeReference<Result<Usuario>>() {
//             });
//
//             if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
//                 model.addAttribute("errorMessage", "No se pudo crear el usuario");
//                 return "UsuarioForm";
//             }
//
//             Usuario usuarioCreado = responseEntity.getBody().Object;
//             if (usuarioCreado == null || usuarioCreado.getToken() == null || usuarioCreado.getToken().isEmpty()) {
//                 model.addAttribute("errorMessage", "No se recibió token de verificación");
//                 return "UsuarioForm";
//             }
//
//             String verifyUrl = "http://localhost:8081/verify?token=" + usuarioCreado.getToken();
//
//             EmailRequest emailRequest = new EmailRequest();
//             emailRequest.setTo(usuarioCreado.getEmail());
//             emailRequest.setSubject("Verifica tu cuenta");
//             emailRequest.setBody("Haz clic en el siguiente enlace para verificar tu cuenta: " + verifyUrl);
//
//             HttpHeaders headersEmail = new HttpHeaders();
//             headersEmail.setBearerAuth(token);
//             headersEmail.setContentType(MediaType.APPLICATION_JSON);
//             HttpEntity<EmailRequest> entityEmail = new HttpEntity<>(emailRequest, headersEmail);
//
//             restTemplate.exchange(
//                     urlBase + "api/email/send",
//                     HttpMethod.POST,
//                     entityEmail,
//                     new ParameterizedTypeReference<Result>() {
//             });
//
//             return "redirect:/usuario";
//
//         } catch (HttpClientErrorException.Unauthorized ex) {
//             session.invalidate();
//             return "redirect:/login";
//         } catch (Exception ex) {
//             model.addAttribute("errorMessage", ex.getMessage());
//             return "UsuarioForm";
//         }
//     }
//
//     @PostMapping("/addDireccion")
//     public String gestionarDireccion(@ModelAttribute Direccion direccion, @RequestParam int IdUsuario,
//             @RequestParam int IdDireccion, Model model, HttpSession session) {
//
//         String token = (String) session.getAttribute("JWT_TOKEN");
//
//         if (token == null) {
//             return "redirect:/login";
//         }
//
//         RestTemplate restTemplate = new RestTemplate();
//
//         try {
//             HttpHeaders headers = new HttpHeaders();
//             headers.setBearerAuth(token);
//             HttpEntity<Direccion> entity = new HttpEntity<>(direccion, headers);
//
//             String url;
//             HttpMethod method;
//
//             if (IdDireccion == 0) {
//                 url = urlBase + "api/direccion/add/" + IdUsuario;
//                 method = HttpMethod.POST;
//             } else {
//                 url = urlBase + "api/usuario/" + IdUsuario + "/direccion/" + IdDireccion;
//                 method = HttpMethod.PUT;
//             }
//
//             ResponseEntity<Result<List<Direccion>>> responseEntity = restTemplate.exchange(
//                     url,
//                     method,
//                     entity,
//                     new ParameterizedTypeReference<Result<List<Direccion>>>() {
//             });
//
//             if (responseEntity.getStatusCode().is2xxSuccessful()) {
//                 String mensaje = IdDireccion == 0 ? "agregada" : "actualizada";
//                 model.addAttribute("successMessage", "Dirección " + mensaje + " correctamente");
//             }
//
//         } catch (HttpClientErrorException.Unauthorized ex) {
//             session.invalidate();
//             return "redirect:/login";
//         } catch (Exception ex) {
//             model.addAttribute("errorMessage", "Error: " + ex.getMessage());
//         }
//
//         return "redirect:/usuario/detail/?IdUsuario=" + IdUsuario;
//     }
//
//     @GetMapping("delete")
//     public String Delete(@RequestParam("IdUsuario") int IdUsuario, Model model, HttpSession session) {
//
//         String token = (String) session.getAttribute("JWT_TOKEN");
//
//         if (token == null) {
//             return "redirect:/login";
//         }
//
//         RestTemplate restTemplate = new RestTemplate();
//
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);
//         HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//         try {
//             restTemplate.exchange(
//                     urlBase + "api/usuario/delete?IdUsuario=" + IdUsuario,
//                     HttpMethod.DELETE,
//                     entity,
//                     Void.class);
//         } catch (HttpClientErrorException.Unauthorized ex) {
//             session.invalidate();
//             return "redirect:/login";
//         } catch (Exception ex) {
//             model.addAttribute("error", "Error: " + ex.getMessage());
//         }
//
//         return "redirect:/usuario";
//     }
//
//     @PostMapping("update")
//     public String Update(
//             @ModelAttribute Usuario usuario,
//             @RequestParam(value = "ImagenFile", required = false) org.springframework.web.multipart.MultipartFile imagenFile,
//             HttpSession session
//     ) {
//         String token = (String) session.getAttribute("JWT_TOKEN");
//         if (token == null) {
//             return "redirect:/login";
//         }
//
//         RestTemplate restTemplate = new RestTemplate();
//
//         try {
//             HttpHeaders headers = new HttpHeaders();
//             headers.setBearerAuth(token);
//             headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
//
//             if (imagenFile != null && !imagenFile.isEmpty()) {
//
//                 String contentType = imagenFile.getContentType();
//                 long size = imagenFile.getSize();
//                 if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
//                     return "redirect:/usuario/detail/?IdUsuario=" + usuario.getIdUsuario();
//                 }
//                 if (size > 2_000_000) {
//                     return "redirect:/usuario/detail/?IdUsuario=" + usuario.getIdUsuario();
//                 }
//
//                 byte[] bytes = imagenFile.getBytes();
//                 String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
//                 usuario.setImagen(base64);
//
//             } else {
//                 HttpEntity<Void> entityGet = new HttpEntity<>(headers);
//
//                 ResponseEntity<Result<Usuario>> responseGet = restTemplate.exchange(
//                         urlBase + "api/usuario/?id=" + usuario.getIdUsuario(),
//                         HttpMethod.GET,
//                         entityGet,
//                         new ParameterizedTypeReference<Result<Usuario>>() {
//                 }
//                 );
//
//                 if (responseGet.getStatusCode().is2xxSuccessful()
//                         && responseGet.getBody() != null
//                         && responseGet.getBody().Object != null) {
//                     Usuario actual = responseGet.getBody().Object;
//                     usuario.setImagen(actual.getImagen());
//                 }
//             }
//
//             HttpEntity<Usuario> entityPut = new HttpEntity<>(usuario, headers);
//
//             ResponseEntity<Result> responseEntity = restTemplate.exchange(
//                     urlBase + "api/usuario/update/" + usuario.getIdUsuario(),
//                     HttpMethod.PUT,
//                     entityPut,
//                     new ParameterizedTypeReference<Result>() {
//             }
//             );
//
//         } catch (HttpClientErrorException.Unauthorized ex) {
//             session.invalidate();
//             return "redirect:/login";
//         } catch (Exception ex) {
//            
//         }
//
//         return "redirect:/usuario/detail/?IdUsuario=" + usuario.getIdUsuario();
//     }
//
//     @PostMapping("updateDireccion")
//     public String UpdateDireccion(@ModelAttribute Direccion direccion, int IdUsuario, Model model,
//             HttpSession session) {
//
//         String token = (String) session.getAttribute("JWT_TOKEN");
//
//         if (token == null) {
//             return "redirect:/login";
//         }
//
//         RestTemplate restTemplate = new RestTemplate();
//         Result result = new Result();
//
//         try {
//             HttpHeaders headers = new HttpHeaders();
//             headers.setBearerAuth(token);
//             HttpEntity<Direccion> entity = new HttpEntity<>(direccion, headers);
//
//             ResponseEntity<Result<List<Direccion>>> responseEntity = restTemplate.exchange(urlBase,
//                     HttpMethod.PUT,
//                     entity,
//                     new ParameterizedTypeReference<Result<List<Direccion>>>() {
//             });
//
//             if (responseEntity.getStatusCode().value() == 200 || IdUsuario > 0) {
//
//                 result = responseEntity.getBody();
//                 model.addAttribute("direccion", result.Object);
//             }
//
//         } catch (HttpClientErrorException.Unauthorized ex) {
//             session.invalidate();
//             return "redirect:/login";
//         } catch (Exception ex) {
//             result.correct = false;
//             result.errorMessage = ex.getMessage();
//             model.addAttribute("errorMessage", result.errorMessage);
//         }
//
//         return ("redirect:/usuario/detail/?IdUsuario=" + IdUsuario);
//     }
//
//     @GetMapping("GetPaises/")
//     @ResponseBody
//     public Result GetPaises(HttpSession session) {
//
//         RestTemplate restTemplate = new RestTemplate();
//         String token = (String) session.getAttribute("JWT_TOKEN");
//         Result result = new Result();
//
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);
//         HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//         ResponseEntity<Result<List<Pais>>> responseEntity = restTemplate.exchange(
//                 urlBase + "api/pais",
//                 HttpMethod.GET,
//                 entity,
//                 new ParameterizedTypeReference<Result<List<Pais>>>() {
//         });
//
//         if (responseEntity.getStatusCode().value() == 200) {
//             result = responseEntity.getBody();
//         } else {
//             result.correct = false;
//         }
//
//         return result;
//     }
//
//     @GetMapping("GetEstados/")
//     @ResponseBody
//     public Result GetEstados(@RequestParam("IdPais") int IdPais, HttpSession session) {
//
//         RestTemplate restTemplate = new RestTemplate();
//         String token = (String) session.getAttribute("JWT_TOKEN");
//         Result result = new Result();
//
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);
//         HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//         ResponseEntity<Result<List<Estado>>> responseEntity = restTemplate.exchange(
//                 urlBase + "api/estado/pais?IdPais=" + IdPais,
//                 HttpMethod.GET,
//                 entity,
//                 new ParameterizedTypeReference<Result<List<Estado>>>() {
//         });
//
//         if (responseEntity.getStatusCode().value() == 200) {
//             result = responseEntity.getBody();
//         }
//
//         return result;
//     }
//
//     @GetMapping("GetMunicipio/")
//     @ResponseBody
//     public Result GetByEstado(@RequestParam("IdEstado") int IdEstado, Model model, HttpSession session) {
//
//         RestTemplate restTemplate = new RestTemplate();
//         String token = (String) session.getAttribute("JWT_TOKEN");
//         Result result = new Result();
//
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);
//         HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//         ResponseEntity<Result<List<Municipio>>> responseEntity = restTemplate.exchange(
//                 urlBase + "api/municipio/estado?IdEstado=" + IdEstado,
//                 HttpMethod.GET,
//                 entity,
//                 new ParameterizedTypeReference<Result<List<Municipio>>>() {
//         });
//         if (responseEntity.getStatusCode().value() == 200) {
//             result = responseEntity.getBody();
//         } else {
//             result.correct = false;
//         }
//
//         return result;
//     }
//
//     @GetMapping("GetColonia/")
//     @ResponseBody
//     public Result GetByMunicipio(@RequestParam("IdMunicipio") int IdMunicipio, HttpSession session) {
//
//         RestTemplate restTemplate = new RestTemplate();
//         String token = (String) session.getAttribute("JWT_TOKEN");
//         Result result = new Result();
//
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);
//         HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//         ResponseEntity<Result<List<Colonia>>> responseEntity = restTemplate.exchange(
//                 urlBase + "api/colonia/municipio?IdMunicipio=" + IdMunicipio,
//                 HttpMethod.GET,
//                 entity,
//                 new ParameterizedTypeReference<Result<List<Colonia>>>() {
//         });
//
//         if (responseEntity.getStatusCode().value() == 200) {
//             result = responseEntity.getBody();
//         }
//
//         return result;
//     }
//
//      // --------- Carga Masiva ----------
//      @GetMapping("/CargaMasiva")
//      public String CargaMasiva() {
//      return "CargaMasiva";
//      }
//     
//      @GetMapping("/CargaMasiva/Procesando")
//      public String CargaMasiva(HttpSession session, Model model) {
//     
//      Object pathObj = session.getAttribute("archivoCargaMasiva");
//      if (pathObj == null) {
//      model.addAttribute("errorMessage", "No se encontro el archivo para el archivo
//      para procesar.");
//      return "CargaMasiva";
//      }
//      String path = pathObj.toString();
//     
//      try {
//      List<Usuario> usuarios = new ArrayList<>();
//      if (path.endsWith(".txt")) {
//      usuarios = LecturaArchivoTXT(new File(path));
//      } else if (path.endsWith(".xlsx")) {
//      usuarios = LecturaArchivoXLSX(new File(path));
//      } else {
//      model.addAttribute("errorMessage", "El formato del archivo no es
//      compatible.");
//      return "CargaMasiva";
//      }
//      Result result = usuarioDAOImplementation.CargaMasiva(usuarios);
//     
//      if (result.correct) {
//      model.addAttribute("successMessage", "Carga masiva resalizado
//      correctamente");
//      model.addAttribute("usuariosIngresados", usuarios);
//      } else {
//      model.addAttribute("errorMessage", "Error al procesar usuarios: " +
//      result.ErrorMessage);
//      }
//     
//      } catch (Exception ex) {
//     
//      } finally {
//      session.removeAttribute("archivoCargaMasiva");
//      }
//      return "CargaMasiva";
//      }
//     
//      @PostMapping("/CargaMasiva")
//      public String CargaMasiva(@RequestParam("archivo") MultipartFile archivo,
//      Model model, HttpSession session) throws IOException {
//      String nombreArchivo = archivo.getOriginalFilename();
//      if (nombreArchivo == null || !nombreArchivo.contains(".")) {
//      model.addAttribute("errorMessage", "Extencion del archivo inválido");
//      return "CargaMasiva";
//      }
//      String Extencion = archivo.getOriginalFilename().split("\\.")[1];
//     
//      String path = System.getProperty("user.dir");
//      String pathArchivo = "src/main/resources/archivosCarga";
//      String fecha =
//      LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
//      String pathDefinitivo = path + "/" + pathArchivo + "/" + fecha +
//      archivo.getOriginalFilename();
//     
//      try {
//      archivo.transferTo(new File(pathDefinitivo));
//      } catch (Exception ex) {
//      model.addAttribute("errorMessage", "Error en el archivo");
//      }
//     
//      List<Usuario> usuarios = new ArrayList<>();
//     
//      try {
//     
//      if (Extencion.equals("txt")) {
//      usuarios = LecturaArchivoTXT(new File(pathDefinitivo));
//     
//      } else if (Extencion.equals("xlsx")) {
//      usuarios = LecturaArchivoXLSX(new File(pathDefinitivo));
//     
//      } else {
//      model.addAttribute("errorMessage", "Error por la extencion del archivo, no
//      compatible.");
//      return "CargaMasiva";
//      }
//     
//      List<ErrorCarga> errores = ValidarDatosArchivo(usuarios);
//     
//      if (errores.isEmpty()) {
//      model.addAttribute("sinErrores", true);
//      session.setAttribute("archivoCargaMasiva", pathDefinitivo);
//      } else {
//      model.addAttribute("sinErrores", false);
//      model.addAttribute("listaErrores", errores);
//      }
//     
//      } catch (Exception ex) {
//      model.addAttribute("errorMessage", "Error al leer archivo");
//     
//      }
//      return "CargaMasiva";
//     
//      }
//     
//      // --------- Carga Masiva ----------
//      // ---------- Validaciones -------------
//      public List<ErrorCarga> ValidarDatosArchivo(List<Usuario> usuarios) {
//     
//      List<ErrorCarga> erroresCarga = new ArrayList<>();
//     
//      int lineaError = 0;
//     
//      for (Usuario usuario : usuarios) {
//      lineaError++;
//      BindingResult bindingResult = validationService.validateObject(usuario);
//      List<ObjectError> errors = bindingResult.getAllErrors();
//      for (ObjectError error : errors) {
//      FieldError fieldError = (FieldError) error;
//      ErrorCarga errorCarga = new ErrorCarga();
//      errorCarga.campo = fieldError.getField();
//      errorCarga.descripcion = fieldError.getDefaultMessage();
//      errorCarga.linea = lineaError;
//      erroresCarga.add(errorCarga);
//      }
//      }
//      return erroresCarga;
//      }
//     
//      // ---------- Validaciones -------------
//     
//      // ---------- Lectura de Archivo -------------
//      public List<Usuario> LecturaArchivoTXT(File archivo) {
//      List<Usuario> usuarios = new ArrayList<>();
//     
//      try (BufferedReader bufferedReader = new BufferedReader(new
//      FileReader(archivo))) {
//      String linea;
//      SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
//      while ((linea = bufferedReader.readLine()) != null) {
//      String[] campos = linea.split("\\|");
//     
//      Usuario usuario = new Usuario();
//      usuario.setUserName(campos[0]);
//      usuario.setNombre(campos[1]);
//      usuario.setApellidoPaterno(campos[2]);
//      usuario.setApellidoMaterno(campos[3]);
//      usuario.setEmail(campos[4]);
//      usuario.setPassword(campos[5]);
//      usuario.setFechaNacimiento(formatoFecha.parse(campos[6]));
//      usuario.setSexo(campos[7].charAt(0));
//      usuario.setTelefono(campos[8]);
//      usuario.setCelular(campos[9]);
//      usuario.setCURP(campos[10]);
//      usuario.setImagen(campos[11]);
//     
//      Rol rol = new Rol();
//      rol.setIdRol(Integer.parseInt(campos[12]));
//      usuario.setRol(rol);
//     
//      Direccion direccion = new Direccion();
//      direccion.setCalle(campos[13]);
//      direccion.setNumeroInterior(campos[14]);
//      direccion.setNumeroExterior(campos[15]);
//     
//      Colonia colonia = new Colonia();
//      colonia.setNombre(campos[16]);
//      colonia.setCodigoPostal(campos[17]);
//     
//      Municipio municipio = new Municipio();
//      municipio.setNombre(campos[18]);
//     
//      Estado estado = new Estado();
//      estado.setNombre(campos[19]);
//     
//      Pais pais = new Pais();
//      pais.setNombre(campos[20]);
//     
//      estado.setPais(pais);
//      municipio.setEstado(estado);
//      colonia.setMunicipio(municipio);
//      direccion.setColonia(colonia);
//     
//      usuario.setDirecciones(new ArrayList<>());
//      usuario.getDirecciones().add(direccion);
//     
//      usuarios.add(usuario);
//      }
//      } catch (Exception ex) {
//      System.out.println(ex);
//      return null;
//      }
//     
//      return usuarios;
//      }
//     
//     // ---------- Lectura de Archivo -------------
//      // ---------- Lectura de Archivo -------------
//      private List<Usuario> LecturaArchivoXLSX(File archivo) {
//     
//      List<Usuario> usuarios = new ArrayList<>();
//      Result result = new Result();
//      try (InputStream fileInputStream = new FileInputStream(archivo); XSSFWorkbook
//      workBook = new XSSFWorkbook(fileInputStream)) {
//      XSSFSheet workSheet = workBook.getSheetAt(0);
//     
//      for (Row row : workSheet) {
//      Usuario usuario = new Usuario();
//      usuario.setRol(new Rol());
//      usuario.setUserName(row.getCell(0).toString());
//      usuario.setNombre(row.getCell(1).toString());
//      usuario.setApellidoPaterno(row.getCell(2).toString());
//      usuario.setApellidoMaterno(row.getCell(3).toString());
//      usuario.setEmail(row.getCell(4).toString());
//      usuario.setPassword(row.getCell(5).toString());
//      usuario.setFechaNacimiento(row.getCell(6).getDateCellValue());
//      usuario.setSexo(row.getCell(7).toString().charAt(0));
//      usuario.setTelefono(row.getCell(8).toString());
//      usuario.setCelular(row.getCell(9).toString());
//      usuario.setCURP(row.getCell(10).toString());
//      usuario.setImagen(row.getCell(11).toString());
//      usuario.getRol().setIdRol((int) row.getCell(12).getNumericCellValue());
//     
//      Direccion direccion = new Direccion();
//     
//      direccion.setCalle(row.getCell(13).toString());
//      direccion.setNumeroInterior(row.getCell(14).toString());
//      direccion.setNumeroExterior(row.getCell(15).toString());
//     
//      Colonia colonia = new Colonia();
//      colonia.setNombre(row.getCell(16).toString());
//      colonia.setCodigoPostal(row.getCell(17).toString());
//     
//      Municipio municipio = new Municipio();
//      municipio.setNombre(row.getCell(18).toString());
//     
//      Estado estado = new Estado();
//      estado.setNombre(row.getCell(19).toString());
//     
//      Pais pais = new Pais();
//      pais.setNombre(row.getCell(20).toString());
//     
//      estado.setPais(pais);
//      municipio.setEstado(estado);
//      colonia.setMunicipio(municipio);
//      direccion.setColonia(colonia);
//     
//      usuario.setDirecciones(new ArrayList<>());
//      usuario.getDirecciones().add(direccion);
//     
//      usuarios.add(usuario);
//      }
//     
//      } catch (Exception ex) {
//     
//      return null;
//      }
//     
//      return usuarios;
//     
//      }
//      // ---------- Lectura de Archivo -------------
// }
