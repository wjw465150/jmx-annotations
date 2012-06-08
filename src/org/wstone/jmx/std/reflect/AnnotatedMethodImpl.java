package org.wstone.jmx.std.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;


/**
 * Abstract introspected view of a Bean
 */
public class AnnotatedMethodImpl<T>
  extends AnnotatedElementImpl implements AnnotatedMethod<T>
{
  private AnnotatedType<T> _declaringType;
  
  private Method _method;
  private AnnotatedMethod<T> _baseMethod;
  
  private HashMap<String,BaseType> _paramMap;

  private List<AnnotatedParameter<T>> _parameterList;
  
  public AnnotatedMethodImpl(Method method)
  {
    this(null, null, method);
  }

  public AnnotatedMethodImpl(AnnotatedType<T> declaringType,
                             Annotated annotated,
                             Method method)
  {
    this(declaringType, annotated, method, method.getAnnotations(), null);
  }
    
  public AnnotatedMethodImpl(AnnotatedType<T> declaringType,
                             Annotated annotated,
                             Method method,
                             Annotation []annotations,
                             HashMap<String,BaseType> paramMap)
  {
    super(createBaseType(declaringType,
                         method.getGenericReturnType(),
                         method.getName()),
          annotated, annotations);

    _declaringType = declaringType;
    _method = method;
    
    if (annotated instanceof AnnotatedMethod<?>)
      _baseMethod = (AnnotatedMethod<T>) annotated;
    
    _paramMap = paramMap;
  }

  @Override
  public AnnotatedType<T> getDeclaringType()
  {
    return _declaringType;
  }
  
  /**
   * Returns the reflected Method
   */
  @Override
  public Method getJavaMember()
  {
    return _method;
  }

  /**
   * Returns the constructor parameters
   */
  @Override
  public List<AnnotatedParameter<T>> getParameters()
  {
    if (_parameterList == null) {
      if (_baseMethod != null) {
        _parameterList = _baseMethod.getParameters();
      }
      else {
        _parameterList = introspectParameters(_method);
      }
    }
    
    return _parameterList;
  }

  @Override
  public boolean isStatic()
  {
    return Modifier.isStatic(_method.getModifiers());
  }

  private List<AnnotatedParameter<T>> introspectParameters(Method method)
  {
    ArrayList<AnnotatedParameter<T>> parameterList
      = new ArrayList<AnnotatedParameter<T>>();
    
    Type []paramTypes = method.getGenericParameterTypes();
    Annotation [][]annTypes = method.getParameterAnnotations();
    
    for (int i = 0; i < paramTypes.length; i++) {
      AnnotatedParameterImpl<T> param
        = new AnnotatedParameterImpl<T>(this, paramTypes[i], _paramMap,
                                        annTypes[i], i);
      parameterList.add(param);
    }
    
    return parameterList;
  }

  @Override
  protected void fillTypeVariables(Set<VarType<?>> typeVariables)
  {
    getBaseTypeImpl().fillSyntheticTypes(typeVariables);

    for (AnnotatedParameter<T> param : getParameters()) {
      if (param instanceof BaseTypeAnnotated) {
        BaseTypeAnnotated annType = (BaseTypeAnnotated) param;
        
        annType.getBaseTypeImpl().fillSyntheticTypes(typeVariables);
      }
    }
  }

  public static boolean isMatch(Method methodA, Method methodB)
  {
    if (! methodA.getName().equals(methodB.getName())) {
      return false;
    }
    
    // ejb/4018 
    if (! methodA.getDeclaringClass().equals(methodB.getDeclaringClass())) {
      return false;
    }

    Class<?> []paramA = methodA.getParameterTypes();
    Class<?> []paramB = methodB.getParameterTypes();

    if (paramA.length != paramB.length)
      return false;

    for (int i = 0; i < paramA.length; i++) {
      if (! paramA[i].equals(paramB[i]))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    return _method.hashCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    else if (! (obj instanceof AnnotatedMethodImpl<?>))
      return false;

    AnnotatedMethodImpl<?> method = (AnnotatedMethodImpl<?>) obj;

    return isMatch(_method, method._method);
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    
    sb.append(getClass().getSimpleName());
    sb.append("[");
    sb.append(_method);
    sb.append("]");
    
    return sb.toString(); 
  }
}
