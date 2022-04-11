package entity.pathfinding;

import chunk.world.exceptions.BodyNotFound;

public class EdgeRegistrationBase {

  public void edgeRegistration() throws BodyNotFound {}

  public void templateEdgeRegistration() throws BodyNotFound {}

  public void horizontalGreedyRegisterEdges() {}

  public void ladderGreedyRegisterEdges() {}

  public void digGreedyRegisterEdges() {}
}
