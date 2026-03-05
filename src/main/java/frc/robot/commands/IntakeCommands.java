package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.intake.Intake;

public class IntakeCommands {
  public static Command intakeDefaultCommand(Intake intake) {
    return Commands.run(
      () -> {intake.oneButtonDeploy();
      if(intake.getIsRunning()) {
        intake.runIntake();
      }
      else {
        intake.stopIntake();
      }}, intake
    );
  }

  public static Command autoDeployAndRunIntake(Intake intake) {
    return Commands.runEnd(
      () -> {intake.deployIntake();
            intake.runIntake();
      },
      () -> {
        intake.stopIntake();
        intake.stopAtBottom();
        intake.setIsRunning(false);
        intake.setIsUp(false);
      },
      intake
      ).until(intake::isDeployed);
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

  public static Command deployIntakeOpenloop(DoubleSupplier supplier, Intake intake) {
    return Commands.run(
      () -> {
        double intakeInput = MathUtil.applyDeadband(supplier.getAsDouble(), 0.1);
        intakeInput = Math.copySign(intakeInput * intakeInput, intakeInput);
        intake.deployIntake(intakeInput);},
        intake
      );
  }

public static Command autoRunIntake(Intake intake) {
    return Commands.runEnd(
      () -> {intake.runIntake();}, 
      () -> {intake.stopIntake(); 
        intake.setIsRunning(false);}, 
      intake);
  }

public static Command autoStopIntake(Intake intake) {
    return Commands.runEnd(
      () -> {intake.stopIntake();}, 
      () -> {intake.runIntake(); 
        intake.setIsRunning(true);}, 
      intake);
  }

  public static Command autoDeployIntake(Intake intake) {
    return Commands.runEnd(
      () -> {intake.deployIntake();}, 
      () -> {intake.stopAtBottom(); 
        intake.setIsUp(false);}, 
      intake).until(
        intake::isDeployed);
  }

  public static Command autoUndeployIntake(Intake intake) {
    return Commands.runEnd(
      () -> {intake.undeployIntake();},
      () -> {intake.stopAtTop();
        intake.setIsUp(true);},
      intake).until(
        intake::isUndeployed);
  }
}
