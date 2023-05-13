package user.mngm.usermanagement.jpa.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;

import java.util.Optional;

/* [JPA] --> 기본쿼리 제공 및 커스텀 쿼리를 작성할 수 있는 클래스 */
public interface UserRepository extends JpaRepository<UserEntity, String>, UserRepositoryCustom{

    //memberId를 기반으로 유저 조회 [SELECT * FROM TB_USER WHERE ID = memberId]
    Optional<UserEntity> findByMemberId(String memberId);

    //email를 기반으로 유저 조회 [SELECT * FROM TB_USER WHERE ID = email]
    Optional<UserEntity> findByEmail(String email);


}
