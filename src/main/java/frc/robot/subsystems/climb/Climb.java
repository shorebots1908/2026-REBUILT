package frc.robot.subsystems.climb;

import static frc.robot.subsystems.climb.ClimbConstants.*;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climb extends SubsystemBase {
    private final SparkMax climbMotor;

    public Climb() {
        climbMotor = new SparkMax(climbMotorID, MotorType.kBrushless);

        SparkMaxConfig config = new SparkMaxConfig();
        config.idleMode(IdleMode.kBrake);
        climbMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void runClimb(double speed) {
        climbMotor.set(speed);
    }

    public void stopClimb() {
        climbMotor.stopMotor();
    }
}