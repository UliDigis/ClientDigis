package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;
//

//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Colonia;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Direccion;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.ErrorCarga;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Colonia;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Direccion;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Municipio;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Estado;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Pais;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Rol;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Usuario;

import java.util.Base64;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service.ValidationService;
//import jakarta.servlet.http.HttpSession;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Base64;
import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private static final String urlBase = "http://localhost:8080/";

    @GetMapping
    public String GetAll(Model model) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Usuario>>> responseEntity = restTemplate.exchange(urlBase + "api/usuario",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Usuario>>>() {
                });

        if (responseEntity.getStatusCode().value() == 200) {
            Result result = responseEntity.getBody();
            model.addAttribute("usuarios", result.Object);
        } else {
            model.addAttribute("errorMessage", "fallo");
        }
        // model.addAttribute("usuarioBusqueda", new Usuario());
        return "index";
    }

    @GetMapping("detail/")
    public String Detail(@RequestParam("IdUsuario") int IdUsuario, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(
                    urlBase + "api/usuario/?id=" + IdUsuario,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Usuario>>() {
                    });

            ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(
                    urlBase + "api/rol",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<List<Rol>>>() {
                    });

            ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(
                    urlBase + "api/pais",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<List<Pais>>>() {
                    });

            if (responseEntity.getStatusCode().is2xxSuccessful() &&
                    responseEntityRol.getStatusCode().is2xxSuccessful() &&
                    responseEntityPais.getStatusCode().is2xxSuccessful()) {

                Result<Usuario> resultUsuario = responseEntity.getBody();
                Result<List<Rol>> resultRol = responseEntityRol.getBody();
                Result<List<Pais>> resultPais = responseEntityPais.getBody();

                model.addAttribute("usuario", resultUsuario.Object);
                model.addAttribute("rol", resultRol.Object);
                model.addAttribute("paises", resultPais.Object);
            } else {
                model.addAttribute("errorMessage", "Error al cargar los datos");
            }

        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error: " + ex.getMessage());
        }

        return "UsuarioDetail";
    }

    @GetMapping("/getDireccion")
    @ResponseBody
    public Direccion getDireccion(@RequestParam("IdDireccion") int IdDireccion) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Result<Direccion>> response = restTemplate.exchange(
                    urlBase + "api/direccion/" + IdDireccion,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Direccion>>() {
                    });

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().Object; // devuelve la Direccion como JSON
            }
        } catch (Exception ex) {
            // opcional: log o manejo
        }
        return null;
    }

    // Add ------------------------------------------------------------------------
    @GetMapping("/add")
    public String Add(Model model) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        try {

            ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(urlBase + "api/pais",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<List<Pais>>>() {
                    });
            if (responseEntityPais.getStatusCode().value() == 200) {
                result = responseEntityPais.getBody();
                model.addAttribute("paises", result.Object);
            }
            ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(urlBase + "api/rol",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<List<Rol>>>() {
                    });

            if (responseEntityRol.getStatusCode().value() == 200) {
                result = responseEntityRol.getBody();
                model.addAttribute("roles", result.Object);
            }
            Usuario usuario = new Usuario();
            model.addAttribute("usuario", usuario);
        } catch (Exception ex) {
            result.Correct = false;
        }

        return "usuarioForm";
    }

    @PostMapping("/add")
    public String Add(@ModelAttribute Usuario usuario,
            @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
            Model model) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

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
            HttpEntity<Usuario> entity = new HttpEntity<>(usuario);

            ResponseEntity<Result<List<Usuario>>> responseEntity = restTemplate.exchange(urlBase + "api/usuario/add",
                    HttpMethod.POST, entity,
                    new ParameterizedTypeReference<Result<List<Usuario>>>() {
                    });

            if (responseEntity.getStatusCode().value() == 201) {
                return "redirect:/usuario";
            }

        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            result.ErrorMessage = ex.getMessage();
            return "UsuarioForm";
        }

        return "UsuarioForm";
    }

    @PostMapping("/addDireccion")
    public String gestionarDireccion(@ModelAttribute Direccion direccion, @RequestParam int IdUsuario,
            @RequestParam int IdDireccion, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpEntity<Direccion> entity = new HttpEntity<>(direccion);
            String url;
            HttpMethod method;

            if (IdDireccion == 0) {
                url = urlBase + "api/direccion/add/" + IdUsuario;
                method = HttpMethod.POST;
            } else {
                url = urlBase + "api/usuario/" + IdUsuario + "/direccion/" + IdDireccion;
                method = HttpMethod.PUT;
            }

            ResponseEntity<Result<List<Direccion>>> responseEntity = restTemplate.exchange(
                    url,
                    method,
                    entity,
                    new ParameterizedTypeReference<Result<List<Direccion>>>() {
                    });

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String mensaje = IdDireccion == 0 ? "agregada" : "actualizada";
                model.addAttribute("successMessage", "Dirección " + mensaje + " correctamente");
            }

        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error: " + ex.getMessage());
        }

        return "redirect:/usuario/detail/?IdUsuario=" + IdUsuario;
    }

    // Add ------------------------------------------------------------------------
    // Delete ------------------------------------------------------------------
    @GetMapping("delete")
    public String Delete(@RequestParam("IdUsuario") int IdUsuario, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.exchange(
                    urlBase + "api/usuario/delete?IdUsuario=" + IdUsuario,
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    Void.class);
        } catch (Exception ex) {
            model.addAttribute("error", "Error: " + ex.getMessage());
        }

        return "redirect:/usuario";
    }

    @PostMapping("update")
    public String Update(@ModelAttribute Usuario usuario, Model model) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        try {
            HttpEntity<Usuario> entity = new HttpEntity<>(usuario);

            ResponseEntity<Result> responseEntity = restTemplate.exchange(
                    urlBase + "api/usuario/update/" + usuario.getIdUsuario(),
                    HttpMethod.PUT,
                    entity,
                    new ParameterizedTypeReference<Result>() {
                    });

            if (responseEntity.getStatusCode().value() == 200) {
                result = responseEntity.getBody();

            }

        } catch (Exception ex) {
            result.Correct = false;
            result.ex = ex;
        }
        return "redirect:/usuario/detail/?IdUsuario=" + usuario.getIdUsuario();
    }

    @PostMapping("updateDireccion")
    public String UpdateDireccion(@ModelAttribute Direccion direccion, int IdUsuario, Model model) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        try {
            HttpEntity<Direccion> entity = new HttpEntity<>(direccion);

            ResponseEntity<Result<List<Direccion>>> responseEntity = restTemplate.exchange(urlBase,
                    HttpMethod.PUT,
                    entity,
                    new ParameterizedTypeReference<Result<List<Direccion>>>() {
                    });

            if (responseEntity.getStatusCode().value() == 200 || IdUsuario > 0) {

                result = responseEntity.getBody();
                model.addAttribute("direccion", result.Object);
            }

        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getMessage();
            model.addAttribute("errorMessage", result.ErrorMessage);
        }

        return ("redirect:/usuario/detail/?IdUsuario=" + IdUsuario);
    }

    @GetMapping("GetPaises/")
    @ResponseBody
    public Result GetPaises() {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        ResponseEntity<Result<List<Pais>>> responseEntity = restTemplate.exchange(
                urlBase + "api/pais",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Pais>>>() {
                });

        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        } else {
            result.Correct = false;
        }

        return result;
    }

    // Esatdo a Pais -----------------------------------------------------------
    @GetMapping("GetEstados/")
    @ResponseBody
    public Result GetEstados(@RequestParam("IdPais") int IdPais) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        ResponseEntity<Result<List<Estado>>> responseEntity = restTemplate.exchange(
                urlBase + "api/estado/pais?IdPais=" + IdPais,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Estado>>>() {
                });

        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        }

        return result;
    }

    // Estado a Pais -----------------------------------------------------------
    // Municipio a Estado ---------------------------------------------------------
    @GetMapping("GetMunicipio/")
    @ResponseBody
    public Result GetByEstado(@RequestParam("IdEstado") int IdEstado, Model model) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        ResponseEntity<Result<List<Municipio>>> responseEntity = restTemplate.exchange(
                urlBase + "api/municipio/estado?IdEstado=" + IdEstado,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Municipio>>>() {
                });
        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        } else {
            result.Correct = false;
        }

        return result;
    }
    // Municipio a Esatdo ---------------------------------------------------------
    // Colonia a Municipio --------------------------------------------------------

    @GetMapping("GetColonia/")
    @ResponseBody
    public Result GetByMunicipio(@RequestParam("IdMunicipio") int IdMunicipio) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        ResponseEntity<Result<List<Colonia>>> responseEntity = restTemplate.exchange(
                urlBase + "api/colonia/municipio?IdMunicipio=" + IdMunicipio,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Colonia>>>() {
                });

        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        }

        return result;
    }

    // // --------- Carga Masiva ----------
    // @GetMapping("/CargaMasiva")
    // public String CargaMasiva() {
    // return "CargaMasiva";
    // }
    //
    // @GetMapping("/CargaMasiva/Procesando")
    // public String CargaMasiva(HttpSession session, Model model) {
    //
    // Object pathObj = session.getAttribute("archivoCargaMasiva");
    // if (pathObj == null) {
    // model.addAttribute("errorMessage", "No se encontro el archivo para el archivo
    // para procesar.");
    // return "CargaMasiva";
    // }
    // String path = pathObj.toString();
    //
    // try {
    // List<Usuario> usuarios = new ArrayList<>();
    // if (path.endsWith(".txt")) {
    // usuarios = LecturaArchivoTXT(new File(path));
    // } else if (path.endsWith(".xlsx")) {
    // usuarios = LecturaArchivoXLSX(new File(path));
    // } else {
    // model.addAttribute("errorMessage", "El formato del archivo no es
    // compatible.");
    // return "CargaMasiva";
    // }
    // Result result = usuarioDAOImplementation.CargaMasiva(usuarios);
    //
    // if (result.Correct) {
    // model.addAttribute("successMessage", "Carga masiva resalizado
    // correctamente");
    // model.addAttribute("usuariosIngresados", usuarios);
    // } else {
    // model.addAttribute("errorMessage", "Error al procesar usuarios: " +
    // result.ErrorMessage);
    // }
    //
    // } catch (Exception ex) {
    //
    // } finally {
    // session.removeAttribute("archivoCargaMasiva");
    // }
    // return "CargaMasiva";
    // }
    //
    // @PostMapping("/CargaMasiva")
    // public String CargaMasiva(@RequestParam("archivo") MultipartFile archivo,
    // Model model, HttpSession session) throws IOException {
    // String nombreArchivo = archivo.getOriginalFilename();
    // if (nombreArchivo == null || !nombreArchivo.contains(".")) {
    // model.addAttribute("errorMessage", "Extencion del archivo inválido");
    // return "CargaMasiva";
    // }
    // String Extencion = archivo.getOriginalFilename().split("\\.")[1];
    //
    // String path = System.getProperty("user.dir");
    // String pathArchivo = "src/main/resources/archivosCarga";
    // String fecha =
    // LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
    // String pathDefinitivo = path + "/" + pathArchivo + "/" + fecha +
    // archivo.getOriginalFilename();
    //
    // try {
    // archivo.transferTo(new File(pathDefinitivo));
    // } catch (Exception ex) {
    // model.addAttribute("errorMessage", "Error en el archivo");
    // }
    //
    // List<Usuario> usuarios = new ArrayList<>();
    //
    // try {
    //
    // if (Extencion.equals("txt")) {
    // usuarios = LecturaArchivoTXT(new File(pathDefinitivo));
    //
    // } else if (Extencion.equals("xlsx")) {
    // usuarios = LecturaArchivoXLSX(new File(pathDefinitivo));
    //
    // } else {
    // model.addAttribute("errorMessage", "Error por la extencion del archivo, no
    // compatible.");
    // return "CargaMasiva";
    // }
    //
    // List<ErrorCarga> errores = ValidarDatosArchivo(usuarios);
    //
    // if (errores.isEmpty()) {
    // model.addAttribute("sinErrores", true);
    // session.setAttribute("archivoCargaMasiva", pathDefinitivo);
    // } else {
    // model.addAttribute("sinErrores", false);
    // model.addAttribute("listaErrores", errores);
    // }
    //
    // } catch (Exception ex) {
    // model.addAttribute("errorMessage", "Error al leer archivo");
    //
    // }
    // return "CargaMasiva";
    //
    // }
    //
    // // --------- Carga Masiva ----------
    // // ---------- Validaciones -------------
    // public List<ErrorCarga> ValidarDatosArchivo(List<Usuario> usuarios) {
    //
    // List<ErrorCarga> erroresCarga = new ArrayList<>();
    //
    // int lineaError = 0;
    //
    // for (Usuario usuario : usuarios) {
    // lineaError++;
    // BindingResult bindingResult = validationService.validateObject(usuario);
    // List<ObjectError> errors = bindingResult.getAllErrors();
    // for (ObjectError error : errors) {
    // FieldError fieldError = (FieldError) error;
    // ErrorCarga errorCarga = new ErrorCarga();
    // errorCarga.campo = fieldError.getField();
    // errorCarga.descripcion = fieldError.getDefaultMessage();
    // errorCarga.linea = lineaError;
    // erroresCarga.add(errorCarga);
    // }
    // }
    // return erroresCarga;
    // }
    //
    // // ---------- Validaciones -------------
    //
    // // ---------- Lectura de Archivo -------------
    // public List<Usuario> LecturaArchivoTXT(File archivo) {
    // List<Usuario> usuarios = new ArrayList<>();
    //
    // try (BufferedReader bufferedReader = new BufferedReader(new
    // FileReader(archivo))) {
    // String linea;
    // SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    // while ((linea = bufferedReader.readLine()) != null) {
    // String[] campos = linea.split("\\|");
    //
    // Usuario usuario = new Usuario();
    // usuario.setUserName(campos[0]);
    // usuario.setNombre(campos[1]);
    // usuario.setApellidoPaterno(campos[2]);
    // usuario.setApellidoMaterno(campos[3]);
    // usuario.setEmail(campos[4]);
    // usuario.setPassword(campos[5]);
    // usuario.setFechaNacimiento(formatoFecha.parse(campos[6]));
    // usuario.setSexo(campos[7].charAt(0));
    // usuario.setTelefono(campos[8]);
    // usuario.setCelular(campos[9]);
    // usuario.setCURP(campos[10]);
    // usuario.setImagen(campos[11]);
    //
    // Rol rol = new Rol();
    // rol.setIdRol(Integer.parseInt(campos[12]));
    // usuario.setRol(rol);
    //
    // Direccion direccion = new Direccion();
    // direccion.setCalle(campos[13]);
    // direccion.setNumeroInterior(campos[14]);
    // direccion.setNumeroExterior(campos[15]);
    //
    // Colonia colonia = new Colonia();
    // colonia.setNombre(campos[16]);
    // colonia.setCodigoPostal(campos[17]);
    //
    // Municipio municipio = new Municipio();
    // municipio.setNombre(campos[18]);
    //
    // Estado estado = new Estado();
    // estado.setNombre(campos[19]);
    //
    // Pais pais = new Pais();
    // pais.setNombre(campos[20]);
    //
    // estado.setPais(pais);
    // municipio.setEstado(estado);
    // colonia.setMunicipio(municipio);
    // direccion.setColonia(colonia);
    //
    // usuario.setDirecciones(new ArrayList<>());
    // usuario.getDirecciones().add(direccion);
    //
    // usuarios.add(usuario);
    // }
    // } catch (Exception ex) {
    // System.out.println(ex);
    // return null;
    // }
    //
    // return usuarios;
    // }
    //
    //// ---------- Lectura de Archivo -------------
    // // ---------- Lectura de Archivo -------------
    // private List<Usuario> LecturaArchivoXLSX(File archivo) {
    //
    // List<Usuario> usuarios = new ArrayList<>();
    // Result result = new Result();
    // try (InputStream fileInputStream = new FileInputStream(archivo); XSSFWorkbook
    // workBook = new XSSFWorkbook(fileInputStream)) {
    // XSSFSheet workSheet = workBook.getSheetAt(0);
    //
    // for (Row row : workSheet) {
    // Usuario usuario = new Usuario();
    // usuario.setRol(new Rol());
    // usuario.setUserName(row.getCell(0).toString());
    // usuario.setNombre(row.getCell(1).toString());
    // usuario.setApellidoPaterno(row.getCell(2).toString());
    // usuario.setApellidoMaterno(row.getCell(3).toString());
    // usuario.setEmail(row.getCell(4).toString());
    // usuario.setPassword(row.getCell(5).toString());
    // usuario.setFechaNacimiento(row.getCell(6).getDateCellValue());
    // usuario.setSexo(row.getCell(7).toString().charAt(0));
    // usuario.setTelefono(row.getCell(8).toString());
    // usuario.setCelular(row.getCell(9).toString());
    // usuario.setCURP(row.getCell(10).toString());
    // usuario.setImagen(row.getCell(11).toString());
    // usuario.getRol().setIdRol((int) row.getCell(12).getNumericCellValue());
    //
    // Direccion direccion = new Direccion();
    //
    // direccion.setCalle(row.getCell(13).toString());
    // direccion.setNumeroInterior(row.getCell(14).toString());
    // direccion.setNumeroExterior(row.getCell(15).toString());
    //
    // Colonia colonia = new Colonia();
    // colonia.setNombre(row.getCell(16).toString());
    // colonia.setCodigoPostal(row.getCell(17).toString());
    //
    // Municipio municipio = new Municipio();
    // municipio.setNombre(row.getCell(18).toString());
    //
    // Estado estado = new Estado();
    // estado.setNombre(row.getCell(19).toString());
    //
    // Pais pais = new Pais();
    // pais.setNombre(row.getCell(20).toString());
    //
    // estado.setPais(pais);
    // municipio.setEstado(estado);
    // colonia.setMunicipio(municipio);
    // direccion.setColonia(colonia);
    //
    // usuario.setDirecciones(new ArrayList<>());
    // usuario.getDirecciones().add(direccion);
    //
    // usuarios.add(usuario);
    // }
    //
    // } catch (Exception ex) {
    //
    // return null;
    // }
    //
    // return usuarios;
    //
    // }
    // // ---------- Lectura de Archivo -------------
}
