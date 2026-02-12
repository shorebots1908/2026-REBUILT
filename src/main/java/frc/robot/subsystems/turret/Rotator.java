package frc.robot.subsystems.turret;

import static frc.robot.subsystems.turret.TurretConstants.*;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.FeedbackSensor;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

public class Rotator extends SubsystemBase {
  private boolean isClosedLoop = false;
  private Rotation2d targetRotation;
  public TalonFX turretRotator;
  final TrapezoidProfile m_profile = new TrapezoidProfile(
    new TrapezoidProfile.Constraints(turretMaxRotationSpeed, turretMaxRotatiionAcceleration)
  );
  private TrapezoidProfile.State m_goal = new TrapezoidProfile.State();
  private TrapezoidProfile.State m_setpoint = new TrapezoidProfile.State();

    public Rotator() {
        turretRotator = new TalonFX(rotatorID);
        targetRotation = new Rotation2d();
        var motorConfig = new TalonFXConfiguration();
        var slot0Configs = motorConfig.Slot0;
        slot0Configs.kP  = 3;
        slot0Configs.kI = 0;
        slot0Configs.kD = 0;

        turretRotator.getConfigurator().apply(slot0Configs);
        //m_profile.c
    }

    public void setTurretRotationOpenLoop(double speed) {
      turretRotator.set(speed);
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
