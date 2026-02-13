package frc.robot.subsystems.turret;

import static frc.robot.subsystems.turret.TurretConstants.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;


public class Shooter extends SubsystemBase{
  public TalonFX shooterMotor;
  public Shooter(){
    shooterMotor = new TalonFX(shooterID);
  }
  public void runShooter(double speed){
    shooterMotor.set(speed);
  }
  public void runShooter(){
    shooterMotor.set(shooterSpeed);
  }
  public void stopShooter(){
    shooterMotor.stopMotor();

  }
  public double getSpeed(){
    return shooterMotor.getRotorVelocity().getValueAsDouble();
  }
  
}
