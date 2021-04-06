package infra.common;

public class Coordinate {
    float x;
    float y;

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return (int) this.x;
    }

    public int getY() {
        return (int) this.y;
    }

    public float getXReal() {
        return this.x;
    }

    public float getYReal() {
        return this.y;
    }

    @Override
    public int hashCode() {
        return (this.x + "," + this.y).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coordinate other = (Coordinate) obj;
        return x == other.x && y == other.y;
    }

    public String tostring(){
        return this.getX()+","+this.getY();
    }
}
