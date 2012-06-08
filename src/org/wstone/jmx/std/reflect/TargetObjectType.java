package org.wstone.jmx.std.reflect;

/**
 * class type matching
 */
public class TargetObjectType extends ObjectType
{
  public static final TargetObjectType OBJECT_TYPE = new TargetObjectType();

  // ioc/024f
  @Override
  public boolean isParamAssignableFrom(BaseType type)
  {
    return true;
  }
}
