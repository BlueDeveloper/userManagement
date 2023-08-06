package user.mngm.usermanagement.jpa.accessLog.entity;

import lombok.Data;
import user.mngm.usermanagement.common.jpa.GenUuid;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity //[JPA] --> DB 테이블과 1:1 매칭되는 클래스 선언
@Table(name = "TB_ACCESS_LOG") //[JPA] --> 테이블 명
@Data
public class AccessLogEntity extends GenUuid {
    @Column(name = "R_URI")
    private String rUri;

    @Column(name = "R_HOST")
    private String rHost;

    @Column(name = "LOG_DAT")
    private LocalDateTime logDat = LocalDateTime.now();
}
