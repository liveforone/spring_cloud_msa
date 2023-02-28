package spring_cloud_msa.userservice.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserPassword {

    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
