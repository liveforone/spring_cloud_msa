package spring_cloud_msa.catalogservice.repository;

import spring_cloud_msa.catalogservice.domain.Catalog;

public interface CatalogRepositoryCustom {

    Catalog findByProductId(String productId);

    void updateStockByProductId(int stock, String productId);
}
