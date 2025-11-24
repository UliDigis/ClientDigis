package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;
//
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Colonia;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Direccion;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.ErrorCarga;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Municipio;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Estado;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Pais;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Rol;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Usuario;
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
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;

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
//        model.addAttribute("usuarioBusqueda", new Usuario());
        return "index";
    }

    @GetMapping("detail/")
    public String Detail(@RequestParam("IdUsuario") int IdUsuario, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Usuario>>> responseEntity = restTemplate.exchange(urlBase + "api/usuario/?id=" + IdUsuario,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Usuario>>>() {
        });
        if (responseEntity.getStatusCode().value() == 200) {
            Result result = responseEntity.getBody();
            model.addAttribute("usuario", result.Object);
        }

        return "UsuarioDetail";
    }

//    Esatdo a Pais -----------------------------------------------------------
    @GetMapping("GetEstados/")
    @ResponseBody
    public Result GetByPais(@RequestParam("IdPais") int IdPais, Model model) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        ResponseEntity<Result<List<Pais>>> responseEntity = restTemplate.exchange(urlBase + "api/estado/pais?IdPais=" + IdPais,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Pais>>>() {
        });
        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        }
        return result;
    }

//    Estado a Pais -----------------------------------------------------------
//    Municipio a Estado ---------------------------------------------------------
    @GetMapping("GetMunicipio/")
    @ResponseBody
    public Result GetByEstado(@RequestParam("IdEstado") int IdEstado, Model model) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        ResponseEntity<Result<List<Estado>>> responseEntity = restTemplate.exchange(urlBase + "api/municipio/estado?IdEstado=" + IdEstado,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Estado>>>() {
        });
        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        } else {
            result.Correct = false;
        }

        return result;
    }
//    Municipio a Esatdo ---------------------------------------------------------
//    Colonia a Municipio --------------------------------------------------------

    @GetMapping("GetColonia/")
    @ResponseBody
    public Result GetByMunicipio(@RequestParam("IdMunicipio") int IdMunicipio) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        ResponseEntity<Result<List<Municipio>>> responseEntity = restTemplate.exchange(urlBase + "api/colonia/municipio?IdMunicipio=" + IdMunicipio,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Municipio>>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        }

        return result;
    }

//    Colonia a Municipio --------------------------------------------------------
//Add ------------------------------------------------------------------------
    @PostMapping("/add")
    public String Add(Model model) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        ResponseEntity<Result<List<Rol>>> responseEntity = restTemplate.exchange(urlBase + "api/rol",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        }
        return "usuarioForm";
    }

//Add ------------------------------------------------------------------------
//    Delete ------------------------------------------------------------------
    @GetMapping("delete")
    public String Delete(@RequestParam("IdUsuario") int IdUsuario, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.exchange(
                    urlBase + "api/usuario/delete?IdUsuario=" + IdUsuario, 
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    Void.class
            );
        } catch (Exception ex) {
            model.addAttribute("error", "Error: " + ex.getMessage());
        }

        return "redirect:/usuario";
    }

//    Delete ------------------------------------------------------------------
//    // ------------- Add Usuairo ---------------
//    // GET
//    @GetMapping("/add")
//    public String Add(Model model) {
//
//        Usuario usuario = new Usuario();
//        Direccion direccion = new Direccion();
//        Colonia colonia = new Colonia();
//        Municipio municipio = new Municipio();
//
//        direccion.setColonia(colonia);
//
//        usuario.setDirecciones(new ArrayList<>(Arrays.asList(direccion)));
//        Result resulRol = rolJPADAOImplementation.GetAll();
//        Result resultPaises = paisJPADAOImplementation.GetAll();
//        model.addAttribute("roles", resulRol.Objects);
//        model.addAttribute("Paises", resultPaises.Objects);
//        model.addAttribute("Usuario", usuario);
//        
//
//        return "UsuarioForm";
//
//    }
//
//    // POST
//    @PostMapping("/add")
//    public String Add(@ModelAttribute("Usuario") Usuario usuario,
//            @RequestParam("imagenFiel") MultipartFile imagenFile, Model model) {
//
//        if (imagenFile != null) {
//            try {
//
//                String extencion = imagenFile.getOriginalFilename().split("\\.")[1];
//                if (extencion.equals("jpg") || extencion.equals("png")) {
//
//                    byte[] byteImagen = imagenFile.getBytes();
//                    String imagenBase64 = Base64.getEncoder().encodeToString(byteImagen);
//                    usuario.setImagen(imagenBase64);
//
//                } else {
//                    model.addAttribute("Error", "Formato de imagen no valida");
//                    return "UsuarioForm";
//                }
//
//            } catch (Exception ex) {
//                model.addAttribute("Error: Ocurrio un error con la imagen");
//                return "UsuarioForm";
//            }
//        }
//
//        Result result = usuarioJPADAOImplementation.AddUsuarioJPA(usuario);
//
//        if (result.Correct) {
//            return "redirect:/Usuario/GetAll";
//        } else {
//            model.addAttribute("Error: Error al registrar al usuario" + result.ErrorMessage);
//            return "UsuarioForm";
//        }
//
//    }
//
//    // ------------- Add Usuairo ---------------
//    // --------- GetAll Usuario -----------
//    
//    // --------- GetAll Usuario -----------
//
//    // --------- Carga Masiva ----------
//    @GetMapping("/CargaMasiva")
//    public String CargaMasiva() {
//        return "CargaMasiva";
//    }
//
//    @GetMapping("/CargaMasiva/Procesando")
//    public String CargaMasiva(HttpSession session, Model model) {
//
//        Object pathObj = session.getAttribute("archivoCargaMasiva");
//        if (pathObj == null) {
//            model.addAttribute("errorMessage", "No se encontro el archivo para el archivo para procesar.");
//            return "CargaMasiva";
//        }
//        String path = pathObj.toString();
//
//        try {
//            List<Usuario> usuarios = new ArrayList<>();
//            if (path.endsWith(".txt")) {
//                usuarios = LecturaArchivoTXT(new File(path));
//            } else if (path.endsWith(".xlsx")) {
//                usuarios = LecturaArchivoXLSX(new File(path));
//            } else {
//                model.addAttribute("errorMessage", "El formato del archivo no es compatible.");
//                return "CargaMasiva";
//            }
//            Result result = usuarioDAOImplementation.CargaMasiva(usuarios);
//
//            if (result.Correct) {
//                model.addAttribute("successMessage", "Carga masiva resalizado correctamente");
//                model.addAttribute("usuariosIngresados", usuarios);
//            } else {
//                model.addAttribute("errorMessage", "Error al procesar usuarios: " + result.ErrorMessage);
//            }
//
//        } catch (Exception ex) {
//
//        } finally {
//            session.removeAttribute("archivoCargaMasiva");
//        }
//        return "CargaMasiva";
//    }
//
//    @PostMapping("/CargaMasiva")
//    public String CargaMasiva(@RequestParam("archivo") MultipartFile archivo, Model model, HttpSession session) throws IOException {
//        String nombreArchivo = archivo.getOriginalFilename();
//        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
//            model.addAttribute("errorMessage", "Extencion del archivo inválido");
//            return "CargaMasiva";
//        }
//        String Extencion = archivo.getOriginalFilename().split("\\.")[1];
//
//        String path = System.getProperty("user.dir");
//        String pathArchivo = "src/main/resources/archivosCarga";
//        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
//        String pathDefinitivo = path + "/" + pathArchivo + "/" + fecha + archivo.getOriginalFilename();
//
//        try {
//            archivo.transferTo(new File(pathDefinitivo));
//        } catch (Exception ex) {
//            model.addAttribute("errorMessage", "Error en el archivo");
//        }
//
//        List<Usuario> usuarios = new ArrayList<>();
//
//        try {
//
//            if (Extencion.equals("txt")) {
//                usuarios = LecturaArchivoTXT(new File(pathDefinitivo));
//
//            } else if (Extencion.equals("xlsx")) {
//                usuarios = LecturaArchivoXLSX(new File(pathDefinitivo));
//
//            } else {
//                model.addAttribute("errorMessage", "Error por la extencion del archivo, no compatible.");
//                return "CargaMasiva";
//            }
//
//            List<ErrorCarga> errores = ValidarDatosArchivo(usuarios);
//
//            if (errores.isEmpty()) {
//                model.addAttribute("sinErrores", true);
//                session.setAttribute("archivoCargaMasiva", pathDefinitivo);
//            } else {
//                model.addAttribute("sinErrores", false);
//                model.addAttribute("listaErrores", errores);
//            }
//
//        } catch (Exception ex) {
//            model.addAttribute("errorMessage", "Error al leer archivo");
//
//        }
//        return "CargaMasiva";
//
//    }
//
//    // --------- Carga Masiva ----------
//    //  ---------- Validaciones -------------
//    public List<ErrorCarga> ValidarDatosArchivo(List<Usuario> usuarios) {
//
//        List<ErrorCarga> erroresCarga = new ArrayList<>();
//
//        int lineaError = 0;
//
//        for (Usuario usuario : usuarios) {
//            lineaError++;
//            BindingResult bindingResult = validationService.validateObject(usuario);
//            List<ObjectError> errors = bindingResult.getAllErrors();
//            for (ObjectError error : errors) {
//                FieldError fieldError = (FieldError) error;
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.campo = fieldError.getField();
//                errorCarga.descripcion = fieldError.getDefaultMessage();
//                errorCarga.linea = lineaError;
//                erroresCarga.add(errorCarga);
//            }
//        }
//        return erroresCarga;
//    }
//
//    //  ---------- Validaciones -------------
//    
//    //  ---------- Lectura de Archivo -------------
//    public List<Usuario> LecturaArchivoTXT(File archivo) {
//        List<Usuario> usuarios = new ArrayList<>();
//
//        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
//            String linea;
//            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
//            while ((linea = bufferedReader.readLine()) != null) {
//                String[] campos = linea.split("\\|");
//
//                Usuario usuario = new Usuario();
//                usuario.setUserName(campos[0]);
//                usuario.setNombre(campos[1]);
//                usuario.setApellidoPaterno(campos[2]);
//                usuario.setApellidoMaterno(campos[3]);
//                usuario.setEmail(campos[4]);
//                usuario.setPassword(campos[5]);
//                usuario.setFechaNacimiento(formatoFecha.parse(campos[6]));
//                usuario.setSexo(campos[7].charAt(0));
//                usuario.setTelefono(campos[8]);
//                usuario.setCelular(campos[9]);
//                usuario.setCURP(campos[10]);
//                usuario.setImagen(campos[11]);
//
//                Rol rol = new Rol();
//                rol.setIdRol(Integer.parseInt(campos[12]));
//                usuario.setRol(rol);
//
//                Direccion direccion = new Direccion();
//                direccion.setCalle(campos[13]);
//                direccion.setNumeroInterior(campos[14]);
//                direccion.setNumeroExterior(campos[15]);
//
//                Colonia colonia = new Colonia();
//                colonia.setNombre(campos[16]);
//                colonia.setCodigoPostal(campos[17]);
//
//                Municipio municipio = new Municipio();
//                municipio.setNombre(campos[18]);
//
//                Estado estado = new Estado();
//                estado.setNombre(campos[19]);
//
//                Pais pais = new Pais();
//                pais.setNombre(campos[20]);
//
//                estado.setPais(pais);
//                municipio.setEstado(estado);
//                colonia.setMunicipio(municipio);
//                direccion.setColonia(colonia);
//                
//                usuario.setDirecciones(new ArrayList<>());
//                usuario.getDirecciones().add(direccion);
//
//                usuarios.add(usuario);
//            }
//        } catch (Exception ex) {
//            System.out.println(ex);
//            return null;
//        }
//
//        return usuarios;
//    }
//
////      ---------- Lectura de Archivo -------------
//    //  ---------- Lectura de Archivo -------------
//    private List<Usuario> LecturaArchivoXLSX(File archivo) {
//
//        List<Usuario> usuarios = new ArrayList<>();
//        Result result = new Result();
//        try (InputStream fileInputStream = new FileInputStream(archivo); XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream)) {
//            XSSFSheet workSheet = workBook.getSheetAt(0);
//
//            for (Row row : workSheet) {
//                Usuario usuario = new Usuario();
//                usuario.setRol(new Rol());
//                usuario.setUserName(row.getCell(0).toString());
//                usuario.setNombre(row.getCell(1).toString());
//                usuario.setApellidoPaterno(row.getCell(2).toString());
//                usuario.setApellidoMaterno(row.getCell(3).toString());
//                usuario.setEmail(row.getCell(4).toString());
//                usuario.setPassword(row.getCell(5).toString());
//                usuario.setFechaNacimiento(row.getCell(6).getDateCellValue());
//                usuario.setSexo(row.getCell(7).toString().charAt(0));
//                usuario.setTelefono(row.getCell(8).toString());
//                usuario.setCelular(row.getCell(9).toString());
//                usuario.setCURP(row.getCell(10).toString());
//                usuario.setImagen(row.getCell(11).toString());
//                usuario.getRol().setIdRol((int) row.getCell(12).getNumericCellValue());
//
//                Direccion direccion = new Direccion();
//
//                direccion.setCalle(row.getCell(13).toString());
//                direccion.setNumeroInterior(row.getCell(14).toString());
//                direccion.setNumeroExterior(row.getCell(15).toString());
//
//                Colonia colonia = new Colonia();
//                colonia.setNombre(row.getCell(16).toString());
//                colonia.setCodigoPostal(row.getCell(17).toString());
//
//                Municipio municipio = new Municipio();
//                municipio.setNombre(row.getCell(18).toString());
//
//                Estado estado = new Estado();
//                estado.setNombre(row.getCell(19).toString());
//
//                Pais pais = new Pais();
//                pais.setNombre(row.getCell(20).toString());
//
//                estado.setPais(pais);
//                municipio.setEstado(estado);
//                colonia.setMunicipio(municipio);
//                direccion.setColonia(colonia);
//
//                usuario.setDirecciones(new ArrayList<>());
//                usuario.getDirecciones().add(direccion);
//
//                usuarios.add(usuario);
//            }
//
//        } catch (Exception ex) {
//
//            return null;
//        }
//
//        return usuarios;
//
//    }
//    //  ---------- Lectura de Archivo -------------
}
