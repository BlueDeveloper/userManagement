package user.mngm.usermanagement.jpa.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user.mngm.usermanagement.jpa.admin.entity.AccessLogEntity;
import user.mngm.usermanagement.jpa.user.repository.UserRepositoryCustom;

public interface AccessLogRepository extends JpaRepository<AccessLogEntity, String>, AccessLogRepositoryCustom {
}
