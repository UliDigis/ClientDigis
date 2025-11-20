package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Direccion {

    private int IdDireccion;
    
    @NotBlank (message = "La calle es obligatoria.")
    @Size(max = 100, message = "LA calle no puede exceder los 100 caracteres.")
    private String Calle;
    
    @NotBlank(message = "El numero Interior es obligatorio.")
    @Size(max = 10, message = "El numero Interior es demasiado largo")
    private String NumeroInterior;
    
    @Size(max=10, message = " El numero Exterior es demasiado largo.")
    private String NumeroExterior;
    
    @Valid
    @NotBlank(message = "La colonia (y sus datos) es obligatorio.")
    public Colonia colonia;
    
    public Usuario usuario;

    public int getIdDireccion() {
        return IdDireccion;
    }

    public void setIdDireccion(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

    public Colonia getColonia() {
        return colonia;
    }

    public void setColonia(Colonia colonia) {
        this.colonia = colonia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    
}
