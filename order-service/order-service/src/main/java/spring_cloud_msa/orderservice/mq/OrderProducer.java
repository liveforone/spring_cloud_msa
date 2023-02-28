package spring_cloud_msa.orderservice.mq;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spring_cloud_msa.orderservice.dto.*;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fields = Arrays.asList(
            new Field("string", true, "product_id"),
            new Field("int32", true, "stock"),
            new Field("int32", true, "unit_price"),
            new Field("int32", true, "total_price"),
            new Field("bigint", true, "user_id")
    );

    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("order")
            .build();

    public void send(String topic, OrderRequest orderRequest) {
        Payload payload = Payload.builder()
                .user_id(orderRequest.getUserId())
                .product_id(orderRequest.getProductId())
                .stock(orderRequest.getStock())
                .unit_price(orderRequest.getUnitPrice())
                .total_price(orderRequest.getTotalPrice())
                .build();

        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(schema, payload);

        Gson gson = new Gson();
        String jsonOrder = gson.toJson(kafkaOrderDto);

        kafkaTemplate.send(topic, jsonOrder);
    }
}
