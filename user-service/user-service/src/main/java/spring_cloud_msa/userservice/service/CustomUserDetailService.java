package spring_cloud_msa.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring_cloud_msa.userservice.domain.Users;
import spring_cloud_msa.userservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return createUserDetails(userRepository.findByEmail(email));
    }

    private UserDetails createUserDetails(Users users) {
        return User.builder()
                .username(users.getEmail())
                .password(users.getPassword())
                .roles("MEMBER")
                .build();
    }
}
