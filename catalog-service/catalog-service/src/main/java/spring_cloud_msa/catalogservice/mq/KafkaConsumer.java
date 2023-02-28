package spring_cloud_msa.catalogservice.mq;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_cloud_msa.catalogservice.domain.Catalog;
import spring_cloud_msa.catalogservice.repository.CatalogRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final CatalogRepository catalogRepository;

    @KafkaListener(topics = "minus-catalog-stock")
    @Transactional
    public void updateStock(String kafkaMessage) {
        log.info("Kafka Message -> " + kafkaMessage);

        JsonObject jsonMessage = JsonParser
                .parseString(kafkaMessage)
                .getAsJsonObject();
        
        Catalog catalog = catalogRepository.findByProductId(jsonMessage.get("productId").getAsString());
        if (catalog != null) {
            catalogRepository.updateStockByProductId(jsonMessage.get("stock").getAsInt(), catalog.getProductId());
        }
    }
}
