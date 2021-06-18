package infra.entity.controllers.actions;

public interface EntityActionFactory {
  HorizontalMovementAction createHorizontalMovementAction(int magnitude);

  JumpMovementAction createJumpMovementAction();

  StopMovementAction createStopMovementAction();
}
