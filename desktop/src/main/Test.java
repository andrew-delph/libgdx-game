package main;

public class Test {

  public static void main(String[] args) throws InterruptedException {
    // diff
    Long x = 100l;
    x = (long) (x * 1.11111111111111111111);
    System.out.println(x);
    System.out.println((100f * 1.11111111111111111111f));
  }
}
