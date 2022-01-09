package entity.pathfinding;

import com.badlogic.gdx.math.Vector2;

public class RelativeVertex {
    private final RelativeCoordinates relativeCoordinates;
    Vector2 velocity;
    EntityStructure entityStructure;


    public RelativeVertex(
            EntityStructure entityStructure, RelativeCoordinates relativeCoordinates, Vector2 velocity) {
        this.entityStructure = entityStructure;
        this.relativeCoordinates = relativeCoordinates;
        this.velocity = velocity;
    }

    public RelativeCoordinates getRelativeCoordinates() {
        return relativeCoordinates;
    }

    @Override
    public String toString() {
        return "RelativeVertex{" + "relativeCoordinates=" + relativeCoordinates + '}';
    }

    @Override
    public int hashCode() {
        return (this.relativeCoordinates.hashCode() + ",").hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RelativeVertex other = (RelativeVertex) obj;
        return this.relativeCoordinates.equals(other.relativeCoordinates)
                && this.velocity.equals(other.velocity);
    }
}
