package spring_cloud_msa.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "이메일은 반드시 작성하여야합니다.")
    @Email
    private String email;

    @NotBlank(message = "이름은 반드시 작성하여야합니다.")
    private String userName;

    @Size(min = 8, message = "비밀번호는 8자리 이사이어야합니다.")
    private String password;
}
