package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Municipio {
    
    private int IdMunicipio;
    
    @NotBlank(message = "El Nombre del municipio es obligatorio.")
    @Size(max = 100, message = "El nombre del municipio es demasiado largo.")
    private String Nombre;
    
    @Valid
    @NotBlank(message = "El Estado (y sus datos) son obligatorios.")
    public Estado estado;

    public int getIdMunicipio() {
        return IdMunicipio;
    }

    public void setIdMunicipio(int IdMunicipio) {
        this.IdMunicipio = IdMunicipio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    
    
}
