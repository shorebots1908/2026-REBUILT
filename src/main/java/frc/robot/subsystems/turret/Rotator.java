package frc.robot.subsystems.turret;

import static frc.robot.subsystems.turret.TurretConstants.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.FeedbackSensor;
import com.ctre.phoenix6.hardware.TalonFX;

public class Rotator extends SubsystemBase {

  public TalonFX turretRotator;

    public Rotator() {
        turretRotator = new TalonFX(rotatorID);
    }

    public void setTurretRotationOpenLoop(double output) {
      //io.setTurretRotationOpenLoop(output);
    }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
