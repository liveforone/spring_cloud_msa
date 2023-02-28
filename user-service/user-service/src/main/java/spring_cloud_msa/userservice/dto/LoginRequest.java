package spring_cloud_msa.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "이메일은 반드시 기입해주세요.")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 반드시 작성해주세요.")
    private String password;
}
