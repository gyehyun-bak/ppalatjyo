package ppalatjyo.server.global.auth.repository;

import ppalatjyo.server.global.auth.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    Optional<RefreshToken> findByUserId(Long userId);

    void deleteByRefreshToken(String refreshToken);
}
