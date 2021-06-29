package infra.entity.pathfinding.template;

import infra.common.Coordinates;

public class RelativeCoordinates {
    float relativeX;
    float relativeY;

    public RelativeCoordinates(float relativeX, float relativeY) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }

    public Coordinates applyRelativeCoordinates(Coordinates coordinates){
        return new Coordinates(coordinates.getXReal()+this.relativeX,coordinates.getYReal()+this.relativeY);
    }
}
