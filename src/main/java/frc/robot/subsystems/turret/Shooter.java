package frc.robot.subsystems.turret;

import static frc.robot.subsystems.turret.TurretConstants.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;


public class Shooter extends SubsystemBase{
  private TalonFX shooterMotor;
  private double shooterAccelerationThreshold;

  public Shooter() {
    shooterMotor = new TalonFX(shooterID);
    this.shooterAccelerationThreshold = TurretConstants.shooterAccelerationThreshold;
  }

  public double getAccelerationThreshold() {
    return shooterAccelerationThreshold;
  }

  public void runShooter(double speed){
    shooterMotor.set(speed);
  }

  public void runShooter() {
    shooterMotor.set(shooterSpeed);
  }

  public void stopShooter() {
    shooterMotor.stopMotor();
  }

  public double getSpeed() {
    return shooterMotor.getRotorVelocity().getValueAsDouble();
  }

  public double getAcceleration() {
    return shooterMotor.getAcceleration().getValueAsDouble();
  }
  
}
