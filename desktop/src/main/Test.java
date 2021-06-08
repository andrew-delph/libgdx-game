package main;

import java.util.HashSet;
import java.util.Set;

public class Test {
  public static void main(String[] args) {
    Set<String> a = new HashSet<>();
    a.add("x");
    a.add("y");

    Set<String> b = new HashSet<>();
    b.add("y");
    b.add("z");

    HashSet result = new HashSet<>(a);
    result.removeAll(b);
    System.out.println(result);
  }
}
