package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Estado {

    private int IdEstado;
    
    @NotBlank(message = "El nombre del estado es obligatorio.")
    @Size(max = 100, message = "El nombre del estado es demasiado largo.")
    private String Nombre;
    
    @Valid
    @NotBlank(message = "El Pais (y sus datos) son obligatorios.")
    public Pais pais;

    public int getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(int IdEstado) {
        this.IdEstado = IdEstado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }
    
    
    
}
