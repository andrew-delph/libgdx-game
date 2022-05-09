package common;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Clock {
  Map<Integer, Lockable> tickLockable = new ConcurrentHashMap();
  List<Runnable> tickRunnableList = new LinkedList<>();
  private Tick currentTick;

  Clock() {
    this.setCurrentTick(new Tick(0));
  }

  public static Long getCurrentTime() {
    return System.currentTimeMillis();
  }

  public synchronized Tick getCurrentTick() {
    return currentTick;
  }

  public synchronized void setCurrentTick(Tick currentTick) {
    this.currentTick = currentTick;
  }

  public synchronized void tick() {
    this.setCurrentTick(new Tick(currentTick.time + 1));
    tickLockable.putIfAbsent(currentTick.time, new Lockable());
    tickLockable.get(currentTick.time).unlock();
    for (Runnable task : tickRunnableList) {
      task.run();
    }
  }

  public void waitForTick() throws InterruptedException {
    waitForTick(1);
  }

  public void waitForTick(int time) throws InterruptedException {
    waitForTick(time, null);
  }

  public void waitForTick(Runnable task) throws InterruptedException {
    this.waitForTick(1, task);
  }

  public void waitForTick(int time, Runnable task) throws InterruptedException {
    int nextTick = this.getCurrentTick().time + time;
    tickLockable.putIfAbsent(nextTick, new Lockable());
    if (task == null) {
      tickLockable.get(nextTick).waitForUnlock();
    } else {
      tickLockable.get(nextTick).waitForUnlock(task);
    }
  }

  public void addTaskOnTickTime(int time, Runnable task) {
    int nextTick = this.getCurrentTick().time + time;
    tickLockable.putIfAbsent(nextTick, new Lockable());
    tickLockable.get(nextTick).addTaskToUnlock(task);
  }

  public void addTaskOnTick(Runnable task) {
    tickRunnableList.add(task);
  }
}
