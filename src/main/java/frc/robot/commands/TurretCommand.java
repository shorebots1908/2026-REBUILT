package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.turret.Rotator;

public class TurretCommand {
  public static Command turretRotate(Rotator rotator, DoubleSupplier output)
  {
    return Commands.run(
      () -> {
        rotator.setTurretRotationOpenLoop(output.getAsDouble());
      },
      rotator
    );
  }
}
