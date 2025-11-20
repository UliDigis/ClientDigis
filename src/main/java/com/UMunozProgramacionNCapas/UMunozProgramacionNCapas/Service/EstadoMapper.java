//package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service;
//
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.EstadoJPA;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Estado;
//import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.ML.Result;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EstadoMapper {
//    @Autowired
//    private ModelMapper modelMapper;        
//            
//    Result result = new Result();
//    public Estado toModel(EstadoJPA estadoJPA){
//        
//        try{
//            
//            return modelMapper.map(estadoJPA, Estado.class);
//            
//        }catch(Exception ex){
//            result.Correct = false;
//            result.ErrorMessage = ex.getLocalizedMessage();
//            return null;
//        }
//        
//    }
//    
//    public EstadoJPA toEntity(Estado estado){
//        
//        try{
//            return modelMapper.map(estado, EstadoJPA.class);
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
