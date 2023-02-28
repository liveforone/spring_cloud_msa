package spring_cloud_msa.gatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalTrackerFilter extends AbstractGatewayFilterFactory<GlobalTrackerFilter.Config> {

    public GlobalTrackerFilter() {
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
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global filter base message: {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Global filter start: request id -> {}", request.getId());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                        if (config.isPostLogger()) {
                            log.info("Global filter end: response code -> {}", response.getStatusCode());
                        }
                    })
            );
        };
    }
}
