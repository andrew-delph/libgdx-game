package common;

public class Tick {
    public int time;

    public Tick(int time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        return (this.time + "".hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Tick other = (Tick) obj;
        return this.time == other.time;
    }
}
