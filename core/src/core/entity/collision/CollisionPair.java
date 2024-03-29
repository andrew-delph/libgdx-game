package core.entity.collision;

public class CollisionPair {
  Class<? extends CollisionPoint> source;
  Class<? extends CollisionPoint> target;

  public CollisionPair(
      Class<? extends CollisionPoint> source, Class<? extends CollisionPoint> target) {
    this.source = source;
    this.target = target;
  }

  public CollisionPair reverse() {
    return new CollisionPair(this.target, this.source);
  }

  @Override
  public int hashCode() {
    return (this.source.toString() + "," + this.target.toString()).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    CollisionPair other = (CollisionPair) obj;
    return target == other.target && source == other.source;
  }

  @Override
  public String toString() {
    return "CollisionPair{" + "source=" + source + ", target=" + target + '}';
  }
}
