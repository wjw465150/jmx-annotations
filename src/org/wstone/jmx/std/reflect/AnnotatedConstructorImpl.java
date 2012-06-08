package org.wstone.jmx.std.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;

/**
 * Abstract introspected view of a Bean
 */
public class AnnotatedConstructorImpl<T>
  extends AnnotatedElementImpl implements AnnotatedConstructor<T>
{
  private final AnnotatedType<T> _declaringType;
  
  private final Constructor<T> _ctor;

  private final ArrayList<AnnotatedParameter<T>> _parameterList
    = new ArrayList<AnnotatedParameter<T>>();
  
  public AnnotatedConstructorImpl(AnnotatedType<T> declaringType, Constructor<T> ctor)
  {
    super(createBaseType(declaringType), null, ctor.getAnnotations());

    _declaringType = declaringType;
    
    _ctor = ctor;

    introspect(ctor);
  }

  @Override
  public AnnotatedType<T> getDeclaringType()
  {
    return _declaringType;
  }
  
  /**
   * Returns the reflected Constructor
   */
  @Override
  public Constructor<T> getJavaMember()
  {
    return _ctor;
  }

  /**
   * Returns the constructor parameters
   */
  @Override
  public List<AnnotatedParameter<T>> getParameters()
  {
    return _parameterList;
  }

  @Override
  public boolean isStatic()
  {
    return false;
  }

  private void introspect(Constructor<T> ctor)
  {
    Type []paramTypes = ctor.getGenericParameterTypes();
    HashMap<String,BaseType> paramMap = null;
    Annotation [][]annTypes = ctor.getParameterAnnotations();
    
    for (int i = 0; i < paramTypes.length; i++) {
      AnnotatedParameterImpl<T> param
        = new AnnotatedParameterImpl<T>(this, paramTypes[i], paramMap,
                                        annTypes[i], i);

      _parameterList.add(param);
    }
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + _ctor + "]";
  }
}
