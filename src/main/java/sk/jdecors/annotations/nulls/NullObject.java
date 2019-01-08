package sk.jdecors.annotations.nulls;

import sk.jdecors.interfaces.nulls.NullObjectIdentity;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface NullObject {
    String DEFAULT_IDENTIFICATION = "default";
    Class defaultObjectType() default NullObjectIdentity.class;
    String identification() default DEFAULT_IDENTIFICATION;
}
