package frc.robot.subsystems.turret;

import com.revrobotics.RelativeEncoder;
import frc.robot.subsystems.BasePosition;
import org.littletonrobotics.junction.AutoLog;

public interface RotatorIO {
    @AutoLog
    public static class RotatorIOInputs {
        public double positionRad = 0;
        public double velocityRadPerSec = 0;
        public boolean atUpper = false;
        public boolean atLower = false;
        public double targetEncoderPosition = 0;
        public boolean atTarget = false;

    }

    public default void periodic() {}

    public default void updateInputs(RotatorIOInputs inputs) {}

    public default RelativeEncoder getEncoder() {
        return null;
    }

    public default void setTargetPosition(BasePosition position) {}

  public default boolean atTargetPosition() {
    return false;
  }

  public default void setTurretRotationOpenLoop(double output) {}

  public default void stop() {}

  public default TurretConfig getConfig() {
    return null;
  }
}
