package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.climb.ClimbConstants;

public class ClimbCommands {
    public static Command climbUp(Climb climb) {
        return Commands.run(() -> climb.runClimb(ClimbConstants.climbSpeed), climb)
            .finallyDo(() -> climb.stopClimb());
    }

    public static Command climbDown(Climb climb) {
        return Commands.run(() -> climb.runClimb(-ClimbConstants.climbSpeed), climb)
            .finallyDo(() -> climb.stopClimb());
    }
}