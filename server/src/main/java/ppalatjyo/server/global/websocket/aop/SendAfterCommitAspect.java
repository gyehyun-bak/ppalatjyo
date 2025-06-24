package ppalatjyo.server.global.websocket.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ppalatjyo.server.global.websocket.MessageBrokerService;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SendAfterCommitAspect {

    private final MessageBrokerService messageBrokerService;

    /**
     * {@link SendAfterCommit} 어노테이션이 붙은 메서드가 반환하는 {@link SendAfterCommitDto} 데이터를 추출하여
     * 현재 트랜잭션이 있는 경우 이를 커밋 후에 Dto의 {@code destination}으로 {@code data}를 발행합니다.
     */
    @Around("@annotation(SendAfterCommit)")
    public Object sendAfterCommit(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof SendAfterCommitDto<?> dto
                && TransactionSynchronizationManager.isActualTransactionActive()
        ) {
            String destination = dto.getDestination();
            Object data = dto.getData();

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    messageBrokerService.publish(destination, data);
                }
            });
        }

        return result;
    }
}
