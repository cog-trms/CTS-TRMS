package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.BURequest;
import com.cognizant.trms.dto.model.user.BusinessUnitDto;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.user.BusinessUnit;
import com.cognizant.trms.repository.user.BusinessUnitRepository;
import com.cognizant.trms.util.TRMSUtil;
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

       Optional<BusinessUnit> businessUnit =  Optional.ofNullable(businessUnitRepository.findBybuNameIgnoreCase(buName));
       if(businessUnit.isPresent()){
           return modelMapper.map(businessUnit.get(),BusinessUnitDto.class);
       }
        throw exceptionWithId(EntityType.BUSINESSUNIT, ExceptionType.ENTITY_NOT_FOUND, buName);
    }

    @Override
    public BusinessUnitDto saveBU(BURequest buRequest) {
        String buName = buRequest.getBusinessUnitName();
       BusinessUnit businessUnit = businessUnitRepository.findBybuNameIgnoreCase(buName);
       if (businessUnit == null) {
           BusinessUnit businessUnit1 = new BusinessUnit()
                   .setBuName(buName);
           return modelMapper.map(businessUnitRepository.save(businessUnit1), BusinessUnitDto.class);
       }
        throw exceptionWithId(EntityType.BUSINESSUNIT, ExceptionType.DUPLICATE_ENTITY, buName);
    }

    @Override
    public BusinessUnitDto updateBU(BusinessUnitDto businessUnitDto) {
        String id = businessUnitDto.getId();
        Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(id);
        if (businessUnit.isPresent()) {
            businessUnit.get().setBuName(businessUnitDto.getBuName());
            return modelMapper.map(businessUnitRepository.save(businessUnit.get()), BusinessUnitDto.class);
        }
        throw exceptionWithId(EntityType.BUSINESSUNIT, ExceptionType.ENTITY_NOT_FOUND, id);
    }

    @Override
    public Boolean deleteBU(String id) {
        if(TRMSUtil.isAdmin()){
            Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(id);
            if(businessUnit.isPresent()){
                businessUnitRepository.deleteById(id);
                return true;
            }
            throw exceptionWithId(EntityType.BUSINESSUNIT, ExceptionType.ENTITY_NOT_FOUND, id);
        }
        throw exceptionWithId(EntityType.BUSINESSUNIT, ExceptionType.ACCESS_DENIED, "Only an admin user can perform this operation");
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TRMSException.throwException(entityType, exceptionType, args);
    }

    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id, String... args) {
        return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
    }
}
