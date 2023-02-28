package spring_cloud_msa.orderservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {

    private Long id;
    private Long user_id;
    private String product_id;
    private int stock;
    private int unit_price;
    private int total_price;
}
