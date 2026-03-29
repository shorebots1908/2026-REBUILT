package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.turret.Spindexer;
import frc.robot.subsystems.turret.Rotator;
import frc.robot.subsystems.turret.Shooter;
import frc.robot.subsystems.turret.Feeder;

public class TurretCommands {
  private static final double DEADBAND = 0.1;

  public static Command spindex(Spindexer spindexer) {
    return Commands.run(
      () -> {spindexer.runSpinner();}, spindexer);
  }

  public static Command openLoopRotate(Rotator rotator, DoubleSupplier rotationInput) {
    return Commands.run(
      () -> {
        // Apply rotation deadband
          double omega = MathUtil.applyDeadband(rotationInput.getAsDouble(), DEADBAND);

          // Square rotation value for more precise control
          omega = Math.copySign(omega * omega, omega);

          rotator.setTurretRotationOpenLoop(0.5 * omega);
      }, 
      rotator
    );
  }

  public static Command primeShooter(Shooter shooter){
    return Commands.run(() -> shooter.primeShooter(), shooter);
  }

  public static Command toggleTargeting(Rotator rotator, Shooter shooter) {
    return Commands.runOnce(() -> {
        rotator.toggleOpenClosedLoop();
      }, 
      rotator
    );
  }

  public static Command targetingIsOnCommand(Rotator rotator, Shooter shooter) {
    return Commands.runOnce(() -> {
        rotator.startClosedLoop();
    },
    rotator
    );
  }

  public static Command spindexCommand(Spindexer spindexer) {
    return Commands.runOnce(
      () -> {spindexer.toggleRunning();}, spindexer);
  }

  public static Command spindexDirectionCommand(Spindexer spindexer) {
    return Commands.runOnce(
      () -> {spindexer.toggleDirection();}, spindexer);
  }

  public static Command spindexStartCommand(Spindexer spindexer) {
    return Commands.runOnce(
      () -> {spindexer.setRunning(true);}, spindexer);
  }

  public static Command spindexForwardDirectionCommand(Spindexer spindexer) {
    return Commands.runOnce(
      () -> {spindexer.setClockwise(true);}, spindexer);
  }

  public static Command spindexStopCommand(Spindexer spindexer) {
    return Commands.runOnce(
      () -> {spindexer.setRunning(false);}, spindexer);
  }

  public static Command spindexReverseDirectionCommand(Spindexer spindexer) {
    return Commands.runOnce(
      () -> {spindexer.setClockwise(false);}, spindexer);
  }

  public static Command fullSendCommand(Shooter shooter, Feeder feeder, Spindexer spindexer, Rotator rotator) {
    return Commands.run(
      () -> {shooter.runShooter(shooter.calculatePower());
      rotator.setShooting(true);
    }, shooter
    ).alongWith(Commands.run(
      () -> {if(Math.abs(shooter.getFilteredAcceleration()) < shooter.getAccelerationThreshold() 
          && rotator.isAligned()) {
        feeder.runFeeder();
      }}, feeder)
    ).alongWith(Commands.runOnce(() -> spindexer.setClockwise(true), spindexer
    ).andThen(Commands.run(() -> {
      if(Math.abs(shooter.getFilteredAcceleration()) < shooter.getAccelerationThreshold()) {
        spindexer.setRunning(true);
      }
    }, spindexer))).finallyDo(() -> {
      shooter.stopShooter();
      feeder.stopFeeder();
      spindexer.setRunning(false);
      rotator.setShooting(false);
    });
  }

    public static Command fullSendCommandOpen(Shooter shooter, Feeder feeder, Spindexer spindexer, Rotator rotator) {
    return Commands.run(
      () -> {shooter.runShooter(-0.7);
      rotator.setShooting(true);
    }, shooter
    ).alongWith(Commands.run(
      () -> {if(Math.abs(shooter.getFilteredAcceleration()) < shooter.getAccelerationThreshold() 
          && rotator.isAligned()) {
        feeder.runFeeder();
      }}, feeder)
    ).alongWith(Commands.runOnce(() -> spindexer.setClockwise(true), spindexer
    ).andThen(Commands.run(() -> {
      if(Math.abs(shooter.getFilteredAcceleration()) < shooter.getAccelerationThreshold()) {
        spindexer.setRunning(true);
      }
    }, spindexer))).finallyDo(() -> {
      shooter.stopShooter();
      feeder.stopFeeder();
      spindexer.setRunning(false);
      rotator.setShooting(false);
    });
  }

  public static Command autoFullSendCommand(Shooter shooter, Feeder feeder, Spindexer spindexer, Rotator rotator) {
    return Commands.runOnce(
      () -> {shooter.resetSensorAccumulation();}, shooter
    ).andThen(Commands.run(
        () -> {
            shooter.runShooter(shooter.calculatePower());
            rotator.setShooting(true);
        }, shooter)
    ).alongWith(Commands.run(
        () -> {
            if (Math.abs(shooter.getFilteredAcceleration()) < shooter.getAccelerationThreshold()
                    && rotator.isAligned()) {
                feeder.runFeeder();
            }
        }, feeder)
    ).alongWith(
        Commands.runOnce(() -> spindexer.setClockwise(true), spindexer)
        .andThen(Commands.run(() -> {
            if (Math.abs(shooter.getFilteredAcceleration()) < shooter.getAccelerationThreshold()) {
                spindexer.setRunning(true);
            }
        }, spindexer))
    ).until(shooter::sensorTimeout  // ends the command
    ).finallyDo(() -> {
        shooter.stopShooter();
        feeder.stopFeeder();
        spindexer.setRunning(false);
        rotator.setShooting(false);
    });
}


  public static Command unjam(Spindexer spindexer, Feeder feeder){
    return Commands.runOnce(
      () -> {spindexer.setClockwise(false);
        spindexer.setRunning(true);
      }, 
      spindexer)
      .alongWith(Commands.run(() -> {feeder.reverseFeeder();}, feeder))
      .finallyDo(
        () -> {feeder.stopFeeder();
        spindexer.setRunning(false);
      spindexer.setClockwise(true);});
  }
}
