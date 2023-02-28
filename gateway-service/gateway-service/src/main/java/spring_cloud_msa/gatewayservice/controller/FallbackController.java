package spring_cloud_msa.gatewayservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/user")
    public Mono<String> fallbackUser() {
        String errorMessage = "유저 서비스의 장애로 접근이 불가능합니다."
                + "\n이용에 불편을 드려 죄송합니다.";
        return Mono.just(errorMessage);
    }

    @GetMapping("/catalog")
    public Mono<String> fallbackCatalog() {
        String errorMessage = "상품 서비스의 장애로 접근이 불가능합니다."
                + "\n이용에 불편을 드려 죄송합니다.";
        return Mono.just(errorMessage);
    }

    @GetMapping("/order")
    public Mono<String> fallbackOrder() {
        String errorMessage = "주문 서비스의 장애로 접근이 불가능합니다."
                + "\n이용에 불편을 드려 죄송합니다.";
        return Mono.just(errorMessage);
    }
}
