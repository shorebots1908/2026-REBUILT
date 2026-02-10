package frc.robot.subsystems.turret;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;

import static frc.robot.subsystems.turret.TurretConstants.spindexID;
import static frc.robot.subsystems.turret.TurretConstants.spinnerDefaultSpeed;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;

import frc.robot.subsystems.turret.TurretConstants.*;

public class Spindexer extends SubsystemBase{
  private boolean isRunning = false;
  private boolean clockwise = true;
  private SparkMax spinner;

  public Spindexer(){
    spinner = new SparkMax(spindexID, MotorType.kBrushless);
    spinner.configure(new SparkMaxConfig().apply(), ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
  }
  public void openLoopSpin(double speed){
    spinner.set(speed);
  }
  public void stopSpinner(){
    spinner.stopMotor();
  }
  public void runSpinner(){
    if(isRunning){
      openLoopSpin(clockwise ? spinnerDefaultSpeed : -spinnerDefaultSpeed);
    }
    else{
      stopSpinner();
    }
  }
  public void setRunning(boolean runState){
    isRunning = runState;
  }
  public void setClockwise(boolean _clockwise){
    clockwise = _clockwise;
  }
  public boolean getIsRunning(){
    return isRunning;
  }
  public boolean getClockwise(){
    return clockwise;
  }
  public void toggleRunning() {
    isRunning = !isRunning;
  }
  public void toggleDirection(){
    clockwise = !clockwise;
  }
}
