package com.cognizant.trms.service;

import com.cognizant.trms.dto.model.user.BusinessUnitDto;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.user.BusinessUnit;
import com.cognizant.trms.repository.user.BusinessUnitRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/*
    Author: Aravindan Dandapani
*/
@Component
public class BusinessUnitServiceImpl implements BusinessUnitService {
   @Autowired
   private BusinessUnitRepository businessUnitRepository;

   @Autowired
   private ModelMapper modelMapper;

    @Override
    public Set<BusinessUnitDto> getAllBusinessUnits() {

        return businessUnitRepository.findAll()
                .stream()
                .map(businessUnit -> modelMapper.map(businessUnit,BusinessUnitDto.class))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public BusinessUnitDto getBusinessUnitById(String Id) {
        Optional<BusinessUnit>  businessUnit = businessUnitRepository.findById(Id);
        if(businessUnit.isPresent()){
            return modelMapper.map(businessUnit.get(),BusinessUnitDto.class);
        }
        throw exceptionWithId(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, Id);
    }

    @Override
    public BusinessUnitDto getBusinessUnitByName(String buName) {

       Optional<BusinessUnit> businessUnit =  Optional.ofNullable(businessUnitRepository.findBybuName(buName));
       if(businessUnit.isPresent()){
           return modelMapper.map(businessUnit.get(),BusinessUnitDto.class);
       }
        throw exceptionWithId(EntityType.BUSINESSUNIT, ExceptionType.ENTITY_NOT_FOUND, buName);
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TRMSException.throwException(entityType, exceptionType, args);
    }

    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id, String... args) {
        return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
    }
}
