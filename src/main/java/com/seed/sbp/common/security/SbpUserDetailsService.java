package com.seed.sbp.common.security;

import com.seed.sbp.common.response.CommonResultCode;
import com.seed.sbp.user.domain.User;
import com.seed.sbp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SbpUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(CommonResultCode.NOT_FOUND_USER.getMessage()));
        return new SbpUserDetails(user);
    }
}
