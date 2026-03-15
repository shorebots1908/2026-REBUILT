package frc.robot.subsystems.intake;

import static frc.robot.subsystems.intake.IntakeConstants.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class Intake extends SubsystemBase {
  public TalonFX intake;
  public TalonFX deploymentIntake;
    public double positionRad = 0;
    public double velocityRadPerSec = 0;
    public boolean atUpper = true;
    public boolean atLower = false;
    public double targetEncoderPosition = 0;
    public boolean isUp = true;
    // private boolean autoFlipped = false;
    // private boolean hasFaultedMaximum = false;
    private double faultedMaximum = 0.0;
    private boolean isRunning = false;

    private boolean shakeForwardFlag = false;

    public Intake(){
        intake = new TalonFX(intakeID);
        deploymentIntake = new TalonFX(intakeDeployID);
        if (deploymentIntake.getPosition().getValueAsDouble() < -5) {
          atUpper = false;
          atLower = true;
          isUp = false;
        }
        var motorConfig = new TalonFXConfiguration();
        motorConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        deploymentIntake.getConfigurator().apply(motorConfig);
    }

    public void runIntake(double speed){
        intake.set(speed);
    }

    public void runIntake() {
      intake.set(intakeSpeed);
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

    public void stopAtTop() {
      deploymentIntake.stopMotor();
        atLower = false; 
        atUpper = true;
    }

    public void stopAtBottom() {
      deploymentIntake.stopMotor();
        atLower = true; 
        atUpper = false;
    }

    public void undeployIntake() {
      if(isUndeployed()) {
        stopAtTop();
      }
      else {
        deploymentIntake.set(-intakeDeploySpeed);
      }
    }

    public void deployIntake() {
      if(isDeployed()) {
        stopAtBottom();
      }
      else {
        deploymentIntake.set(intakeDeploySpeed);
      }
    }

    public void shakeLift(){
      if (isLifted()) {
        deploymentIntake.stopMotor(); 
        shakeForwardFlag = true;
      }
      else {
        deploymentIntake.set(-intakeShakeSpeed);
      }
    }

    public void shakeLower(){
      if (isDeployed()) {
        deploymentIntake.stopMotor();
        shakeForwardFlag = false;
      }
      else{
        deploymentIntake.set(intakeShakeSpeed/2);
      }
    }

    public void shake(){
      if (shakeForwardFlag) {
        shakeLower();
      }
      else{
        shakeLift();
      }
    }

    public boolean isDeployed() {
      return deploymentIntake.getPosition().getValueAsDouble() < (intakeDeployRange + deployRangeError);
    }

    public boolean isUndeployed() {
      return deploymentIntake.getPosition().getValueAsDouble() > (0 - deployRangeError);
    }

    public boolean isLifted(){
      return deploymentIntake.getPosition().getValueAsDouble() > (intakeDeployRange * (1 - intakeLiftScale));
    }

    public void oneButtonDeploy(){
      if(isUp) {
        undeployIntake();
      }
      else {
        deployIntake();
      }
    }



    @Override
    public void periodic() {
      //oneButtonDeploy(); 
      SmartDashboard.putNumber("Intake Encoder", deploymentIntake.getPosition().getValueAsDouble());
    }
}
