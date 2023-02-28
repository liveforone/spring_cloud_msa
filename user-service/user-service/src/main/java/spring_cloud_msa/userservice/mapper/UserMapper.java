package spring_cloud_msa.userservice.mapper;

import spring_cloud_msa.userservice.domain.Users;
import spring_cloud_msa.userservice.dto.OrderResponse;
import spring_cloud_msa.userservice.dto.UserRequest;
import spring_cloud_msa.userservice.dto.UserResponse;
import spring_cloud_msa.userservice.utility.UserPassword;

import java.util.List;

public class UserMapper {

    public static Users dtoToEntity(UserRequest userRequest) {
        return Users.builder()
                .email(userRequest.getEmail())
                .user_name(userRequest.getUserName())
                .password(UserPassword.encodePassword(userRequest.getPassword()))
                .build();
    }

    public static UserResponse entityToDto(Users users, List<OrderResponse> orders) {
        return UserResponse.builder()
                .id(users.getId())
                .email(users.getEmail())
                .userName(users.getUser_name())
                .orders(orders)
                .build();
    }
}
