package user.mngm.usermanagement.jpa.admin.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import user.mngm.usermanagement.common.utils.Utils;
import user.mngm.usermanagement.jpa.admin.dto.AccessLogSearchDto;
import user.mngm.usermanagement.jpa.admin.dto.QAccessLogSearchDto;
import user.mngm.usermanagement.jpa.admin.entity.QAccessLogEntity;
import user.mngm.usermanagement.jpa.user.dto.QUserSearchDto;
import user.mngm.usermanagement.jpa.user.dto.UserSearchDto;
import user.mngm.usermanagement.jpa.user.entity.QUserEntity;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;
import user.mngm.usermanagement.jpa.user.repository.UserRepositoryCustom;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AccessLogRepositoryImpl extends QuerydslRepositorySupport implements AccessLogRepositoryCustom {
    @Autowired
    private JPAQueryFactory queryFactory;
    private final QAccessLogEntity qAccessLogEntity = QAccessLogEntity.accessLogEntity;

    public AccessLogRepositoryImpl() {
        super(UserEntity.class);
    }

    @Override
    public Page<AccessLogSearchDto> findBySearchOption(Pageable pageable, String rHost, String accDatStart, String accDateEnd, HttpServletRequest request) {
        JPQLQuery<AccessLogSearchDto> result = result = queryFactory.select(new QAccessLogSearchDto(qAccessLogEntity.rHost, qAccessLogEntity.rUri, qAccessLogEntity.accDat)).from(qAccessLogEntity)
                .where(containAccessIp(rHost),betweenAccDate(accDatStart, accDateEnd), ignoreMyIp(request)).orderBy(qAccessLogEntity.accDat.desc());;
        List<AccessLogSearchDto> content = this.getQuerydsl().applyPagination(pageable, result).fetch();
        Long total = result.fetchCount();
        return new PageImpl<AccessLogSearchDto>(content, pageable, total);
    }

    private BooleanExpression ignoreMyIp(HttpServletRequest request) {
        return qAccessLogEntity.rHost.notEqualsIgnoreCase(Utils.getClientIP(request));
    }

    private BooleanExpression containAccessIp(String memberId) {
        if (memberId == null || memberId.isEmpty()) {
            return null;
        }
        return qAccessLogEntity.rHost.containsIgnoreCase(memberId);
    }

    private BooleanExpression betweenAccDate(String startDt, String endDt) {
        if (startDt == null || endDt == null) {
            return null;
        }
        return qAccessLogEntity.accDat.between(startDt, endDt);
    }


}
