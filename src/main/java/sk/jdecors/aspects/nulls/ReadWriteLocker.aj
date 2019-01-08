package sk.jdecors.aspects.nulls;

import com.google.common.collect.Maps;
import sk.jdecors.annotations.locking.LockedForRead;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect("perthis(inspectedContext())")
public class ReadWriteLocker {
    private final Map<Integer, StampedLock> locksMap = Maps.newHashMap();

    @Pointcut("within(sk.jdecors.aspects..*)")
    public void aspectDefinitions() {
    }

    @SuppressWarnings("SpringAopErrorsInspection")
    @Pointcut("cflow(aspectDefinitions())")
    public void deprecatedAspectTraces() {
    }

    @Pointcut("within(sk.jdecors..*) "
            + "&& !aspectDefinitions()")
    public void inspectedContext() {
    }

    @SuppressWarnings("SpringAopErrorsInspection")
    @Pointcut("call(* *(..))")
    public void callsToAllMethods() {
    }

    @Pointcut("!deprecatedAspectTraces() "
            + "&& callsToAllMethods() "
            + "&& @annotation(sk.jdecors.annotations.locking.LockedForRead)")
    public void callsToReadOnlyMethods() {
    }

    @Pointcut("!deprecatedAspectTraces() "
            + "&& callsToAllMethods() "
            + "&& @annotation(sk.jdecors.annotations.locking.LockedForWrite)")
    public void callsToReadWriteMethods() {
    }

    @Pointcut("callsToReadOnlyMethods() "
            + "&& inspectedContext()")
    public void callsToReadOnlyMethodsInContext() {
    }

    @Pointcut("callsToReadWriteMethods() "
            + "&& inspectedContext()")
    public void callsToReadWriteMethodsInContext() {
    }

    @Around("callsToReadOnlyMethodsInContext()")
    public Object readLock(ProceedingJoinPoint joinPoint) throws Throwable {
        final List<StampedLock> stampedLocks = getStampedLocks(joinPoint);
        final List<Long> stamps = stampedLocks.stream()
                .map(StampedLock::readLock)
                .collect(Collectors.toCollection(LinkedList::new));
        try {
            return joinPoint.proceed();
        } finally {
            IntStream.range(0, stampedLocks.size())
                    .boxed()
                    .sorted(Collections.reverseOrder())
                    .forEach(index -> stampedLocks.get(index).unlockRead(stamps.get(index)));
        }
    }

    @Around("callsToReadWriteMethodsInContext()")
    public Object writeLock(ProceedingJoinPoint joinPoint) throws Throwable {
        final List<StampedLock> stampedLocks = getStampedLocks(joinPoint);
        final List<Long> stamps = stampedLocks.stream()
                .map(StampedLock::writeLock)
                .collect(Collectors.toCollection(LinkedList::new));
        try {
            return joinPoint.proceed();
        } finally {
            IntStream.range(0, stampedLocks.size())
                    .boxed()
                    .sorted(Collections.reverseOrder())
                    .forEach(index -> stampedLocks.get(index).unlockWrite(stamps.get(index)));
        }
    }

    private synchronized List<StampedLock> getStampedLocks(ProceedingJoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final LockedForRead annotation = method.getAnnotation(LockedForRead.class);
        final int[] stamps = annotation.stamp();
        return Arrays.stream(stamps)
                .boxed()
                .map(stamp -> locksMap.putIfAbsent(stamp, new StampedLock()))
                .collect(Collectors.toCollection(LinkedList::new));
    }
}