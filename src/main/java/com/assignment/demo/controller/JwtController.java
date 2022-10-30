package com.assignment.demo.controller;

import com.assignment.demo.helper.JwtUtil;
import com.assignment.demo.model.JwtRequest;
import com.assignment.demo.model.JwtResponse;
import com.assignment.demo.model.User;
import com.assignment.demo.repo.UserRepository;
import com.assignment.demo.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class JwtController {

// This will check whether user name and password is valid or not.

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repo;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {

        System.out.println(jwtRequest);

        try{
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
        }
        catch (UsernameNotFoundException e){
            e.printStackTrace();
            throw new Exception("Bad Credentials");
        }

        catch (BadCredentialsException e){
            e.printStackTrace();
            throw new Exception("Bad Credentials");
        }

        // fine area
        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(jwtRequest.getUsername());

        String token = this.jwtUtil.generateToken(userDetails);
        System.out.println("JWT "+token);

        return ResponseEntity.ok(new JwtResponse(token));

    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody User user) throws Exception {

//        System.out.println(jwtRequest);

        User checkIfExists =  repo.findByEmail(user.getEmail());

//        if(repo.fin)
        if(checkIfExists==null) {

            repo.save(user);

            try {
                this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            } catch (BadCredentialsException e) {
                e.printStackTrace();
                throw new Exception("Bad Credentials");
            }

            // fine area
            UserDetails userDetails = this.customUserDetailService.loadUserByUsername(user.getEmail());

            String token = this.jwtUtil.generateToken(userDetails);
            System.out.println("JWT " + token);

            return ResponseEntity.ok(new JwtResponse(token));
        }
        throw new Exception("User already Registered");
    }

}
