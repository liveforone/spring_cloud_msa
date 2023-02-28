package spring_cloud_msa.gatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Data
    public static class Config {
        //configuration 존재하면 넣기
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging filter base message: {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Logging Pre filter: request id -> {}", request.getId());
            }

            //custom post filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                        if (config.isPostLogger()) {
                            log.info("Logging Post filter: response code -> {}", response.getStatusCode());
                        }
                    })
            );
        }, Ordered.HIGHEST_PRECEDENCE);
        /*
        * Ordered 로 필터의 순서를 걸 수 있게된다.
        * custom 필터를 만들때 순서를 걸고 싶다면 order 필터를 사용해 순서를 걸어주면 된다.
         */
    }
}
