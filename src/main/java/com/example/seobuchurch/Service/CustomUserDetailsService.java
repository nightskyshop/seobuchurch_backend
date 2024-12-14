package com.example.seobuchurch.Service;

import com.example.seobuchurch.Entity.UserEntity;
import com.example.seobuchurch.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + "을 DB에서 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(UserEntity userEntity) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userEntity.getAuthority().toString());
        return new User(
                String.valueOf(userEntity.getId()),
                userEntity.getPassword(),
                Collections.singletonList(grantedAuthority)
        );
    }
}
