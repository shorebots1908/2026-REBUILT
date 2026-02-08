package frc.robot.subsystems.intake;

import static frc.robot.subsystems.intake.IntakeConstants.*;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.FeedbackSensor;

public class Intake extends SubsystemBase {
    public TalonFX intake;
    public TalonFX deploymentIntake;
        public double positionRad = 0;
        public double velocityRadPerSec = 0;
        public boolean atUpper = false;
        public boolean atLower = false;
        public double targetEncoderPosition = 0;
        public boolean atTarget = false;
        public double intakeSpeed = 0;

        public Intake(){
            intake = new TalonFX(intakeID);
            deploymentIntake = new TalonFX(intakeDeployID);
        }

        public void runIntake(double speed){
            intake.set(speed);
        }

        public void stopIntake(){
            intake.stopMotor();
        }
}
