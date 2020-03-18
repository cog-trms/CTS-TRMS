package com.cognizant.trms.controller.v1.api;

import com.cognizant.trms.controller.v1.request.UserSignupRequest;
import com.cognizant.trms.dto.model.user.SpringUser;
import com.cognizant.trms.dto.model.user.UserDto;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Aravindan Dandapani
 */
@RestController
@RequestMapping("/api/v1/user")
@Api(value="trms-application", description="Operations pertaining to user management in the TRMS application")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Handles the incoming POST API "/v1/user/signup"
     *
     * @param userSignupRequest
     * @return
     */
    @PostMapping("/signup")
    public Response signup(@RequestBody @Valid UserSignupRequest userSignupRequest) {
        return Response.ok().setPayload(registerUser(userSignupRequest, false));
    }

    @GetMapping("/listUsers")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response listUsers(){
        return Response
                .ok()
                .setPayload(userService.listUsers());
    }

    @GetMapping("/getTokenDetails")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getTokenDetails(){
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       SpringUser user = new SpringUser();
       user.setUsername(principal.toString());
        List<String> roles = new ArrayList<>();
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().forEach(authority -> roles.add(authority.getAuthority()));
        user.setAuthorities(roles);
     return Response
                .ok()
                .setPayload(user);

    }

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
