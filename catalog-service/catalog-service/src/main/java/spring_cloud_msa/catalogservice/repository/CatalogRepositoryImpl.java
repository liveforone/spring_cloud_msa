package spring_cloud_msa.catalogservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring_cloud_msa.catalogservice.domain.Catalog;
import spring_cloud_msa.catalogservice.domain.QCatalog;

@Repository
@RequiredArgsConstructor
public class CatalogRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public Catalog findByProductId(String productId) {
        QCatalog catalog = QCatalog.catalog;

        return queryFactory.selectFrom(catalog)
                .where(catalog.productId.eq(productId))
                .fetchOne();
    }

    public void updateStockByProductId(int stock, String productId) {
        QCatalog catalog = QCatalog.catalog;

        queryFactory.update(catalog)
                .set(catalog.stock, catalog.stock.add(-stock))
                .execute();
    }
}
