package user.mngm.usermanagement.jpa.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import user.mngm.usermanagement.jpa.user.dto.UserSearchDto;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;

public interface UserRepositoryCustom {
    Page<UserSearchDto> findBySearchOption(Pageable pageable, String memberId, String name, String email);
}
