package com.elcom.id.service.impl;

import com.elcom.id.auth.CustomUserDetails;
import com.elcom.id.messaging.rabbitmq.model.User;
import com.elcom.id.repository.UserCustomizeRepository;
import com.elcom.id.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserCustomizeRepository userRepository;

    // JwtAuthenticationFilter sẽ sử dụng hàm này
    @Transactional
    @Override
    public UserDetails loadUserByUuid(String uuid) {
        long start = System.currentTimeMillis();
        User user = userRepository.findByUuid(uuid);
        long end = System.currentTimeMillis();
        LOGGER.info("loadUserByUuid >>> find by id: {} => In: {} ms", uuid, (end - start));
        if(user == null){
            throw new UsernameNotFoundException("User not found with uuid: " + uuid);
        }
        return new CustomUserDetails(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);
        if(user == null){
            throw new UsernameNotFoundException("User not found with username " + s);
        } else {
            LOGGER.info("Find user with username " + s + " ==> uuid " + user.getUuid());
        }
        return new CustomUserDetails(user);
    }
}
