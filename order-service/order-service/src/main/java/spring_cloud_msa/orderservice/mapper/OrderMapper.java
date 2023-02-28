package spring_cloud_msa.orderservice.mapper;

import spring_cloud_msa.orderservice.domain.Orders;
import spring_cloud_msa.orderservice.dto.OrderRequest;
import spring_cloud_msa.orderservice.dto.OrderResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Orders dtoToEntity(OrderRequest orderRequest) {
        return Orders.builder()
                .productId(orderRequest.getProductId())
                .stock(orderRequest.getStock())
                .unitPrice(orderRequest.getUnitPrice())
                .totalPrice(orderRequest.getTotalPrice())
                .userId(orderRequest.getUserId())
                .build();
    }

    public static OrderResponse entityToDto(Orders orders) {
        return OrderResponse.builder()
                .id(orders.getId())
                .productId(orders.getProductId())
                .stock(orders.getStock())
                .unitPrice(orders.getUnitPrice())
                .totalPrice(orders.getTotalPrice())
                .build();
    }

    public static List<OrderResponse> entityToDtoList(List<Orders> orders) {
        return orders
                .stream()
                .map(OrderMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
