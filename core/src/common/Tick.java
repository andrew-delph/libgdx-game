package common;

import java.util.Objects;

public class Tick implements Comparable<Tick> {
  public int time;

  public Tick(int time) {
    this.time = time;
  }

  public Tick next() {
    return new Tick(time + 1);
  }

  @Override
  public int compareTo(Tick other) {
    return this.time - other.time;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tick tick = (Tick) o;
    return time == tick.time;
  }

  @Override
  public int hashCode() {
    return Objects.hash(time);
  }
}
