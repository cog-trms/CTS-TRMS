package com.cognizant.trms.service;

import com.cognizant.trms.dto.mapper.AccountMapper;
import com.cognizant.trms.dto.model.user.AccountDto;
import com.cognizant.trms.repository.user.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
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


    @Override
    public Set<AccountDto> getAllAccounts() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String reqString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountRepository.findAll());

        log.debug("GET ALL ACCOUNTS " + reqString);
        return accountRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .map(account -> AccountMapper.toAccountDto(account))
                .collect(Collectors.toCollection(TreeSet::new));

    }
}
