package frc.robot.subsystems.turret;

import static frc.robot.subsystems.turret.TurretConstants.*;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pitch extends SubsystemBase{
  private double actuatorValue = 0.0;
  private Servo linA1, linA2;

  public Pitch() {
    linA1 = new Servo(pitchChannel1);
    linA2 =  new Servo(pitchChannel2);
  }

  public void setPitch(double pitchFactor) {
    actuatorValue = pitchFactor;
    linA1.set(pitchFactor);
    linA2.set(pitchFactor);
  }

  public void increasePitch(double increase) {
    actuatorValue += increase;
    actuatorValue = Math.min(actuatorValue, 1.0);
    linA1.set(actuatorValue);
    linA2.set(actuatorValue);
  }

  public void decreasePitch(double decrease) {
    actuatorValue -= decrease;
    actuatorValue = Math.max(actuatorValue, 0.0);
    linA1.set(actuatorValue);
    linA2.set(actuatorValue);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("pitch output", actuatorValue);
  } 
}
