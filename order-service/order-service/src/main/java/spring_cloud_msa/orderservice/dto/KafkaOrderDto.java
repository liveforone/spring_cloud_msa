package spring_cloud_msa.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaOrderDto {

    private Schema schema;
    private Payload payload;
}
