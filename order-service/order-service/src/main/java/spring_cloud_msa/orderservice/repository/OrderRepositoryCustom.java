package spring_cloud_msa.orderservice.repository;

import spring_cloud_msa.orderservice.domain.Orders;

import java.util.List;

public interface OrderRepositoryCustom {

    Orders findOneById(Long id);

    List<Orders> findByUserId(Long userId);
}
