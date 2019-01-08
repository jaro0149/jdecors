package sk.jdecors.annotations.locking;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockedForWrite {
    int DEFAULT_STAMP = 0;
    int[] stamp() default DEFAULT_STAMP;
}