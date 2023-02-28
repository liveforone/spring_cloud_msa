package spring_cloud_msa.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spring_cloud_msa.userservice.dto.OrderResponse;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/{userId}/orders")
    List<OrderResponse> getOrders(@PathVariable("userId") Long userId);
}
