package spring_cloud_msa.userservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring_cloud_msa.userservice.domain.QUsers;
import spring_cloud_msa.userservice.domain.Users;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public Users findOneById(Long id) {
        QUsers users = QUsers.users;

        return queryFactory.selectFrom(users)
                .where(users.id.eq(id))
                .fetchOne();
    }

    public Users findByEmail(String email) {
        QUsers users = QUsers.users;

        return queryFactory.selectFrom(users)
                .where(users.email.eq(email))
                .fetchOne();
    }
}
