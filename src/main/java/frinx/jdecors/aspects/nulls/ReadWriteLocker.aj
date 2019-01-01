package frinx.jdecors.aspects.nulls;

import com.google.common.collect.Maps;
import frinx.jdecors.annotations.locking.LockedForRead;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect("perthis(inspectedContext())")
public class ReadWriteLocker {
    private final Map<Integer, StampedLock> locksMap = Maps.newHashMap();

    @Pointcut("within(frinx.jdecors.aspects..*)")
    public void aspectDefinitions() {
    }

    @SuppressWarnings("SpringAopErrorsInspection")
    @Pointcut("cflow(aspectDefinitions())")
    public void deprecatedAspectTraces() {
    }

    @Pointcut("within(frinx.jdecors..*) && !aspectDefinitions()")
    public void inspectedContext() {
    }

    @SuppressWarnings("SpringAopErrorsInspection")
    @Pointcut("call(* *(..))")
    public void callsToAllMethods() {
    }

    @Pointcut("!deprecatedAspectTraces() && callsToAllMethods() "
            + "&& @annotation(frinx.jdecors.annotations.locking.LockedForRead)")
    public void callsToReadOnlyMethods() {
    }

    @Pointcut("!deprecatedAspectTraces() && callsToAllMethods() "
            + "&& @annotation(frinx.jdecors.annotations.locking.LockedForWrite)")
    public void callsToReadWriteMethods() {
    }

    @Pointcut("callsToReadOnlyMethods() && inspectedContext()")
    public void callsToReadOnlyMethodsInContext() {
    }

    @Pointcut("callsToReadWriteMethods() && inspectedContext()")
    public void callsToReadWriteMethodsInContext() {
    }

    @Around("callsToReadOnlyMethodsInContext()")
    public Object readLock(ProceedingJoinPoint joinPoint) throws Throwable {
        final StampedLock stampedLock = getStampedLock(joinPoint);
        final long stamp = stampedLock.readLock();
        try {
            return joinPoint.proceed();
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }

    @Around("callsToReadWriteMethodsInContext()")
    public Object writeLock(ProceedingJoinPoint joinPoint) throws Throwable {
        final StampedLock stampedLock = getStampedLock(joinPoint);
        final long stamp = stampedLock.writeLock();
        try {
            return joinPoint.proceed();
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    private synchronized StampedLock getStampedLock(ProceedingJoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final LockedForRead annotation = method.getAnnotation(LockedForRead.class);
        final int stamp = annotation.stamp();
        locksMap.putIfAbsent(stamp, new StampedLock());
        return locksMap.get(stamp);
    }
}