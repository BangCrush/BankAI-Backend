package com.hana.bankai.global.aop;

import com.hana.bankai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.hana.bankai.global.error.ErrorCode.*;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        // 분산락의 키를 생성하고 락을 가지고 온다.
        // 어노테이션에서 설정한 키 값을 조합하여 생성
        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
                signature.getParameterNames(),
                joinPoint.getArgs(),
                distributedLock.key()
        );
        log.info("lock on [method:{}] [key:{}]", method, key);

        RLock rLock = redissonClient.getLock(key);
        String lockName = rLock.getName();

        try {
            boolean lockable = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!lockable) {
                throw new CustomException(LOCK_NOT_AVAILABLE);
            }
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new CustomException(LOCK_INTERRUPTED_ERROR);
        } finally {
            try {
                rLock.unlock();
                log.info("unlock complete [Lock:{}] ", lockName);
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLocked");
            }
        }
    }
}
