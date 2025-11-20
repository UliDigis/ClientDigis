///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service;
//
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.RolJPA;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Rol;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RolMapper {
//    
//    @Autowired
//    private ModelMapper modelMapper;        
//    
//    Result result = new Result();
//    
//    public Rol toModel(RolJPA rolJPA) {
//
//        try {
//            
//            return modelMapper.map(rolJPA, Rol.class);
//            
//        } catch (Exception ex) {
//            result.Correct = false;
//            result.ErrorMessage = ex.getLocalizedMessage();
//            result.ex = ex;
//            return null;
//        }
//
//    }
//    
//    public RolJPA toEntity(Rol rol){
//        
//        try{
//            
//            return modelMapper.map(rol,RolJPA.class);
//            
//        }catch(Exception ex){
//            result.Correct = false;
//            result.ErrorMessage = ex.getLocalizedMessage();
//            result.ex = ex;
//            return null;
//        }
//    }
//    
//}
