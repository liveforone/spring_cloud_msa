package spring_cloud_msa.orderservice.mq;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spring_cloud_msa.orderservice.dto.OrderRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, OrderRequest orderRequest) {
        Gson gson = new Gson();
        String jsonOrder = gson.toJson(orderRequest);

        kafkaTemplate.send(topic, jsonOrder);
        log.info("Kafka producer sent data from order-service");
    }
}
