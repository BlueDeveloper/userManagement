package user.mngm.usermanagement.jpa.accessLog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user.mngm.usermanagement.jpa.accessLog.entity.AccessLogEntity;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLogEntity, String> {
}
