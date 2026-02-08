package frc.robot.subsystems.turret;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import static frc.robot.subsystems.turret.TurretConstants.spindexID;

import com.revrobotics.spark.FeedbackSensor;

import frc.robot.subsystems.turret.TurretConstants.*;

public class Spindexer extends SubsystemBase{
  public boolean isRunning = false;
  public boolean clockwise = true;
  public SparkMax spinner;

  public Spindexer(){
    spinner = new SparkMax(spindexID, MotorType.kBrushless);

  }
}
