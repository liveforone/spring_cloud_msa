package spring_cloud_msa.userservice.controller;

import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import spring_cloud_msa.userservice.domain.Users;
import spring_cloud_msa.userservice.dto.LoginRequest;
import spring_cloud_msa.userservice.dto.UserRequest;
import spring_cloud_msa.userservice.dto.UserResponse;
import spring_cloud_msa.userservice.jwt.TokenInfo;
import spring_cloud_msa.userservice.service.UserService;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final Environment environment;
    private final UserService userService;

    @GetMapping("/health-check")
    @Timed(value = "user.status", longTask = true)
    public String status() {
        return "Port Num : " + environment.getProperty("local.server.port");
    }

    @GetMapping("/welcome")
    @Timed(value = "user.welcome", longTask = true)
    public String welcome() {
        return environment.getProperty("greeting.message");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        userService.singUp(userRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Create User Success");
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginPage() {
        return ResponseEntity.ok("로그인 페이지 입니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        try {
            TokenInfo token = userService.login(loginRequest);
            log.info("로그인 성공");

            response.addHeader("access-token", token.getAccessToken());
            response.addHeader("refresh-token", token.getRefreshToken());
            response.addHeader("email", loginRequest.getEmail());

            return ResponseEntity.ok("로그인에 성공하였습니다.");
        } catch (BadCredentialsException exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("이메일 혹은 비밀번호가 다릅니다.\n다시 시도하세요.");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<Users> users = userService.getAllUser();

        return ResponseEntity
                .ok(Objects.requireNonNullElse(users, "아직 회원이 없습니다."));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        UserResponse user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }
}
