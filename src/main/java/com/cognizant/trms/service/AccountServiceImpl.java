package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.AccountCreationRequest;
import com.cognizant.trms.dto.mapper.AccountMapper;
import com.cognizant.trms.dto.model.user.AccountDto;
import com.cognizant.trms.dto.model.user.BusinessUnitDto;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.user.*;
import com.cognizant.trms.repository.user.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.ls.LSInput;

import java.util.*;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;


    @Override
    public Set<AccountDto> getAllAccounts() throws JsonProcessingException {
        String reqString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountRepository.findAll());
        log.debug("GET ALL ACCOUNTS " + reqString);
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

        Optional<Account> account = Optional.ofNullable(accountRepository.findByaccountNameIgnoreCase(accName));
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
    public List<AccountDto> getAccountsByBusinessUnitId(String Id) throws JsonProcessingException {
        Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(Id);
        if (businessUnit.isPresent()) {
            BusinessUnit bu = businessUnit.get();
            List<Account> accounts = accountRepository.findByBusinessUnit(bu);
            String reqString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accounts);
        log.debug("GET ACCOUNT BY NAME " + reqString);

            if (!accounts.isEmpty()) {
                return accounts
                        .stream()
                        .filter(account -> account != null)
                        .map(account -> AccountMapper.toAccountDto(account))
                        .collect(Collectors.toList());
            }
            throw exceptionWithId(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, bu.getBuName());
        }
        throw exceptionWithId(EntityType.BUSINESSUNIT, ExceptionType.ENTITY_NOT_FOUND, Id);
    }

    @Override
    public List<AccountDto> getAccountsByBusinessUnitName(String Name) {
        BusinessUnit businessUnit = businessUnitRepository.findBybuNameIgnoreCase(Name);
        if (businessUnit != null) {

            List<Account> accounts = accountRepository.findByBusinessUnit(businessUnit);
            if (!accounts.isEmpty()) {
                return accounts
                        .stream()
                        .filter(account -> account != null)
                        .map(account -> AccountMapper.toAccountDto(account))
                        .collect(Collectors.toList());
            }
            throw exceptionWithId(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, businessUnit.getBuName());
        }
        throw exceptionWithId(EntityType.BUSINESSUNIT, ExceptionType.ENTITY_NOT_FOUND, Name);
    }

    @Override
    @Transactional
    public AccountDto createAccount(AccountCreationRequest accountCreationRequest) {
        String buId = accountCreationRequest.getBusinessUnitId();
        String accountName = accountCreationRequest.getAccountName();
        Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(accountCreationRequest.getBusinessUnitId());
        if (businessUnit.isPresent()) {
            BusinessUnit bu = businessUnit.get();
            Optional<Account> account = Optional.ofNullable(accountRepository.findByAccountNameAndBusinessUnit(accountName, bu));
            if (!account.isPresent()) {
                Optional<User> user = userRepository.findById(accountCreationRequest.getUserId());
                if (user.isPresent()) {

                    Account account1 = new Account()
                            .setAccountName(accountName)
                            .setBusinessUnit(bu)
                            .setHiringManger(user.get());
                    //.setUserId(user.get().getId());
                    //return modelMapper.map(accountRepository.save(account1),AccountDto.class);
                    account1 = accountRepository.save(account1);
                    createUserRoles(user.get(), account1);
                    return AccountMapper.toAccountDto(account1);
                }
                throw exceptionWithId(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, accountCreationRequest.getUserId());
            }
            throw exceptionWithId(EntityType.ACCOUNT, ExceptionType.DUPLICATE_ENTITY, accountName);
        }
        throw exceptionWithId(EntityType.BUSINESSUNIT, ExceptionType.ENTITY_NOT_FOUND, buId);
    }

    private void createUserRoles(User user, Account account) {
        Role role = roleRepository.findByRole(UserRoles.HIRING_MANAGER.name());
        UserRole userRole = new UserRole()
                //.setRoleId(role.getId())
                //.setUserId(user.getId())
                .setUser(user)
                .setAccount(account)
                .setRole(role);
        userRoleRepository.save(userRole);

        user.setRoles(new HashSet<>(Arrays.asList(role)));
        userRepository.save(user);
    }

    private void updateUserRoles(User user, Account account) throws JsonProcessingException {
        Role role = roleRepository.findByRole(UserRoles.HIRING_MANAGER.name());
        UserRole existingUserRole = userRoleRepository.findByRoleIdAndAccount(role.getId(), account);
        log.debug("Service Layer - Get User role per roleid account "+ objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(existingUserRole));

        if (existingUserRole == null) {
            UserRole userRole = new UserRole()
                    //.setRoleId(role.getId())
                    //.setUserId(user.getId())
                    .setUser(user)
                    .setAccount(account)
                    .setRole(role);
            userRoleRepository.save(userRole);

            user.setRoles(new HashSet<>(Arrays.asList(role)));
            userRepository.save(user);
        } else {
            //existingUserRole.setUserId(user.getId());
            existingUserRole.setUser(user);
            userRoleRepository.save(existingUserRole);
            user.setRoles(new HashSet<>(Arrays.asList(role)));
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public AccountDto updateAccount(AccountCreationRequest accountCreationRequest) throws JsonProcessingException {
        String buId = accountCreationRequest.getBusinessUnitId();
        String accountName = accountCreationRequest.getAccountName();
        String accountId = accountCreationRequest.getId();
        Optional<Account> existingAccount = accountRepository.findById(accountId);
        if (existingAccount.isPresent()) {
            Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(accountCreationRequest.getBusinessUnitId());
            if (businessUnit.isPresent()) {
                BusinessUnit bu = businessUnit.get();
                Optional<User> user = userRepository.findById(accountCreationRequest.getUserId());
                //UPDATE THE USER ROLE WITH HM
                if (user.isPresent()) {
//                    if (accountName.equalsIgnoreCase(existingAccount.get().getAccountName())) {
//                        existingAccount.get()
//                                .setAccountName(accountName)
//                                .setHiringManger(user.get())
//                                //.setUserId(user.get().getId())
//                                .setBusinessUnit(bu);
//                        return AccountMapper.toAccountDto(accountRepository.save(existingAccount.get()));
//                    } else {
//                        Optional<Account> accountWithName = Optional.ofNullable(accountRepository.findByAccountNameAndBusinessUnit(accountName, bu));
//                        if (!accountWithName.isPresent()) {
//                            existingAccount.get()
//                                    .setAccountName(accountName)
//                                    .setHiringManger(user.get())
//                                    //.setUserId(user.get().getId())
//                                    .setBusinessUnit(bu);
//                            return AccountMapper.toAccountDto(accountRepository.save(existingAccount.get()));
//                        }
//                        throw exceptionWithId(EntityType.ACCOUNT, ExceptionType.DUPLICATE_ENTITY, accountName,"Duplicate account name under the same BusinessUnit");
//                    }
                    if (!accountName.equalsIgnoreCase(existingAccount.get().getAccountName())) {
                        Optional<Account> accountWithName = Optional.ofNullable(accountRepository.findByAccountNameAndBusinessUnit(accountName, bu));
                        if (accountWithName.isPresent()) {
                            throw exceptionWithId(EntityType.ACCOUNT, ExceptionType.DUPLICATE_ENTITY, accountName, "Duplicate account name under the same BusinessUnit");
                        }
                    }
                    existingAccount.get()
                            .setAccountName(accountName)
                            .setHiringManger(user.get())
                            //.setUserId(user.get().getId())
                            .setBusinessUnit(bu);
                    Account newAccount = accountRepository.save(existingAccount.get());
                    updateUserRoles(user.get(), newAccount);
                    return AccountMapper.toAccountDto(newAccount);
                }
                throw exceptionWithId(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, accountCreationRequest.getUserId());
            }
            throw exceptionWithId(EntityType.BUSINESSUNIT, ExceptionType.ENTITY_NOT_FOUND, buId);
        }
        throw exceptionWithId(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, accountId);
    }


    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TRMSException.throwException(entityType, exceptionType, args);
    }

    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id, String... args) {
        return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
    }


}
