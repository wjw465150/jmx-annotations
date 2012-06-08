package org.wstone.jmx.std.reflect;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;


/**
 * Abstract introspected view of a Bean
 */
public interface BaseTypeAnnotated {
  public BaseType getBaseTypeImpl();
  
  public HashMap<String,BaseType> getBaseTypeParamMap();
  
  /**
   * Returns the type variables local to the type.
   */
  public Set<VarType<?>> getTypeVariables();
  
  /**
   * Overrides, e.g. for analysis
   */
  public void addOverrideAnnotation(Annotation ann);
}
