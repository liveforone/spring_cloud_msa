package spring_cloud_msa.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring_cloud_msa.orderservice.domain.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long>, OrderRepositoryCustom {
}
