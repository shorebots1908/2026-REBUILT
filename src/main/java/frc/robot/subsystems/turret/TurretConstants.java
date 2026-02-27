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
    public static final int pitchChannel1 = 0;
    public static final int pitchChannel2 = 1;
    public static final double spinnerDefaultSpeed = 0.5;
    public static final double feederSpeed = 0.6;
    public static final double shooterSpeed = -0.7;
    public static final double shooterAccelerationThreshold = 0.1;
    public static final double turretMaxRotationSpeed = 80;
    public static final double turretMaxRotationAcceleration = 160;
    public static final Transform2d turretOffSet = new Transform2d(Units.inchesToMeters(-5.5), Units.inchesToMeters(4.75), new Rotation2d());
    public static final Translation2d targetPoint = new Translation2d(Units.inchesToMeters(182.11), Units.inchesToMeters(158.84));
    public static final Translation2d teamAreaPoint = new Translation2d(Units.inchesToMeters(182.11), Units.inchesToMeters(50));
    //public static final Translation2d  
    public static final Rotation2d turretZeroOffset = new Rotation2d(Units.degreesToRadians(45));
    public static final double rotatorP = 35.0;
    public static final double rotatorI = 0.0;
    public static final double rotatorD = 0.0;
    public static final double rotatorS = 0.1;
    public static final double rotatorGearRatio = 48.0; //motor spins per turret revolution
    public static final double allowableRotatorError = 0.005; 
    public static final double targetAlignmentError = 0.02;
    public static final double rotatorMinLimit = 0;
    public static final double rotatorMaxLimit = 0.75;
    public static final boolean rotatorContinuousWrap = false;
    public static final double pitchCoefficientFeet = 1.05; //increase to make distance factor into pitch more
    public static final double pitchInterceptFeet = 11.0; //reduce to make pitch response start closer to target
    public static final double passingPitchHeight = 0.6; //current measurements indicate greatest distance at 0.6. 
}   
