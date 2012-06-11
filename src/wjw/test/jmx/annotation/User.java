package wjw.test.jmx.annotation;

public class User {
  private final String name;

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  private int age;

  public User(String name, int age) {
    super();
    this.name = name;
    this.age = age;
  }

  private static void setTitle(String title) {
    System.out.println("this title is:" + title);
  }

}
