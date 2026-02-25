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
    public boolean atLower = true;
    public double targetEncoderPosition = 0;
    public boolean isUp = false;
    private boolean autoFlipped = false;
    private boolean isRunning = false;

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

    public void deployIntake(double speed){
        deploymentIntake.set(speed);
    }

    public void stopDeployIntake(){
        deploymentIntake.stopMotor();
    }

    public void setIsUp(boolean _isUp){
      isUp = _isUp;
    }

    public void toggleIsUp(){
      isUp = !isUp;
    }

    public boolean getIsRunning() {
      return isRunning;
    }
    
    public void setIsRunning(boolean _isRunning) {
      isRunning = _isRunning;
    }

    public void toggleIsRunning() {
      isRunning = !isRunning;
    }

    private void stopAtTop() {
      deploymentIntake.stopMotor();
        atLower = false; 
        atUpper = true;
    }

    private void stopAtBottom() {
      deploymentIntake.stopMotor();
        atLower = true; 
        atUpper = false;
    }

    public void oneButtonDeploy(){
      if(isUp){
        if (!autoFlipped){
          if (deploymentIntake.getPosition().getValueAsDouble() > -(intakeDeployRange - deployRangeError)) {
            deploymentIntake.set(intakeDeploySpeed);
          }
          else
          {
            stopAtTop();
          }
          if(deploymentIntake.getFault_StatorCurrLimit().getValue()){
            autoFlipped = true;
          }
        }
        else {
          if (deploymentIntake.getPosition().getValueAsDouble() < (intakeDeployRange - deployRangeError)) {
            deploymentIntake.set(-intakeDeploySpeed);
          }
          else {
            stopAtTop();
          }
        }
      }
      else {
        if(!autoFlipped){
          if(deploymentIntake.getPosition().getValueAsDouble() < -(0 + deployRangeError)) {
            deploymentIntake.set(-intakeDeploySpeed);
          }
          else {
            stopAtBottom();
          }
        }
        else {
          if(deploymentIntake.getPosition().getValueAsDouble() > (0 + deployRangeError)) {
            deploymentIntake.set(intakeDeploySpeed);
          }
          else {
            stopAtBottom();
          }
        }
      }
      if(isRunning) {
        runIntake(intakeSpeed);
      }
      else {
        stopIntake();
      }
    }
}
