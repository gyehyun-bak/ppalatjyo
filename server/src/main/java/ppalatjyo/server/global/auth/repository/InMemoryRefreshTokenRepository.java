package ppalatjyo.server.global.auth.repository;


import org.springframework.stereotype.Repository;
import ppalatjyo.server.global.auth.domain.RefreshToken;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRefreshTokenRepository implements RefreshTokenRepository {
    private final Map<Long, RefreshToken> store = new ConcurrentHashMap<>();

    @Override
    public void save(RefreshToken refreshToken) {
        store.put(refreshToken.getUserId(), refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        return Optional.ofNullable(store.get(userId));
    }

    @Override
    public void deleteByRefreshToken(String refreshToken) {
        store.entrySet().removeIf(entry -> entry.getValue().getRefreshToken().equals(refreshToken));
    }
}
