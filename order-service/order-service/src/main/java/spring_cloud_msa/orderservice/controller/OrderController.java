package spring_cloud_msa.orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_cloud_msa.orderservice.domain.Orders;
import spring_cloud_msa.orderservice.dto.OrderRequest;
import spring_cloud_msa.orderservice.mapper.OrderMapper;
import spring_cloud_msa.orderservice.mq.KafkaProducer;
import spring_cloud_msa.orderservice.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final Environment environment;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;

    @GetMapping("/health-check")
    public String status() {
        return "Port Num : " + environment.getProperty("local.server.port");
    }

    @PostMapping("/{userId}/order")
    public ResponseEntity<?> createOrder(
            @PathVariable("userId") Long userId,
            @RequestBody OrderRequest orderRequest
    ) {
        OrderRequest order = orderService.saveOrder(orderRequest, userId);

        kafkaProducer.send("minus-catalog-stock", order);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderRequest);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<?> getOrders(@PathVariable("userId") Long userId) {
        List<Orders> orders = orderService.getOrderByUserId(userId);

        return ResponseEntity.ok(OrderMapper.entityToDtoList(orders));
    }
}
