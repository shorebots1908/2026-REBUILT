package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.intake.Intake;

public class IntakeCommands {
  public static Command intakeDefaultCommand(Intake intake) {
    return Commands.run(
      () -> {intake.oneButtonDeploy();}, intake
    );
  }

  public static Command intakeDeployCommand(Intake intake) {
    return Commands.runOnce(
      () -> {intake.toggleIsUp();}, intake
    );
  }

  public static Command intakeRunCommand(Intake intake) {
    return Commands.runOnce(
      () -> {intake.toggleIsRunning();}, intake
    );
  }

  public static Command intakeSetRunCommand(boolean _isRunning, Intake intake) {
    return Commands.runOnce(
      () -> {intake.setIsRunning(_isRunning);}, intake
    );
  }

  public static Command intakeSetDeployCommand(boolean _isUp, Intake intake) {
    return Commands.runOnce(
      () -> {intake.setIsUp(_isUp);}, intake
    );
  }
}
