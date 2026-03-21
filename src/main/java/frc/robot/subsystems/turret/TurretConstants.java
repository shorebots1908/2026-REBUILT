package frc.robot.subsystems.turret;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public class TurretConstants {
    public static final int rotatorID = 26;
    public static final int spindexID = 28;
    public static final int feederID = 27;
    public static final int shooterID = 24;
    public static final int shooterID2 = 9; //motor for double motor shooter
    public static final boolean shooterMotorInversionSwapped = true;
    public static final int pitchChannel1 = 0;
    public static final int pitchChannel2 = 1;
    public static final double spinnerDefaultSpeed = 0.9; 
    public static final double feederSpeed = 0.6;
    public static final double shooterSpeed = -0.9; //was -0.7
    public static final double shooterAccelerationThreshold = 0.2;
    public static final double turretMaxRotationSpeed = 80;
    public static final double turretMaxRotationAcceleration = 160;
    public static final Transform2d turretOffSet = new Transform2d(Units.inchesToMeters(-5.5), Units.inchesToMeters(4.75), new Rotation2d());
    public static final Rotation2d turretPositionRotation = turretOffSet.getTranslation().getAngle();
    public static final double turretOffsetDistance = turretOffSet.getTranslation().getDistance(new Translation2d());
    public static final Translation2d targetPoint = new Translation2d(Units.inchesToMeters(182.11), Units.inchesToMeters(158.84));
    public static final Translation2d teamAreaPoint = new Translation2d(Units.inchesToMeters(150.0), Units.inchesToMeters(90));
    public static final double fieldLength = (Units.inchesToMeters(651.22));
    public static final double fieldWidth = (Units.inchesToMeters(317.69));
    public static final Rotation2d turretZeroOffset = new Rotation2d(Units.degreesToRadians(0));
    public static final double rotatorP = 30.0; //was 35.0
    public static final double rotatorI = 1.0;
    public static final double rotatorD = 0.1;
    public static final double rotatorS = 0.1;
    public static final double rotatorGearRatio = 48.0; //motor spins per turret revolution
    public static final double allowableRotatorError = 0.005; 
    public static final double targetAlignmentError = 0.02;
    public static final double rotatorMinLimit = 0;
    public static final double rotatorMaxLimit = Units.degreesToRotations(359);
    public static final boolean rotatorContinuousWrap = false;
    //public static final double pitchCoefficientFeet = 1.05; //increase to make distance factor into pitch more
    //public static final double pitchInterceptFeet = 9.5; //reduce to make pitch response start closer to target
    //public static final double passingPitchHeight = 0.2; //current measurements indicate greatest distance at 0.6. //editing to 0.0 for wk0 safety purposes
    public static final double shooterDistanceCoefficient = 0.065; // -- determining power relative to distance from target. directly proportional. -- 0.0832 is calculated value for target on ground 
    public static final double shooterDistanceIntercept = 0.175; // -- determining starting amount that is adding to the power -- 0.0452 is calculated value for target on ground
    public static final double passingDistanceCoefficient = 0.0832;
    public static final double timeCoefficient = 0.225; // -- relationship between distance to target and ball flight time -- used to counteract robot motion (should lower to make it correct less) --initial calculated as 0.225
    public static final double timeIntercept = 0.385; // starting amount that we get from any thrown ball in terms of time -- used same as above () -- initial calculated 0.385
    public static final double shooterPrespinPower = -0.3; //was -0.4. changed because shots seemed to be overpowered
    public static final double shooterMinimumPower = -0.3;
    public static final double shooterMaximumPower = -0.9; //ADJUST THIS!!!!!!!!!!!
  }   

