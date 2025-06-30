package ppalatjyo.server.global.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final TaskScheduler taskScheduler;

    /**
     * 지정된 시간(초) 후 작업 실행
     * @param seconds   대시 시간(초)
     * @param task      실행할 작업
     */
    public void runAfterSeconds(int seconds, Runnable task) {
        Instant executionTime = Instant.now().plusSeconds(seconds);
        taskScheduler.schedule(task, executionTime);
    }

    /**
     * 지정된 시간(분) 후 작업 실행
     * @param minutes   대시 시간(분)
     * @param task      실행할 작업
     */
    public void runAfterMinutes(int minutes, Runnable task) {
        Instant executionTime = Instant.now().plus(minutes, ChronoUnit.MINUTES);
        taskScheduler.schedule(task, executionTime);
    }
}
