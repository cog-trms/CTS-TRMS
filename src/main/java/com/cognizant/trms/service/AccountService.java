package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.AccountCreationRequest;
import com.cognizant.trms.dto.model.user.AccountDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Set;

public interface AccountService {

    Set<AccountDto> getAllAccounts() throws JsonProcessingException;

    AccountDto findByaccountName(String accName) throws JsonProcessingException;

    AccountDto getAccountById(String Id);

    AccountDto getAccountByBusinessUnitId(String Id);

    AccountDto createAccount(AccountCreationRequest accountCreationRequest);

    AccountDto updateAccount(AccountCreationRequest accountCreationRequest);
}
