package com.cognizant.trms.service;

import com.cognizant.trms.dto.model.user.UserDto;
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
     * @param userDto
     * @return
     */
    UserDto updateProfile(UserDto userDto);

    /**
     * Update password
     *
     * @param newPassword
     * @return
     */
    UserDto changePassword(UserDto userDto, String newPassword);

    Set<UserDto> listUsers() throws JsonProcessingException;
}
