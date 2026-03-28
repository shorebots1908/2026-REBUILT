package frc.robot.subsystems.turret;

import static frc.robot.subsystems.turret.TurretConstants.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.turret.Rotator;

public class Shooter extends SubsystemBase{
  private TalonFX shooterMotor;
  private TalonFX shooterMotor2;
  private double shooterAccelerationThreshold;
  private LinearFilter accelerationFilter = LinearFilter.singlePoleIIR(0.2, 0.02);
  private double filteredAcceleration = 0.0;
  private Rotator rotator;
  private double calculatedPower = 0.0;
  private double targetDistance = 0.0;
  private double oomph = 0.0;
  final VoltageOut m_request = new VoltageOut(0);
  private final AnalogInput ballSensor;


  public Shooter(Rotator _rotator) {
    shooterMotor = new TalonFX(shooterID);
    shooterMotor2 = new TalonFX(shooterID2);
    rotator = _rotator;
    ballSensor = new AnalogInput(0);
    this.shooterAccelerationThreshold = TurretConstants.shooterAccelerationThreshold;
    var motorConfig1 = new TalonFXConfiguration();
    var motorOutputConfig1 = motorConfig1.MotorOutput;
    motorOutputConfig1.Inverted = InvertedValue.valueOf(1);
    var motorConfig2 = new TalonFXConfiguration();
    var motorOutputConfig2 = motorConfig2.MotorOutput;
    motorOutputConfig2.Inverted = InvertedValue.valueOf(0);
    if(shooterMotorInversionSwapped) {
      shooterMotor.getConfigurator().apply(motorConfig1);
      shooterMotor2.getConfigurator().apply(motorConfig2);
    }
    else {
      shooterMotor.getConfigurator().apply(motorConfig2);
      shooterMotor2.getConfigurator().apply(motorConfig1);
    }
  }

  public double getAccelerationThreshold() {
    return shooterAccelerationThreshold;
  }

  public void runShooter(double speed){
    shooterMotor.setControl(m_request.withOutput(shooterNominalVoltage * speed));
    shooterMotor2.setControl(m_request.withOutput(shooterNominalVoltage * speed));
    runFilter();
  }

  public void runShooter() {
    shooterMotor.set(shooterSpeed);
    shooterMotor2.set(shooterSpeed);
    runFilter();
  }

  public void stopShooter() {
    shooterMotor.stopMotor();
    shooterMotor2.stopMotor();
  }

  public double calculatePower() {
    if(rotator.getOutsideTeamZone()) {
      targetDistance = rotator.revisedTargetDistance();
      calculatedPower = Math.min(Math.max((-((passingDistanceCoefficient * targetDistance) + shooterDistanceIntercept)), shooterMaximumPower), shooterMinimumPower);
      return calculatedPower - oomph;
    }
    else {
      targetDistance = rotator.revisedTargetDistance();
      calculatedPower = Math.min(Math.max((-((shooterDistanceCoefficient * targetDistance) + shooterDistanceIntercept)), shooterMaximumPower), shooterMinimumPower);
      return calculatedPower - oomph;
    }
  }

  public double getSpeed() {
    return shooterMotor.getRotorVelocity().getValueAsDouble();
  }

  public double getAcceleration() {
    return shooterMotor.getAcceleration().getValueAsDouble();
  }

  public double getFilteredAcceleration() {
    return filteredAcceleration;
  }

  private void runFilter() {
    filteredAcceleration = accelerationFilter.calculate(getAcceleration());
  }

  public void addOomph(double _oomph) {
    oomph = _oomph;
  }

  @Override
  public void periodic() {
    
    SmartDashboard.putNumber("Filtered Shooter Acceleration", filteredAcceleration);
    SmartDashboard.putNumber("Calculated Shooter Power", calculatePower());
    SmartDashboard.putNumber("ballSensor value", ballSensor.getValue());
  }

  public void primeShooter(){
    shooterMotor.set(shooterPrespinPower);
    shooterMotor2.set(shooterPrespinPower);
  }
  
}
