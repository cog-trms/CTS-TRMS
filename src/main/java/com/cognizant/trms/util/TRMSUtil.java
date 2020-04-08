package com.cognizant.trms.util;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

/*
    Author: Aravindan Dandapani
*/
public class TRMSUtil {

    public final static boolean isAdmin(){
        boolean isAdmin = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        return isAdmin = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
    }

    public final static boolean validateRequestBody(Errors errors){

        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {
            // get all errors
            String error = errors.getAllErrors()
                    .stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(","));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,error);
        }
        return true;
    }
}
