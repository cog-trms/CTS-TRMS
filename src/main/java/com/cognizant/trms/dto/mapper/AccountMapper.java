package com.cognizant.trms.dto.mapper;

import com.cognizant.trms.dto.model.user.AccountDto;
import com.cognizant.trms.dto.model.user.BusinessUnitDto;
import com.cognizant.trms.dto.model.user.ProgramDto;
import com.cognizant.trms.dto.model.user.UserDto;
import com.cognizant.trms.model.user.Account;
import com.cognizant.trms.model.user.Program;

import org.modelmapper.ModelMapper;


/*
    Author: Aravindan Dandapani
*/
public class AccountMapper {

    public static AccountDto toAccountDto(Account account){
        //returns the full user and bu object in the response
//        return new AccountDto()
//                .setId(account.getId())
//                .setAccountName(account.getAccountName())
//                .setBusinessUnit(new ModelMapper().map(account.getBusinessUnit(),BusinessUnitDto.class))
//                .setUser(new ModelMapper().map(account.getUser(), UserDto.class));

        return  new AccountDto()
                .setId(account.getId())
                .setAccountName(account.getAccountName())
                .setBusinessUnit(new ModelMapper().map(account.getBusinessUnit(),BusinessUnitDto.class))
               // .setUser(UserMapper.toUserDto(account.getUser()))
                //.setUserId(account.getUserId())
                .setHiringManger(new ModelMapper().map(account.getHiringManger(), UserDto.class));

    }
}
