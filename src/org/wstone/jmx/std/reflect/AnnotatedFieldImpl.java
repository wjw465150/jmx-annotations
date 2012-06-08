package org.wstone.jmx.std.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;


/**
 * Abstract introspected view of a Bean
 */
public class AnnotatedFieldImpl<X>
  extends AnnotatedElementImpl implements AnnotatedField<X>
{
  private AnnotatedType<X> _declaringType;
  
  private Field _field;
  
  public AnnotatedFieldImpl(AnnotatedType<X> declaringType, Field field)
  {
    super(createBaseType(declaringType, field.getGenericType()),
          null, 
          field.getAnnotations());

    _declaringType = declaringType;
    _field = field;

    introspect(field);
  }

  public AnnotatedType<X> getDeclaringType()
  {
    return _declaringType;
  }
  
  /**
   * Returns the reflected Method
   */
  public Field getJavaMember()
  {
    return _field;
  }

  public boolean isStatic()
  {
    return Modifier.isStatic(_field.getModifiers());
  }

  private void introspect(Field field)
  {
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + _field + "]";
  }
}
