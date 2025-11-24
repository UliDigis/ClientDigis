//package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service;
//
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Municipio;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.MunicipioJPA;
//import jakarta.persistence.EntityManager;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class MunicipioMapper {
//
//    @Autowired
//    private ModelMapper modelMapper;
//    
//    
//    Result result = new Result();
//    public Municipio toModel(MunicipioJPA municipioJPA){
//        
//        try{
//            return modelMapper.map(municipioJPA,Municipio.class);
//            
//        }catch(Exception ex){
//            result.Correct = false;
//            result.ErrorMessage = ex.getLocalizedMessage();
//            return null;
//        }
//        
//    }
//    
//    public MunicipioJPA toEntity(Municipio municipio){
//        
//        try{
//            return modelMapper.map(municipio, MunicipioJPA.class);
//        }catch(Exception ex){
//           result.Correct = false;
//           result.ErrorMessage = ex.getLocalizedMessage();
//           return null;
//        }
//        
//    }
//
//    
//    
//}
