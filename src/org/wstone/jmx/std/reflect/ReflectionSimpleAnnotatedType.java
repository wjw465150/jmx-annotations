package org.wstone.jmx.std.reflect;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

/**
 * Represents the reflected type where only the top-level annotations are
 * introspected, i.e. the class itself, not the fields or methods.
 */
public class ReflectionSimpleAnnotatedType<X>
  extends ReflectionAnnotated
  implements AnnotatedType<X>
{
  private Class<X> _javaClass;

  private static Set _emptyConstructorSet
    = new LinkedHashSet<AnnotatedConstructor<?>>();

  private static Set _emptyFieldSet
    = new LinkedHashSet<AnnotatedField<?>>();

  private static Set _emptyMethodSet
    = new LinkedHashSet<AnnotatedMethod<?>>();
  
  public ReflectionSimpleAnnotatedType(BaseTypeFactory manager, BaseType type)
  {
    super(type,
          type.getTypeClosure(manager),
          type.getRawClass().getAnnotations());
    
    _javaClass = (Class<X>) type.getRawClass();
  }
  
  public Class<X> getJavaClass()
  {
    return _javaClass;
  }

  /**
   * Returns the abstract introspected constructors
   */
  public Set<AnnotatedConstructor<X>> getConstructors()
  {
    return _emptyConstructorSet;
  }

  /**
   * Returns the abstract introspected methods
   */
  public Set<AnnotatedMethod<? super X>> getMethods()
  {
    return _emptyMethodSet;
  }

  /**
   * Returns the abstract introspected fields
   */
  public Set<AnnotatedField<? super X>> getFields()
  {
    return _emptyFieldSet;
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + _javaClass + "]";
  }
}
