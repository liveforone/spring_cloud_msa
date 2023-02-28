package spring_cloud_msa.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_cloud_msa.orderservice.domain.Orders;
import spring_cloud_msa.orderservice.dto.OrderRequest;
import spring_cloud_msa.orderservice.mapper.OrderMapper;
import spring_cloud_msa.orderservice.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Orders> getOrderByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public OrderRequest saveOrder(OrderRequest orderRequest, Long userId) {
        orderRequest.setTotalPrice(orderRequest.getUnitPrice() * orderRequest.getStock());
        orderRequest.setUserId(userId);
        orderRepository.save(OrderMapper.dtoToEntity(orderRequest));

        return orderRequest;
    }
}
