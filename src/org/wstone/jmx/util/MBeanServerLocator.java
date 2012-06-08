package org.wstone.jmx.util;

import java.util.ArrayList;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.wstone.jmx.MBean;
import org.wstone.jmx.std.reflect.ReflectionAnnotatedFactory;

public class MBeanServerLocator {

  private MBeanServer mBeanServer;

  private static MBeanServerLocator instance;

  private MBeanServerLocator() {
    this.mBeanServer = locateMBeanServer();
  }

  public static MBeanServerLocator instance() {
    if (instance == null) {
      instance = new MBeanServerLocator();
    }

    return instance;
  }

  public MBeanServer getmBeanServer() {
    return mBeanServer;
  }

  public void setmBeanServer(MBeanServer mBeanServer) {
    this.mBeanServer = mBeanServer;
  }

  private MBeanServer locateMBeanServer() {
    MBeanServer mBeanServer = null;

    try {
      //mBeanServer = org.jboss.mx.util.MBeanServerLocator.locateJBoss();
      java.lang.reflect.Method locateJBossMethod = Class.forName("org.jboss.mx.util.MBeanServerLocator").getDeclaredMethod("locateJBoss", new Class[0]);
      locateJBossMethod.invoke(null, new Object[0]);
    } catch (Exception e) {
      ArrayList<MBeanServer> al = MBeanServerFactory.findMBeanServer(null);
      if (al.isEmpty())
        mBeanServer = java.lang.management.ManagementFactory.getPlatformMBeanServer();
      else
        mBeanServer = (MBeanServer) al.get(0);
    }

    return mBeanServer;
  }

  public static ObjectName getObjectName(Object instance) throws Exception {
    AnnotatedType<?> at = ReflectionAnnotatedFactory.introspectType(instance.getClass());
    String name = "";

    if (at.isAnnotationPresent(MBean.class)) {
      MBean mBeanAnnotation = at.getAnnotation(MBean.class);
      name = mBeanAnnotation.value();
      if (name != null && name.equals("")) {
        name = null;
      }
    }

    if (name == null) {
      name = at.getJavaClass().getPackage().getName() + ":type=" + at.getJavaClass().getSimpleName();
    }

    return new ObjectName(name);
  }

}
