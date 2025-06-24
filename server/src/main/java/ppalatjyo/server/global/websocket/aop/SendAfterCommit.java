package ppalatjyo.server.global.websocket.aop;

import java.lang.annotation.*;

/**
 * 메서드가 {@link SendAfterCommitDto}를 반환하면
 * 현재 트랜잭션이 커밋된 후에 Dto의 {@code destination}으로 {@code data}를 발행합니다.
 * {@link SendAfterCommitAspect} 참조.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SendAfterCommit {
}
