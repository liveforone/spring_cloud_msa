package spring_cloud_msa.gatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter() {
        super(Config.class);
    }

    public static class Config {
        //configuration 존재하면 넣기
    }

    @Override
    public GatewayFilter apply(Config config) {
        //custom pre filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom Pre filter: request id -> {}", request.getId());

            //custom post filter
            return chain.filter(exchange).then(Mono.fromRunnable(() ->
                    log.info("Custom Post filter: response code -> {}", response.getStatusCode()
                    )
            ));
        };
    }
}
