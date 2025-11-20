package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class Usuario {

    private int IdUsuario;
    @NotBlank(message = "El obligatorio colocar un User Name.")
    @Size(min = 2, max = 25, message = "El User Name del usuario debe de tener de 2 a 25 caracteres.")
    private String UserName;

    @NotBlank(message = "Es obligatorio el Nombre.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo debe contener letras.")
    private String Nombre;

    @NotBlank(message = "El Apellido Paterno es obligatorio.")
    private String ApellidoPaterno;
    private String ApellidoMaterno;

    @NotBlank(message = "El correo electronico es obligatorio.")
    @Email(message = "Formato de correco electronico invalido.")
    private String Email;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, message = "La contraseña debe de tener al menos 8 caracteres.")
    private String Password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date FechaNacimiento;

//  @NotBlank(message = "EL Sexo es obligatorio.")
    private char Sexo;

    @NotBlank(message = "El telefono es obligatorio.")
//    @Pattern(regexp = "\\d{10}", message = "El telefono debe ser un numero de 10 digitos.")
    private String Telefono;

//    @Pattern(regexp = "\\d{10}", message = "El celular debe ser un numero de 10 digitos.")
    private String Celular;

    @NotBlank(message = "El CURP es Obligatorio.")
//    @Pattern(regexp = "[A-Z]{4}[0-9]{6}[H|M][A-Z]{5}[0-9]{2}", message = "Formato de CURP invalido ")
    private String CURP;

    private String Imagen;

//    @NotBlank(message = "El Rol es obligatorio.")
    public Rol rol;

//    @Valid
//    @NotBlank(message = "Debe haver al menos una direccion.")
    public List<Direccion> Direcciones;

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String Imagen) {
        this.Imagen = Imagen;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String ApellidoPaterno) {
        this.ApellidoPaterno = ApellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String ApellidoMaterno) {
        this.ApellidoMaterno = ApellidoMaterno;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public Date getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(Date FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }

    public char getSexo() {
        return Sexo;
    }

    public void setSexo(char Sexo) {
        this.Sexo = Sexo;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }

    public String getCURP() {
        return CURP;
    }

    public void setCURP(String CURP) {
        this.CURP = CURP;
    }

    public List<Direccion> getDirecciones() {
        return Direcciones;
    }

    public void setDirecciones(List<Direccion> Direcciones) {
        this.Direcciones = Direcciones;
    }

}
