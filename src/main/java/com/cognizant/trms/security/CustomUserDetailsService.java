package com.cognizant.trms.security;

import com.cognizant.trms.dto.model.user.RoleDto;
import com.cognizant.trms.dto.model.user.UserDto;
import com.cognizant.trms.model.user.User;
import com.cognizant.trms.repository.user.UserRepository;
import com.cognizant.trms.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Aravindan Dandapani
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger log = LogManager.getLogger(CustomUserDetailsService.class);
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;


//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        UserDto userDto = userService.findUserByEmail(email);
//        if (userDto != null) {
//            List<GrantedAuthority> authorities = getUserAuthority(userDto.getRoles());
//            return buildUserForAuthentication(userDto, authorities);
//        } else {
//            throw new UsernameNotFoundException("user with email " + email + " does not exist.");
//        }
//    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email.toLowerCase()));
        if (user.isPresent()) {
           List<String> userRoleList = userService.getUserRoleByUser(user.get());
           // log.debug("UserRole list " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userRoleList));
            List<GrantedAuthority> authorities = getUserAuthority(userRoleList);
            //log.debug("Authority list " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(authorities));
            return buildUserForAuthentication(user.get(), authorities);
        } else {
            throw new UsernameNotFoundException("user with email " + email + " does not exist.");
        }
    }


//    private List<GrantedAuthority> getUserAuthority(Set<RoleDto> userRoles) {
//        Set<GrantedAuthority> roles = new HashSet<>();
//        userRoles.forEach((role) -> {
//            roles.add(new SimpleGrantedAuthority(role.getRole()));
//        });
//        return new ArrayList<GrantedAuthority>(roles);
//    }

    private List<GrantedAuthority> getUserAuthority(List<String> userRoles) {
        List<GrantedAuthority> roles = userRoles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return new ArrayList<GrantedAuthority>(roles);
    }

//    private UserDetails buildUserForAuthentication(UserDto user, List<GrantedAuthority> authorities) {
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
//    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
