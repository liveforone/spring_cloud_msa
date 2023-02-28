package spring_cloud_msa.userservice.repository;

import spring_cloud_msa.userservice.domain.Users;

public interface UserRepositoryCustom {

    Users findOneById(Long id);

    Users findByEmail(String email);
}
