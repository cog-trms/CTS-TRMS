package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.AccountCreationRequest;
import com.cognizant.trms.dto.model.user.AccountDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Set;

public interface AccountService {

    Set<AccountDto> getAllAccounts() throws JsonProcessingException;

    AccountDto findByaccountName(String accName) throws JsonProcessingException;

    AccountDto getAccountById(String Id);

    //AccountDto getAccountByBusinessUnitId(String Id);
    List<AccountDto> getAccountsByBusinessUnitId(String Id) throws JsonProcessingException;
    List<AccountDto> getAccountsByBusinessUnitName(String Name);

    AccountDto createAccount(AccountCreationRequest accountCreationRequest);

    AccountDto updateAccount(AccountCreationRequest accountCreationRequest) throws JsonProcessingException;
}
