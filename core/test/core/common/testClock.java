package core.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;

public class testClock {

  @Test
  public void testWaitForTick() throws InterruptedException {
    final ExecutorService executor = Executors.newFixedThreadPool(2);
    Clock clock = new Clock();
    AtomicReference<Boolean> flag = new AtomicReference<>(false);

    executor.execute(
        () -> {
          try {
            clock.waitForTick();
          } catch (InterruptedException e) {
            e.printStackTrace();
            return;
          }
          flag.set(true);
        });

    assert !flag.get();
    TimeUnit.MILLISECONDS.sleep(500);
    clock.tick();
    executor.shutdown();
    TimeUnit.MILLISECONDS.sleep(500);
    assert flag.get();
  }

  @Test
  public void testWaitForTickNumber() throws InterruptedException {
    final ExecutorService executor = Executors.newFixedThreadPool(2);
    Clock clock = new Clock();
    AtomicReference<Boolean> flag = new AtomicReference<>(false);

    executor.execute(
        () -> {
          try {
            clock.waitForTick(2);
          } catch (InterruptedException e) {
            e.printStackTrace();
            return;
          }
          flag.set(true);
        });

    assert !flag.get();
    TimeUnit.MILLISECONDS.sleep(500);
    clock.tick();
    clock.tick();
    executor.shutdown();
    TimeUnit.MILLISECONDS.sleep(500);
    assert flag.get();
  }

  @Test
  public void testWaitForTickTask() throws InterruptedException {
    final ExecutorService executor = Executors.newFixedThreadPool(2);
    Clock clock = new Clock();
    AtomicReference<Boolean> flag = new AtomicReference<>(false);

    executor.execute(
        () -> {
          try {
            clock.waitForTick(
                () -> {
                  flag.set(true);
                });
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        });

    assert !flag.get();
    TimeUnit.MILLISECONDS.sleep(500);
    clock.tick();
    executor.shutdown();
    TimeUnit.MILLISECONDS.sleep(500);
    assert flag.get();
  }
}
