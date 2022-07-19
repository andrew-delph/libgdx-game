package core.common;

import java.util.LinkedList;
import java.util.List;

public class Lockable {

  List<Runnable> tasks = new LinkedList<>();

  void unlock() {
    for (Runnable task : tasks) {
      task.run();
    }
    synchronized (this) {
      this.notifyAll();
    }
  }

  void reset() {
    tasks = new LinkedList<>();
  }

  void waitForUnlock() throws InterruptedException {
    synchronized (this) {
      this.wait();
    }
  }

  void waitForUnlock(Runnable task) throws InterruptedException {
    addTaskToUnlock(task);
    waitForUnlock();
  }

  synchronized void addTaskToUnlock(Runnable task) {
    tasks.add(task);
  }
}
