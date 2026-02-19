package frc.robot.subsystems.turret;

import static frc.robot.subsystems.turret.TurretConstants.*;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.Drive;

import com.revrobotics.spark.FeedbackSensor;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

public class Rotator extends SubsystemBase {
  private boolean isClosedLoop = false;
  private Rotation2d targetRotation;
  public TalonFX turretRotator;
  final TrapezoidProfile m_profile = new TrapezoidProfile(
    new TrapezoidProfile.Constraints(turretMaxRotationSpeed, turretMaxRotationAcceleration)
  );
  // private TrapezoidProfile.State m_goal = new TrapezoidProfile.State();
  // private TrapezoidProfile.State m_setpoint = new TrapezoidProfile.State();
  private PositionVoltage m_request;
  private Drive drive;
  private Pose2d robotPose = new Pose2d();

    public Rotator() {
      rotatorInit();
    }

    public Rotator(Drive _drive) {
      drive = _drive;
      rotatorInit();
    }

    private void rotatorInit() {
      turretRotator = new TalonFX(rotatorID);
      targetRotation = new Rotation2d();
      var motorConfig = new TalonFXConfiguration();
      var slot0Configs = motorConfig.Slot0;
      slot0Configs.kP = rotatorP;
      slot0Configs.kI = rotatorI;
      slot0Configs.kD = rotatorD;
      var motorFeedbackConfig = motorConfig.Feedback;
      motorFeedbackConfig.SensorToMechanismRatio = rotatorGearRatio;

      turretRotator.getConfigurator().apply(motorConfig);
      m_request = new PositionVoltage(0).withSlot(0);
    }

    public boolean getIsClosedLoop(){
      return isClosedLoop;
    }

    public void setIsClosedLoop(boolean closedLoop){
      isClosedLoop = closedLoop;
    }

    public void startClosedLoop() {
      isClosedLoop = true;
    }

    public void stopClosedLoop() {
      isClosedLoop = false;
    }

    public void toggleOpenClosedLoop() {
      isClosedLoop = !isClosedLoop;
    }

    public void setTurretRotationOpenLoop(double speed) {
      if(!isClosedLoop) {
        turretRotator.set(speed);
      }
      //io.setTurretRotationOpenLoop(output);
    }
    public void setTarget(Rotation2d _targetRotation){
      targetRotation = _targetRotation;
      //m_goal = new TrapezoidProfile.State(targetRotation.getRotations(), 0);
    }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    /* TODO: break out different commands for calculating target point 
     * based on different field positions and call them when the field position changes. 
     * TODO: get flag boolean for if turret is rotating and off target (target deviation
     * is above threshold.) If target deviation is above threshold, feeder should be 
     * blocked from feeding balls into the shooter. 
     * TODO: use drive odometry to get current vector and alter turret rotation accordingly
     * based on distance and estimated flight time. 
     * TODO: break out functions for calculating aiming so that they can be easily called 
     * in both the rotator and the pitch controller, for more fine control of game pieces. 
     */
    if(isClosedLoop){
      //set robot pose as this value will be reused
      robotPose = drive.getPose();

      //Calculate what the target parameter for the closedloop control should be
      //first, get the target field angle relative to the turret center
      Rotation2d targetTurretRelativeAngle = (targetPoint //constant. TODO: Need to swap based on team
        .minus(robotPose
          .plus(turretOffSet) //add turret position relative to the robot center to get the point we should aim from
          .getTranslation())) //get only the translation component of the joint robot and turret pose
        .getAngle(); //convert to an angle. 

      //second, get the turret orientation in field space
      Rotation2d turretFieldRelativeRotation = (robotPose
          .getRotation())
        .plus(turretZeroOffset);

      //finally, set the target rotation equal to the difference between the two, giving how far to rotate
      //in order to get the turret to point at the target. 
      targetRotation = targetTurretRelativeAngle.minus(turretFieldRelativeRotation);
      //setTarget(targetRotation);
      // m_setpoint = m_profile.calculate(0.020, m_setpoint, m_goal);
      // m_request.Position = m_setpoint.position;
      // m_request.Velocity = m_setpoint.velocity;
      turretRotator.setControl(m_request.withPosition(targetRotation.getMeasure()));
      //turretRotator.setControl(m_request.withPosition(60));
    }
    SmartDashboard.putNumber("turret position", turretRotator.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("target turret position", targetRotation.getRotations());
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
