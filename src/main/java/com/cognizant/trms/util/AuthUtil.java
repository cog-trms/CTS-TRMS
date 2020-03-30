package com.cognizant.trms.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/*
    Author: Aravindan Dandapani
*/
public class AuthUtil {

    public final static boolean isAdmin(){
        boolean isAdmin = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        return isAdmin = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
    }
}
