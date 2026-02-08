package frc.robot.subsystems.turret;

import static frc.robot.subsystems.turret.TurretConstants.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.FeedbackSensor;
import com.ctre.phoenix6.hardware.TalonFX;

public class Feeder extends SubsystemBase{
  public TalonFX feeder;

  public Feeder() {
    feeder = new TalonFX(feederID);
  }

  public void runFeeder(double speed) {
    feeder.set(speed);
  }

  public void stopFeeder() {
    feeder.stopMotor();
  }
}
