package frc.robot.subsystems.turret;

import static frc.robot.subsystems.turret.TurretConstants.*;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.Drive;

public class Pitch extends SubsystemBase{
  private double actuatorValue = 0.2; //minimum control value for the linear actuator
  private Servo linA1, linA2;
  public boolean isAutoPitched = false;
  private Drive drive;
  private Rotator rotator;
  private double pitchCoefficient = Units.feetToMeters(1.05);
  private double pitchIntercept = Units.feetToMeters(13.1);

  public Pitch(Drive _drive, Rotator _rotator) {
    linA1 = new Servo(pitchChannel1);
    linA2 =  new Servo(pitchChannel2);
    drive = _drive;
    rotator = _rotator;
  }

  public void setPitch(double pitchFactor) {
    actuatorValue = pitchFactor;
    linA1.set(pitchFactor);
    linA2.set(pitchFactor);
  }

  public void increasePitch(double increase) {
    actuatorValue += increase;
    actuatorValue = Math.min(actuatorValue, 1.0);
    linA1.set(actuatorValue);
    linA2.set(actuatorValue);
  }

  public void decreasePitch(double decrease) {
    actuatorValue -= decrease;
    actuatorValue = Math.max(actuatorValue, 0.0);
    linA1.set(actuatorValue);
    linA2.set(actuatorValue);
  }
  
  public void setIsAutoPitched(boolean _isAutoPitchedd){
    isAutoPitched = _isAutoPitchedd;
  }

  public void toggleIsAutoPitched(){
    isAutoPitched = !isAutoPitched;
  }

  public boolean getIsAutoPitched(){
    return isAutoPitched;
  }

  public void passingPitch() {
    setPitch(0.2);
  }

  public void defaultPitchMethod() {
    if(isAutoPitched) {

      //determine the distance from the cernter of the turret to the target
      double targetDistance = (
        rotator.getCurrentTarget()
        .getDistance(drive.getPose()
          .plus(turretOffSet) //add turret position relative to the robot center to get the point we should aim from
          .getTranslation()));

      double targetPitchFactor = Math.min(Math.max((((targetDistance - pitchIntercept) / pitchCoefficient)/10.0 + 0.1), 0.2), 0.8);
      SmartDashboard.putNumber("ClosedLoop PitchFactor", targetPitchFactor);
      if(rotator.getOutsideTeamZone()){
        setPitch(passingPitchHeight);
      }
      else{
        setPitch(targetPitchFactor);
      }
    }
  } 

  @Override
  public void periodic() {
  }
}
