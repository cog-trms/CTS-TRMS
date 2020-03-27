package com.cognizant.trms.service;

import com.cognizant.trms.dto.model.user.BusinessUnitDto;

import java.util.Set;

public interface BusinessUnitService {
    Set<BusinessUnitDto> getAllBusinessUnits();

    BusinessUnitDto getBusinessUnitById(String Id);

    BusinessUnitDto getBusinessUnitByName(String buName);

}
