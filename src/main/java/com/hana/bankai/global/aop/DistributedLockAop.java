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
import java.util.ArrayList;
import java.util.List;

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
        String[] keys = distributedLock.key();
        List<RLock> locks = new ArrayList<>();
        for (String key : keys) {
            String lockKey = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
                    signature.getParameterNames(),
                    joinPoint.getArgs(),
                    key
            );
            RLock rLock = redissonClient.getLock(lockKey);
            locks.add(rLock);
        }

        try {
            boolean allLocksAcquired = true;
            for (RLock lock : locks) {
                if (!lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit())) {
                    allLocksAcquired = false;
                    break;
                }
            }

            if (!allLocksAcquired) {
                for (RLock lock : locks) {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
                throw new CustomException(LOCK_NOT_AVAILABLE);
            }

            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new CustomException(LOCK_INTERRUPTED_ERROR);
        } finally {
            for (RLock lock : locks) {
                try {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                } catch (IllegalMonitorStateException e) {
                    log.info("Redisson Lock Already UnLocked");
                }
            }
        }
    }
}
