package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.turret.Spindexer;

public class TurretCommands {
  public static Command spindex(Spindexer spindexer) {
    return Commands.run(
      () -> {spindexer.runSpinner();}, spindexer);
  }
}
