package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Colonia;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Direccion;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Estado;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Municipio;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Pais;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;

import jakarta.servlet.http.HttpSession;

// Controlador para obtener datos de direcciones y llenar listas desplegables (DDL)
@Controller
@RequestMapping("usuario")
public class DDLGetController {

    private static final String urlBase="http://localhost:8080/";

    // Obtener una direccion especifica por su ID
    // getDireccion
    @GetMapping("/getDireccion")
    @ResponseBody
    public Direccion getDireccion(@RequestParam("IdDireccion") int IdDireccion, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();

        String token = (String) session.getAttribute("JWT_TOKEN");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Result<Direccion>> response = restTemplate.exchange(
                    urlBase + "api/direccion/" + IdDireccion,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<Direccion>>() {
            });

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().Object;
            }
        } catch (Exception ex) {

        }
        return null;
    }

    // Obtener la lista de todos los paises disponibles
    // GetPaises
    @GetMapping("GetPaises/")
    @ResponseBody
    public Result GetPaises(HttpSession session) {

        RestTemplate restTemplate = new RestTemplate();
        String token = (String) session.getAttribute("JWT_TOKEN");
        Result result = new Result();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Result<List<Pais>>> responseEntity = restTemplate.exchange(
                urlBase + "api/pais",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Result<List<Pais>>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        } else {
            result.correct = false;
        }

        return result;
    }

    // Obtener los estados pertenecientes a un pais especifico
    // GetEstados
    @GetMapping("GetEstados/")
    @ResponseBody
    public Result GetEstados(@RequestParam("IdPais") int IdPais, HttpSession session) {

        RestTemplate restTemplate = new RestTemplate();
        String token = (String) session.getAttribute("JWT_TOKEN");
        Result result = new Result();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Result<List<Estado>>> responseEntity = restTemplate.exchange(
                urlBase + "api/estado/pais?IdPais=" + IdPais,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Result<List<Estado>>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        }

        return result;
    }

    // Obtener los municipios pertenecientes a un estado especifico
    // GetByEstado
    @GetMapping("GetMunicipio/")
    @ResponseBody
    public Result GetByEstado(@RequestParam("IdEstado") int IdEstado, Model model, HttpSession session) {

        RestTemplate restTemplate = new RestTemplate();
        String token = (String) session.getAttribute("JWT_TOKEN");
        Result result = new Result();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Result<List<Municipio>>> responseEntity = restTemplate.exchange(
                urlBase + "api/municipio/estado?IdEstado=" + IdEstado,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Result<List<Municipio>>>() {
        });
        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        } else {
            result.correct = false;
        }

        return result;
    }

    // Obtener las colonias pertenecientes a un municipio especifico
    // GetByMunicipio
    @GetMapping("GetColonia/")
    @ResponseBody
    public Result GetByMunicipio(@RequestParam("IdMunicipio") int IdMunicipio, HttpSession session) {

        RestTemplate restTemplate = new RestTemplate();
        String token = (String) session.getAttribute("JWT_TOKEN");
        Result result = new Result();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Result<List<Colonia>>> responseEntity = restTemplate.exchange(
                urlBase + "api/colonia/municipio?IdMunicipio=" + IdMunicipio,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Result<List<Colonia>>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        }

        return result;
    }

}