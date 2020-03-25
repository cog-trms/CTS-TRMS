package com.cognizant.trms.controller.v1.api;

import com.cognizant.trms.controller.v1.request.UserProfileRequest;
import com.cognizant.trms.controller.v1.request.UserSignupRequest;
import com.cognizant.trms.dto.model.user.SpringUser;
import com.cognizant.trms.dto.model.user.UserDto;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.model.user.User;
import com.cognizant.trms.repository.user.UserRepository;
import com.cognizant.trms.service.UserService;
import com.sun.deploy.net.HttpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * Aravindan Dandapani
 */
@RestController
@RequestMapping("/api/v1/user")
@Api(value="trms-application", description="Operations pertaining to user management in the TRMS application")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/profile")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getProfile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Response.ok().setPayload(userService.findUserByEmail(auth.getName()));
    }

    @PatchMapping("/profile")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response patchProfile(UserProfileRequest userProfileRequest){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userProfile = userService.findUserByEmail(auth.getName());
        userProfile.setFirstName(userProfileRequest.getFirstName())
                .setLastName(userProfileRequest.getLastName())
                .setMobileNumber(userProfileRequest.getMobileNumber());
       return Response.ok().setPayload(userService.updateProfile(userProfile));
    }


    @DeleteMapping("/profile/{id}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response deleteProfile(@PathVariable("id") String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        boolean authorized = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
        System.out.println("Authorized :"+ authorized);
        if(authorized) {
            Optional<User> userProfile = userRepository.findById(id);
            if (userProfile.isPresent()) {
                userRepository.deleteById(id);
                return Response.ok();
            } else {
                return Response.notFound();
            }
        }
        return Response.accessDenied();
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
