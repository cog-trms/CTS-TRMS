package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.UserProfileRequest;
import com.cognizant.trms.controller.v1.request.UserSignupRequest;
import com.cognizant.trms.dto.mapper.UserMapper;
import com.cognizant.trms.dto.mapper.UserRoleMapper;
import com.cognizant.trms.dto.model.user.UserDto;
import com.cognizant.trms.dto.model.user.UserRoleDto;
import com.cognizant.trms.dto.model.user.UserRoleDtoMinimal;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.model.user.*;
import com.cognizant.trms.repository.user.*;
import com.cognizant.trms.util.AuthUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Aravindan Dandapani
 */
@Component
public class UserServiceImpl implements UserService {
    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private TeamRepository teamRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public UserDto signup(UserDto userDto) {
        Role userRole;
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            if (userDto.isAdmin()) {
                userRole = roleRepository.findByRole(UserRoles.ADMIN.name());
            } else {
                userRole = roleRepository.findByRole(UserRoles.UN_ASSIGNED.name());
            }
            user = new User()
                    .setEmail(userDto.getEmail().toLowerCase())
                    .setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()))
                    .setRoles(new HashSet<>(Arrays.asList(userRole)))
                    .setFirstName(userDto.getFirstName().toLowerCase())
                    .setLastName(userDto.getLastName().toLowerCase())
                    .setMobileNumber(userDto.getMobileNumber());
            return UserMapper.toUserDto(userRepository.save(user));
        }
        throw exception(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, userDto.getEmail());
    }

    /**
     * Search an existing user
     *
     * @param email
     * @return
     */
    public UserDto findUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email.toLowerCase()));
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDto.class);
        }
        throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    /**
     * Update User Profile
     *
     * @param userProfileRequest
     * @return
     */
//    @Override
//    public UserDto updateProfile(UserDto userDto) {
//        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
//        if (user.isPresent()) {
//            User userModel = user.get();
//            userModel.setFirstName(userDto.getFirstName())
//                    .setLastName(userDto.getLastName())
//                    .setMobileNumber(userDto.getMobileNumber());
//            return UserMapper.toUserDto(userRepository.save(userModel));
//        }
//        throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, userDto.getEmail());
//    }
    @Override
    public UserDto updateProfile(UserProfileRequest userProfileRequest) throws JsonProcessingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.debug("Service Layer - Update profile " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userProfileRequest));

            Optional<User> user = Optional.ofNullable(userRepository.findByEmail(auth.getName()));
            if (user.isPresent()) {
                user.get().setFirstName((userProfileRequest.getFirstName() != null) ? userProfileRequest.getFirstName().toLowerCase() : user.get().getFirstName())
                        .setLastName((userProfileRequest.getLastName() != null) ? userProfileRequest.getLastName().toLowerCase() : user.get().getLastName())
                        .setMobileNumber((userProfileRequest.getMobileNumber() != null) ? userProfileRequest.getMobileNumber() : user.get().getMobileNumber());
                return UserMapper.toUserDto(userRepository.save(user.get()));
            }
            throw exceptionWithId(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, auth.getName());
        }
        throw exceptionWithId(EntityType.USER, ExceptionType.ACCESS_DENIED, "Invalid token");

    }

    /**
     * Change Password
     *
     * @param userDto
     * @param newPassword
     * @return
     */
    @Override
    public UserDto changePassword(UserDto userDto, String newPassword) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
        if (user.isPresent()) {
            User userModel = user.get();
            userModel.setPassword(bCryptPasswordEncoder.encode(newPassword));
            return UserMapper.toUserDto(userRepository.save(userModel));
        }
        throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, userDto.getEmail());
    }

    @Override
    public Set<UserDto> listUsers() throws JsonProcessingException {
        // log.debug("Service Layer - LIST ALL USERS "+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userRepository.findAll()));
        return userRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .map(userM -> UserMapper.toUserDto(userM))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public boolean deleteUser(String id) {
        if (AuthUtil.isAdmin()) {
            Optional<User> userProfile = userRepository.findById(id);
            if (userProfile.isPresent()) {
                userRepository.deleteById(id);
                return true;
            }
            throw exceptionWithId(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, id);
        }
        throw exceptionWithId(EntityType.USER, ExceptionType.ACCESS_DENIED, " Only an admin user can perform this operation");

    }

    @Override
    public List<UserRoleDtoMinimal> getUserByAccount(String accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            List<UserRole> userRoles = userRoleRepository.findByAccount(account.get());
            if (!userRoles.isEmpty()) {
                return userRoles
                        .stream()
                        //.filter(userRole -> userRole != null)
                        .map(userRole -> UserRoleMapper.toUserRoleDto(userRole))
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        }
        throw exception(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, accountId);
    }

    @Override
    public List<UserRoleDtoMinimal> getUserByProgram(String programId) {
        Optional<Program> program = programRepository.findById(programId);
        if (program.isPresent()) {
            List<UserRole> userRoles = userRoleRepository.findByProgram(program.get());
            if (!userRoles.isEmpty()) {
                return userRoles
                        .stream()
                        //.filter(userRole -> userRole != null)
                        .map(userRole -> UserRoleMapper.toUserRoleDto(userRole))
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        }
        throw exception(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, programId);
    }

    @Override
    public List<UserRoleDtoMinimal> getUserByTeam(String teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isPresent()) {
            List<UserRole> userRoles = userRoleRepository.findByTeam(team.get());
            if (!userRoles.isEmpty()) {
                return userRoles
                        .stream()
                        //.filter(userRole -> userRole != null)
                        .map(userRole -> UserRoleMapper.toUserRoleDto(userRole))
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        }
        throw exception(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, teamId);
    }

    /**
     * Returns a new RuntimeException
     *
     * @param entityType
     * @param exceptionType
     * @param args
     * @return
     */
    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TRMSException.throwException(entityType, exceptionType, args);
    }

    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id, String... args) {
        return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
    }
}
