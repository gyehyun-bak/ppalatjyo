package ppalatjyo.server.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
