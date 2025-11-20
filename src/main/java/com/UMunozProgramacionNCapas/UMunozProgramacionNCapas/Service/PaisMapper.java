//package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service;
//
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.PaisJPA;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Pais;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PaisMapper {
//    
//    @Autowired
//     private ModelMapper modelMapper;
//
//    Result result = new Result();
//
//    public Pais toModel(PaisJPA paisJPA) {
//
//        try {
//
//            return modelMapper.map(paisJPA, Pais.class);
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
//    public PaisJPA toEntity(Pais pais){
//        
//        try{
//            
//            return modelMapper.map(pais, PaisJPA.class);
//            
//        }catch(Exception ex){
//            result.Correct = false;
//            result.ErrorMessage = ex.getLocalizedMessage();
//            return null;
//        }
//        
//    }
//
//}
