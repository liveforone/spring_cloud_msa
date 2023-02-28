package spring_cloud_msa.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring_cloud_msa.userservice.domain.Users;

public interface UserRepository extends JpaRepository<Users, Long>, UserRepositoryCustom {
}
