package com.cognizant.trms.service;

import com.cognizant.trms.dto.mapper.AccountMapper;
import com.cognizant.trms.dto.model.user.AccountDto;
import com.cognizant.trms.dto.model.user.BusinessUnitDto;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.user.Account;
import com.cognizant.trms.model.user.BusinessUnit;
import com.cognizant.trms.repository.user.AccountRepository;
import com.cognizant.trms.repository.user.BusinessUnitRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/*
    Author: Aravindan Dandapani
*/
@Component
public class AccountServiceImpl implements AccountService {
    private static final Logger log = LogManager.getLogger(AccountServiceImpl.class);

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BusinessUnitRepository businessUnitRepository;


    @Override
    public Set<AccountDto> getAllAccounts() throws JsonProcessingException {
//        String reqString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountRepository.findAll());
//        log.debug("GET ALL ACCOUNTS " + reqString);
        return accountRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .map(account -> AccountMapper.toAccountDto(account))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public AccountDto findByaccountName(String accName) throws JsonProcessingException {
//        String reqString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountRepository.findByaccountName(accName));
//        log.debug("GET ACCOUNT BY NAME " + reqString);

        Optional<Account> account = Optional.ofNullable(accountRepository.findByaccountName(accName));
        if(account.isPresent()) {
            //return modelMapper.map(account.get(), AccountDto.class);
            return AccountMapper.toAccountDto(account.get());
        }
        throw exceptionWithId(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, accName);
    }

    @Override
    public AccountDto getAccountById(String Id) {
        Optional<Account> account = accountRepository.findById(Id);
        if(account.isPresent()) {
            return AccountMapper.toAccountDto(account.get());
        }
        throw exceptionWithId(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, Id);
    }

    @Override
    public AccountDto getAccountByBusinessUnitId(String Id) {
        Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(Id);
        if(businessUnit.isPresent()) {
            BusinessUnit bu = businessUnit.get();
         Optional<Account> account =  Optional.ofNullable(accountRepository.findByBusinessUnit(bu));
         if(account.isPresent()){
             return AccountMapper.toAccountDto(account.get());
         }
            throw  exceptionWithId(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, bu.getBuName());
        }
            throw exceptionWithId(EntityType.BUSINESSUNIT, ExceptionType.ENTITY_NOT_FOUND, Id );
    }



    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TRMSException.throwException(entityType, exceptionType, args);
    }

    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id, String... args) {
        return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
    }


}
