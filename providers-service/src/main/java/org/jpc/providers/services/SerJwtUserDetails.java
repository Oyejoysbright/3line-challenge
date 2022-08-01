package org.jpc.providers.services;

import java.util.ArrayList;
import java.util.Optional;

import org.jpc.providers.configs.JwtTokenUtil;
import org.jpc.providers.entities.Institution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.jpc.providers.repos.RepoInstitution;

@Service
@RequiredArgsConstructor
public class SerJwtUserDetails implements UserDetailsService {

    @Autowired
    RepoInstitution rCustomer;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    AuthenticationManager authenticationManager;

    public String createJwtToken(String username, String password) throws Exception {
        authenticate(username, password);

        final UserDetails userDetails = loadUserByUsername(username);
        String newToken = jwtTokenUtil.generateToken(userDetails);
        return newToken;
    }

    public String createRefreshJwtToken(String username, String password) throws Exception {
        authenticate(username, password);

        final UserDetails userDetails = loadUserByUsername(username);
        String newToken = jwtTokenUtil.generateRefreshToken(userDetails);
        return newToken;
    }

    public String createOtherToken(String username) throws Exception {

        final UserDetails userDetails = loadUserByUsername(username);
        String newToken = jwtTokenUtil.generateOtherToken(userDetails);
        return newToken;
    }

    public Boolean validateOtherToken(String username, String token) throws Exception {
        final UserDetails userDetails = loadUserByUsername(username);
        return jwtTokenUtil.validateOtherToken(token, userDetails);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Institution> customer = rCustomer.findByEmailAddressOrPhoneNumber(username, username);
        if (customer.isPresent()) {
            return new User(username, customer.get().getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);            
        }
    }
    
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));            
        } catch (DisabledException e) {
            throw new Exception("User is disabled");
        }
        catch (BadCredentialsException ex) {
            throw new Exception("Invalid Credentials");
        }
    }

}
