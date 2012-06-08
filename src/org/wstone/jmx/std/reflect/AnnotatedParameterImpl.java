package org.wstone.jmx.std.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;

import javax.enterprise.inject.spi.AnnotatedCallable;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;

/**
 * Abstract introspected view of a Bean
 */
public class AnnotatedParameterImpl<T>
  extends AnnotatedElementImpl implements AnnotatedParameter<T>
{
  private AnnotatedCallable<T> _callable;
  private int _position;
  
  public AnnotatedParameterImpl(AnnotatedCallable<T> callable,
                                Type type,
                                HashMap<String,BaseType> paramMap,
                                Annotation []annList,
                                int position)
  {
    super(createBaseType(callable, type, paramMap), null, annList);

    _callable = callable;
    _position = position;
  }
  
  protected static BaseType createBaseType(AnnotatedCallable<?> callable,
                                           Type type,
                                           HashMap<String,BaseType> paramMap)
  {
    if (callable != null && callable.getDeclaringType() != null) {
      AnnotatedType<?> declAnnType = callable.getDeclaringType();
      
      String callableName = callable.getJavaMember().getName();
      
      return createBaseType(declAnnType, type, paramMap, callableName);
    }
    
    return createBaseType(type);
  }

  @Override
  public AnnotatedCallable<T> getDeclaringCallable()
  {
    return _callable;
  }

  @Override
  public int getPosition()
  {
    return _position;
  }
}
