package org.wstone.jmx;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(value = RUNTIME)
@Target(value = { FIELD })
public @interface ManagedAttribute {
  String description() default "";
  
  boolean readable() default true;

  boolean writable() default true;
}
