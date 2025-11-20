//package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service;
//
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.ColoniaJPA;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Colonia;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
//import jakarta.persistence.EntityManager;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ColoniaMapper {
//    
//    @Autowired
//    private EntityManager entityManager;
//    private ModelMapper modelMapper;
//    
//    Result result = new Result();
//    
//    public Colonia toModel(ColoniaJPA coloniaJPA){
//        
//        try{
//            
//            return modelMapper.map(coloniaJPA, Colonia.class);
//            
//        }catch(Exception ex){
//            result.Correct = false;
//            result.ErrorMessage = ex.getLocalizedMessage();
//            return null;
//        }
//        
//    }
//    
//    public ColoniaJPA toEntity(Colonia colonia){
//        
//        try{
//            
//            return modelMapper.map(colonia, ColoniaJPA.class);
//            
//        }catch(Exception ex){
//            
//            result.Correct = false;
//            result.ErrorMessage = ex.getLocalizedMessage();
//            return null;
//            
//        }
//        
//    }
//    
//}
