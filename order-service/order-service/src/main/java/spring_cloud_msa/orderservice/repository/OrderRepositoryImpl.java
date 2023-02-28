package spring_cloud_msa.orderservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring_cloud_msa.orderservice.domain.Orders;
import spring_cloud_msa.orderservice.domain.QOrders;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public Orders findOneById(Long id) {
        QOrders orders = QOrders.orders;

        return queryFactory.selectFrom(orders)
                .where(orders.id.eq(id))
                .fetchOne();
    }

    public List<Orders> findByUserId(Long userId) {
        QOrders orders = QOrders.orders;

        return queryFactory.selectFrom(orders)
                .where(orders.userId.eq(userId))
                .fetch();
    }
}
