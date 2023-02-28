package spring_cloud_msa.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_cloud_msa.userservice.client.OrderServiceClient;
import spring_cloud_msa.userservice.domain.Users;
import spring_cloud_msa.userservice.dto.LoginRequest;
import spring_cloud_msa.userservice.dto.OrderResponse;
import spring_cloud_msa.userservice.dto.UserRequest;
import spring_cloud_msa.userservice.dto.UserResponse;
import spring_cloud_msa.userservice.jwt.TokenInfo;
import spring_cloud_msa.userservice.jwt.TokenProvider;
import spring_cloud_msa.userservice.mapper.UserMapper;
import spring_cloud_msa.userservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final OrderServiceClient orderServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public UserResponse getUserById(Long id) {
        Users users = userRepository.findOneById(id);

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("order-service-breaker");
        List<OrderResponse> orders = circuitBreaker.run(
                () -> orderServiceClient.getOrders(id),
                throwable -> new ArrayList<>()
        );

        return UserMapper.entityToDto(users, orders);
    }

    public List<Users> getAllUser() {
        return userRepository.findAll();
    }

    @Transactional
    public void singUp(UserRequest userRequest) {
        userRepository.save(UserMapper.dtoToEntity(userRequest));
    }

    @Transactional
    public TokenInfo login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);

        return tokenProvider
                .generateToken(authentication);
    }
}
