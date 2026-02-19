package frc.robot.subsystems.climb;

import static frc.robot.subsystems.climb.ClimbConstants.*;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climb extends SubsystemBase {
    private final SparkMax climbMotor;

    public Climb() {
        climbMotor = new SparkMax(climbMotorID, MotorType.kBrushless);
    }

    public void runClimb(double speed) {
        climbMotor.set(speed);
    }

    public void stopClimb() {
        climbMotor.stopMotor();
    }
}