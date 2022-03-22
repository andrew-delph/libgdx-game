package main;

import common.Tick;
import java.util.HashSet;
import java.util.Set;

public class Test {

  public static void main(String[] args) throws InterruptedException {

    //    final ExecutorService executor = Executors.newFixedThreadPool(2);
    //    final Lock lock = new ReentrantLock();
    //    final Condition condition = lock.newCondition();
    //    final Object obj = new Object();
    //
    //    Tick tick = new Tick(1);
    //
    //    Runnable task = ()->{
    //      System.out.println("hello");
    //    };
    //
    //    task.run();

    Set<Tick> set = new HashSet<>();

    Tick a = new Tick(1);
    Tick b = new Tick(1);

    set.add(a);
    set.add(b);

    //    System.out.println(a == a);
    //    System.out.println(a == b);
    //    System.out.println(a != b);

    //    executor.submit(
    //        () -> {
    //          try {
    //            TimeUnit.SECONDS.sleep(3);
    //          } catch (InterruptedException e) {
    //            e.printStackTrace();
    //          }
    //          System.out.println("unlocking");
    //
    //          synchronized (tick) {
    //            tick.notifyAll();
    //          }
    //
    //          System.out.println("unlocked");
    //        });
    //
    //    executor.submit(
    //        () -> {
    //          System.out.println("waiting ");
    //          synchronized (tick) {
    //            try {
    //              tick.wait();
    //            } catch (InterruptedException e) {
    //              e.printStackTrace();
    //            }
    //          }
    //
    //          System.out.println("done waiting");
    //        });

    //    executor.shutdown();
  }
}
