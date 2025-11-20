package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Rol {

    private int IdRol;

    @NotBlank(message = "El nombre del Rol es obligatorio.")
    @Size(max = 50, message = "El nombre del rol es demasiado largo.")
    private String Nombre;

    public int getIdRol() {
        return IdRol;
    }

    public void setIdRol(int IdRol) {
        this.IdRol = IdRol;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

}
