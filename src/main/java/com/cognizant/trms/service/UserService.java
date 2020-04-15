package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.UserProfileRequest;
import com.cognizant.trms.controller.v1.request.UserSignupRequest;
import com.cognizant.trms.dto.model.user.UserDto;
import com.cognizant.trms.dto.model.user.UserRoleDto;
import com.cognizant.trms.dto.model.user.UserRoleDtoMinimal;
import com.cognizant.trms.model.user.User;
import com.cognizant.trms.model.user.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Set;

/**
 * Aravindan Dandapani
 */
public interface UserService {
    /**
     * Register a new user
     *
     * @param userDto
     * @return
     */
    UserDto signup(UserDto userDto);

    /**
     * Search an existing user
     *
     * @param email
     * @return
     */
    UserDto findUserByEmail(String email);

    /**
     * Update profile of the user
     *
     * @param userProfileRequest
     * @return
     */
    UserDto updateProfile(UserProfileRequest userProfileRequest) throws JsonProcessingException;

    /**
     * Update password
     *
     * @param newPassword
     * @return
     */
    UserDto changePassword(UserDto userDto, String newPassword);

    Set<UserDto> listUsers() throws JsonProcessingException;
    boolean deleteUser(String id);

    List<UserRoleDtoMinimal> getUserByAccount(String accountId);
    List<UserRoleDtoMinimal> getUserByProgram(String programId);
    List<UserRoleDtoMinimal> getUserByTeam(String teamId);
    List<String> getUserRoleByUser(User user);
}
