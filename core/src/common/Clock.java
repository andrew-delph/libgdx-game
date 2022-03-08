package common;

public class Clock {
  public Tick currentTick;

  Clock() {
    this.currentTick = new Tick(0);
  }

  public void tick() {
    this.currentTick = new Tick(this.currentTick.time + 1);
  }
}
