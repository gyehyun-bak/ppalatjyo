package ppalatjyo.server.usergame;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserGameService {

    private final UserGameRepository userGameRepository;

    public void increaseScoreBy(Long userGameId, int amount) {
        UserGame userGame = userGameRepository.findById(userGameId).orElseThrow(UserGameNotFoundException::new);
        userGame.increaseScoreBy(amount);
    }
}
