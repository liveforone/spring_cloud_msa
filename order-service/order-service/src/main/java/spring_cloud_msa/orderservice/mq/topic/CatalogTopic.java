package spring_cloud_msa.orderservice.mq.topic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CatalogTopic {

    MINUS_STOCK("minus-catalog-stock");

    private String value;
}
