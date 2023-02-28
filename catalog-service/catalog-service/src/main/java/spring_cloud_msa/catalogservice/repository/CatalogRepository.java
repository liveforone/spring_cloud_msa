package spring_cloud_msa.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring_cloud_msa.catalogservice.domain.Catalog;

public interface CatalogRepository extends JpaRepository<Catalog, Long>, CatalogRepositoryCustom {

    Catalog findByProductId(String productId);

    @Modifying
    @Query("update Catalog c set c.stock = c.stock - :stock where c.productId = :productId")
    void updateStockByProductId(@Param("stock") int stock, @Param("productId") String productId);
}
