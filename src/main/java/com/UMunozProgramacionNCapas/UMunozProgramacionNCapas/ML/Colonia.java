package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Colonia {

    private int IdColonia;
    
    @NotBlank(message = "El nombre de la colonia es obligatorio.")
    @Size(max = 100, message = " El Nombre de la colonia es demasiado largo.")
    private String Nombre;
    
    @NotBlank(message = "El numero de la colonia es obligatorio.")
    @Size(max=10, message = "El codigo postal es demasiado largo.")
    private String CodigoPostal;
    
    private String NumeroInterior;
    
    private String NumeroExterior;
    
    @Valid
    @NotBlank(message = "El municipio (y sus datos) son obligatorios")
    public Municipio municipio;

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

    
    
    public int getIdColonia() {
        return IdColonia;
    }

    public void setIdColonia(int IdColonia) {
        this.IdColonia = IdColonia;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(String CodigoPostal) {
        this.CodigoPostal = CodigoPostal;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    
    
}

