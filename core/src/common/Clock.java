package common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Clock {
  Map<Integer, Object> tickNotifier = new ConcurrentHashMap();
  private Tick currentTick;

  Clock() {
    this.setCurrentTick(new Tick(0));
  }

  public synchronized Tick getCurrentTick() {
    return currentTick;
  }

  public synchronized void setCurrentTick(Tick currentTick) {
    this.currentTick = currentTick;
  }

  public synchronized void tick() {
    this.setCurrentTick(new Tick(currentTick.time + 1));
    tickNotifier.putIfAbsent(currentTick.time, new Object());
    synchronized (tickNotifier.get(currentTick.time)) {
      tickNotifier.get(currentTick.time).notifyAll();
    }
  }

  public void waitForTick() throws InterruptedException {
    waitForTick(1);
  }

  public void waitForTick(int time) throws InterruptedException {
    int nextTick = this.getCurrentTick().time + time;
    tickNotifier.putIfAbsent(nextTick, new Object());
    synchronized (tickNotifier.get(nextTick)) {
      tickNotifier.get(nextTick).wait();
    }
  }

  public void waitForTick(Runnable task) throws InterruptedException {
    this.waitForTick(task, 1);
  }

  public void waitForTick(Runnable task, int waitTime) throws InterruptedException {
    waitForTick(waitTime);
    task.run();
  }
}
