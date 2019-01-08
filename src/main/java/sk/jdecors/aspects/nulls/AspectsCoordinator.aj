package sk.jdecors.aspects.nulls;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;

@Aspect
@DeclarePrecedence("ReadWriteLocker, ExceptionsLogger, CollectorInvoker, *")
public class AspectsCoordinator {
}