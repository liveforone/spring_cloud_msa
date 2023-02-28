package spring_cloud_msa.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderRequest {

    private Long id;
    private String productId;
    private int stock;
    private int unitPrice;
    private int totalPrice;
    private Long userId;
}
