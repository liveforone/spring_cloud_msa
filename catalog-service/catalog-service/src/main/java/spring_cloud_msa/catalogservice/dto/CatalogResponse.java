package spring_cloud_msa.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogResponse {

    private String productId;
    private String productName;
    private int stock;
    private int unitPrice;
    private int totalPrice;
    private Long orderId;
    private Long userId;
}
