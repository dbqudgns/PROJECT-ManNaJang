package com.culture.BAEUNDAY.jwt.Custom;

import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//AuthenticationManager를 통해 온 username이 DB에 존재하는지 찾아주는 클래스
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user != null) {
            return new CustomUserDetails(user);
        }

        return null;

    }
}
