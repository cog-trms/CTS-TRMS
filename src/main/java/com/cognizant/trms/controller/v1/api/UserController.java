package com.cognizant.trms.controller.v1.api;

import com.cognizant.trms.controller.v1.request.UserProfileRequest;
import com.cognizant.trms.controller.v1.request.UserSignupRequest;
import com.cognizant.trms.dto.model.user.SpringUser;
import com.cognizant.trms.dto.model.user.UserDto;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.repository.user.UserRepository;
import com.cognizant.trms.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

/**
 * Aravindan Dandapani
 */

@RestController
@RequestMapping("/api/v1/users")
@Api(value="trms-application", description="Operations pertaining to user management in the TRMS application")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
   private ObjectMapper mapper;

    /**
     * Handles the incoming POST API "/v1/user/signup"
     *
     * @param userSignupRequest
     * @return
     */
    @PostMapping("/signup")
    public Response signup(@RequestBody @Valid UserSignupRequest userSignupRequest) throws JsonProcessingException {
        log.info("user signup");

        String reqString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userSignupRequest);
        log.debug("Request object " + reqString);
        return Response.ok().setPayload(registerUser(userSignupRequest, false));
    }

    @GetMapping("/listUsers")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response listUsers() throws JsonProcessingException {
        log.debug("Controller layer - LIST ALL USERS "+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userService.listUsers()));
        return Response
                .ok()
                .setPayload(userService.listUsers());
    }

    @GetMapping("/account/{accountId}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getUsersByAccount(@PathVariable ("accountId") String accountId) throws JsonProcessingException {
        return Response
                .ok()
                .setPayload(userService.getUserByAccount(accountId));
    }
    @GetMapping("/program/{programId}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getUsersByProgram(@PathVariable ("programId") String programId) throws JsonProcessingException {
        return Response
                .ok()
                .setPayload(userService.getUserByProgram(programId));
    }

    @GetMapping("/team/{teamId}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getUsersByTeam(@PathVariable ("teamId") String teamId) throws JsonProcessingException {
        return Response
                .ok()
                .setPayload(userService.getUserByTeam(teamId));
    }

    @GetMapping("/getTokenDetails")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getTokenDetails() throws JsonProcessingException {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       SpringUser user = new SpringUser();
       user.setUsername(principal.toString());
        List<String> roles = new ArrayList<>();
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().forEach(authority -> roles.add(authority.getAuthority()));
        log.debug("GET TOKEN - ROLES LIST "+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(roles));

        user.setAuthorities(roles);
     return Response
                .ok()
                .setPayload(user);
    }

    @GetMapping("/profile")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getProfile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Response.ok().setPayload(userService.findUserByEmail(auth.getName()));
    }

    @PatchMapping("/profile")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response patchProfile(@RequestBody UserProfileRequest userProfileRequest) throws JsonProcessingException {
        log.debug("Controller Layer - Update profile "+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userProfileRequest));

       return Response
               .ok()
               .setPayload(updateUserProfile(userProfileRequest));
    }

    private UserDto updateUserProfile(UserProfileRequest userProfileRequest) throws JsonProcessingException {
        return userService.updateProfile(userProfileRequest);
    }

    @DeleteMapping("/profile/{id}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response deleteProfile(@PathVariable("id") String id){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
//        boolean authorized = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
        return Response
                .ok()
                .setPayload(userService.deleteUser(id));
    }





//    @GetMapping("/logout")
//    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
//    public Response logout(HttpServletRequest request, HttpServletResponse response){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null){
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//       // SecurityContextHolder.getContext().setAuthentication(null);
//        return Response.ok();
//    }

    /**
     * Register a new user in the database
     *
     * @param userSignupRequest
     * @return
     */
    private UserDto registerUser(UserSignupRequest userSignupRequest, boolean isAdmin) {
        UserDto userDto = new UserDto()
                .setEmail(userSignupRequest.getEmail())
                .setPassword(userSignupRequest.getPassword())
                .setFirstName(userSignupRequest.getFirstName())
                .setLastName(userSignupRequest.getLastName())
                .setMobileNumber(userSignupRequest.getMobileNumber())
                .setAdmin(isAdmin);

        return userService.signup(userDto);
    }
}
