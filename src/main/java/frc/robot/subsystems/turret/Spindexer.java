package frc.robot.subsystems.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.spark.config.SparkBaseConfig;
//import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;

import static frc.robot.subsystems.turret.TurretConstants.spindexID;
import static frc.robot.subsystems.turret.TurretConstants.spinnerDefaultSpeed;

//import com.revrobotics.PersistMode;
//import com.revrobotics.ResetMode;
//import com.revrobotics.spark.FeedbackSensor;

import frc.robot.subsystems.turret.TurretConstants.*;

public class Spindexer extends SubsystemBase{
  private boolean isRunning = false;
  private boolean clockwise = true;
  private SparkMax spinner;

  // Stall detection
  private static final double STALL_CURRENT_THRESHOLD = 40.0; // amps — tune this on the real robot
  private static final int STALL_CYCLE_COUNT = 25; // consecutive cycles above threshold (100ms)
  private static final int REVERSE_CYCLES = 25; // 0.5s at 20ms per cycle

  private int stallCounter = 0;
  private int reverseCounter = 0;
  private boolean isReversing = false;


  public Spindexer(){
    spinner = new SparkMax(spindexID, MotorType.kBrushless);
    //spinner.configure(new SparkMaxConfig().apply(), ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
  }
  public void openLoopSpin(double speed){
    spinner.set(speed);
  }
  public void stopSpinner(){
    spinner.stopMotor();
  }
  // this is the original code block for run spindexer
  // public void runSpinner(){
  //   if(isRunning){
  //     openLoopSpin(clockwise ? spinnerDefaultSpeed : -spinnerDefaultSpeed);
  //     SmartDashboard.putNumber("spindexer current", spinner.getOutputCurrent());
  //   }
  //   else{
  //     stopSpinner();
  //   }
  // }

  public void runSpinner() { // new jam detection
    if (!isRunning) {
    stopSpinner();
    stallCounter = 0;
    reverseCounter = 0;
    isReversing = false;
    return;
    }

    if (isReversing) {
    // Run opposite direction briefly
    openLoopSpin(clockwise ? (-spinnerDefaultSpeed * 0.5) : (spinnerDefaultSpeed * 0.5)); // half speed
    reverseCounter++;
    if (reverseCounter >= REVERSE_CYCLES) {
    isReversing = false;
    reverseCounter = 0;
    stallCounter = 0;
    }
    } else {
    // Normal operation
    openLoopSpin(clockwise ? spinnerDefaultSpeed : -spinnerDefaultSpeed);

    // Stall detection
    if (spinner.getOutputCurrent() > STALL_CURRENT_THRESHOLD) {
    stallCounter++;
    } else {
    stallCounter = 0;
    }

    if (stallCounter >= STALL_CYCLE_COUNT) {
    isReversing = true;
    reverseCounter = 0;
    stallCounter = 0;
    }
    }

    SmartDashboard.putNumber("spindexer applied output", spinner.getAppliedOutput());
    SmartDashboard.putNumber("spindexer current", spinner.getOutputCurrent());
    SmartDashboard.putBoolean("spindexer reversing", isReversing);
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

  @Override
  public void periodic() {
    runSpinner();
  }

}
