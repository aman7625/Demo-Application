package com.assignment.demo.services;

import com.assignment.demo.model.CustomUserDetails;
import com.assignment.demo.model.User;
import com.assignment.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        final User user = this.userRepository.findByEmail(userName);

        if(user==null){
            throw new UsernameNotFoundException("User not found");
        }
        else{
            return new CustomUserDetails(user);
        }
    }
}
