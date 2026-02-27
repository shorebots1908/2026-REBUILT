package frc.robot.subsystems.turret;

import static frc.robot.subsystems.turret.TurretConstants.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;


public class Shooter extends SubsystemBase{
  private TalonFX shooterMotor;
  private double shooterAccelerationThreshold;
  private LinearFilter accelerationFilter = LinearFilter.singlePoleIIR(0.2, 0.02);
  private double filteredAcceleration = 0.0;

  public Shooter() {
    shooterMotor = new TalonFX(shooterID);
    this.shooterAccelerationThreshold = TurretConstants.shooterAccelerationThreshold;
  }

  public double getAccelerationThreshold() {
    return shooterAccelerationThreshold;
  }

  public void runShooter(double speed){
    shooterMotor.set(speed);
    runFilter();
  }

  public void runShooter() {
    shooterMotor.set(shooterSpeed);
    runFilter();
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

  public double getFilteredAcceleration() {
    return filteredAcceleration;
  }

  private void runFilter() {
    filteredAcceleration = accelerationFilter.calculate(getAcceleration());
  }

  @Override
  public void periodic() {
    
    SmartDashboard.putNumber("Filtered Shooter Acceleration", filteredAcceleration);
  }
  
}
