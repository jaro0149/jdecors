package frinx.jdecors.aspects.nulls;

import frinx.jdecors.annotations.logging.LogException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class ExceptionsLogger {

    @Pointcut("within(frinx.jdecors..*) && !within(frinx.jdecors.aspects..*)")
    public void inspectedContext() {
    }

    @SuppressWarnings("SpringAopErrorsInspection")
    @Pointcut("call(* *(..))")
    public void callsToAllMethods() {
    }

    @Pointcut("callsToAllMethods() && @annotation(frinx.jdecors.annotations.logging.LogException)")
    public void callsToLoggedMethods() {
    }

    @Pointcut(value = "inspectedContext() && callsToLoggedMethods()")
    public void callsToLoggedMethodsInContext() {
    }

    @AfterThrowing(value = "callsToLoggedMethodsInContext()", throwing = "exception", argNames = "exception,joinPoint")
    public void logException(Exception exception, JoinPoint joinPoint) {
        final String className = joinPoint.getSignature().getDeclaringTypeName();
        final String methodName = joinPoint.getSignature().getName();
        final Logger logger = Logger.getLogger(className);
        final LogRecord logRecord = new LogRecord(
                getLoggingLevel(joinPoint),
                "An exception has been thrown in class '{0}' and method '{1}'.");
        logRecord.setParameters(new Object[] {className, methodName});
        logRecord.setThrown(exception);
        logger.log(logRecord);
    }

    private static Level getLoggingLevel(JoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final LogException annotation = method.getAnnotation(LogException.class);
        return annotation.loggingLevel().getLevel();
    }
}