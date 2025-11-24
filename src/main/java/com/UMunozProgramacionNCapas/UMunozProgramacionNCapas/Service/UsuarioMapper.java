//package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service;
//
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.RolJPA;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.UsuarioJPA;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Rol;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Usuario;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UsuarioMapper {
//
//    @Autowired
//    private ModelMapper modelMapper;
//    Result result = new Result();
//
//    //ML aJPA
//    public Usuario toModel(UsuarioJPA usuarioJPA) {
//
//        try {
//            return modelMapper.map(usuarioJPA, Usuario.class);
//        } catch (Exception ex) {
//            
//            result.ErrorMessage = ex.getLocalizedMessage();
//            result.ex = ex;
//            return null;
//        }
//
//    }
//
//    //ML a JPA
//    public UsuarioJPA toEntity(Usuario usuarioML) {
//
//        try {
//
//            return modelMapper.map(usuarioML, UsuarioJPA.class);
//
//        } catch (Exception ex) {
//
//            result.ErrorMessage = ex.getLocalizedMessage();
//            result.ex = ex;
//            return null;
//        }
//
//    }
//
//}
