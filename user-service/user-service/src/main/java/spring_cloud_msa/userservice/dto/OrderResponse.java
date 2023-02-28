package spring_cloud_msa.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private String productId;
    private int stock;
    private int unitPrice;
    private int totalPrice;
}
