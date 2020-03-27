package com.cognizant.trms.service;

import com.cognizant.trms.dto.model.user.AccountDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Set;

public interface AccountService {

    Set<AccountDto> getAllAccounts() throws JsonProcessingException;
}