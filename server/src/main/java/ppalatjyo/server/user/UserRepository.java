package ppalatjyo.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
