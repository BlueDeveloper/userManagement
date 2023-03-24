package user.mngm.usermanagement.jpa.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/* [JPA] --> 기본쿼리 제공 및 커스텀 쿼리를 작성할 수 있는 클래스 */
public interface UserRepository extends JpaRepository<UserEntity, String> {

    //memberId를 기반으로 유저 조회 [SELECT * FROM TB_USER WHERE ID = memberId]
    Optional<UserEntity> findByMemberId(String memberId);

    //email를 기반으로 유저 조회 [SELECT * FROM TB_USER WHERE ID = email]
    UserEntity findByEmail(String email);

    UserEntity save(UserEntity user);

}
