package user.mngm.usermanagement.common.jpa;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/* 테이블에 INSERT시 UUID컬럼에 자동으로 값을 생성해낸다.*/
/* 테이블에 UUID컬럼이 PK값으로 설정되어 있고 단일 PK인 Entity들은 해당 클래스를 상속받는다. */
@MappedSuperclass
@Data
public class GenUuid {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ID", unique = true)
    private String uuid;
}
