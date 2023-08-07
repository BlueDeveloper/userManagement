package user.mngm.usermanagement.jpa.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import user.mngm.usermanagement.jpa.admin.dto.AccessLogSearchDto;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;

public interface AccessLogRepositoryCustom {
    Page<AccessLogSearchDto> findBySearchOption(Pageable pageable, String rHost, String accDatStart, String accDateEnd, HttpServletRequest request);
}
