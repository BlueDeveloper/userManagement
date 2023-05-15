package user.mngm.usermanagement.jpa.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import user.mngm.usermanagement.jpa.user.dto.QUserSearchDto;
import user.mngm.usermanagement.jpa.user.dto.UserSearchDto;
import user.mngm.usermanagement.jpa.user.entity.QUserEntity;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;

import java.util.List;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {
    @Autowired
    private JPAQueryFactory queryFactory;
    private final QUserEntity qUserEntity =  QUserEntity.userEntity;

    public UserRepositoryImpl() {
        super(UserEntity.class);
    }

    @Override
    public Page<UserSearchDto> findBySearchOption(Pageable pageable, String memberId, String name, String email) {
        JPQLQuery<UserSearchDto> result = queryFactory.select(new QUserSearchDto(qUserEntity.memberId, qUserEntity.name, qUserEntity.email, qUserEntity.stat, qUserEntity.loginDat))
                .from(qUserEntity)
                .where(containMemberId(memberId), containName(name), containEmail(email), qUserEntity.grade.eq("ROLE_USER"));
        List<UserSearchDto> content = this.getQuerydsl().applyPagination(pageable, result).fetch();
        Long total = result.fetchCount();
        return new PageImpl<UserSearchDto>(content, pageable, total);
    }


    private BooleanExpression containMemberId(String memberId) {
        if (memberId == null || memberId.isEmpty()) {
            return null;
        }
        return qUserEntity.memberId.containsIgnoreCase(memberId);
    }

    private BooleanExpression containName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return qUserEntity.name.containsIgnoreCase(name);
    }

    private BooleanExpression containEmail(String gu) {
        if (gu == null || gu.isEmpty()) {
            return null;
        }
        return qUserEntity.email.containsIgnoreCase(gu);
    }

}
