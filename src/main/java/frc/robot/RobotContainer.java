// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.ClimbCommands;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.TurretCommands;
import frc.robot.commands.IntakeCommands;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.LED.LED;
import frc.robot.subsystems.turret.Feeder;
import frc.robot.subsystems.turret.Rotator;
import frc.robot.subsystems.turret.Shooter;
import frc.robot.subsystems.turret.Spindexer;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIOSpark;
import frc.robot.subsystems.vision.Vision;
import frc.robot.subsystems.vision.VisionConstants;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.subsystems.vision.VisionIOLimelight;
import frc.robot.subsystems.vision.VisionIOPhotonVisionSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import java.util.Optional;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final Climb climb;
  private final Drive drive;
  private final Intake intake;
  private final LED led;
  private final Spindexer spindexer;
  private final Rotator rotator;
  private final Shooter shooter;
  private final Vision vision;
  private final Feeder feeder;
  //private final Pitch pitch;
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController player1 =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);
  private final CommandXboxController player2 =
      new CommandXboxController(OperatorConstants.kDriverControllerPort1);
  //dashboard inputs
  private final SendableChooser<Command> autoChooser;
  private Optional<DriverStation.Alliance> alliance;
  


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    climb = new Climb();
    drive = initDrive();
    intake = new Intake();
    led = new LED(intake);
    spindexer = new Spindexer();
    rotator = new Rotator(drive);
    shooter = new Shooter(rotator);
    vision = initVision();
    feeder = new Feeder();
    //pitch = new Pitch(drive, rotator);
    
    registerNamedCommands();
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Choices", autoChooser);
    SmartDashboard.putString("AutoWinOverride", "");   // blank = use real game data
    configureBindings();

    // Try to get alliance at startup (may be empty if FMS hasn't assigned yet)
    alliance = DriverStation.getAlliance();
    if (alliance.isPresent()) {
      rotator.setAlliance(alliance);
    }
  }

  // Checks if the alliance has been assigned by the FMS and updates subsystems if it has changed.
  // We call this in disabledPeriodic, autonomousInit, and teleopInit to ensure we catch the alliance assignment from the FMS.
  public void checkAndUpdateAlliance() {
    Optional<DriverStation.Alliance> currentAlliance = DriverStation.getAlliance();
    // Update if we now have an alliance and either didn't before, or it changed
    if (currentAlliance.isPresent() && !currentAlliance.equals(alliance)) {
      alliance = currentAlliance;
      rotator.setAlliance(alliance);
      // Add any other subsystems that need alliance info here
    }
  }

private void registerNamedCommands() {
    NamedCommands.registerCommand("path", DriveCommands.followPath(drive, "Example"));
    NamedCommands.registerCommand("AbbyPath1", DriveCommands.followPath(drive, "AbbyPath1"));
    NamedCommands.registerCommand("AbbyAuto1", DriveCommands.followPath(drive, "AbbyAuto1"));
    NamedCommands.registerCommand("Swipe+Swipe Auto", DriveCommands.followPath(drive, "Swipe+Swipe Auto"));
    NamedCommands.registerCommand("fullSendCommand", 
        TurretCommands.fullSendCommand(shooter, feeder, spindexer, rotator).withTimeout(4.0));
    NamedCommands.registerCommand("fullSendCommand3Sec", 
        TurretCommands.fullSendCommand(shooter, feeder, spindexer, rotator).withTimeout(3.0));
    NamedCommands.registerCommand("fullSendCommand4.5Sec", 
        TurretCommands.fullSendCommand(shooter, feeder, spindexer, rotator).withTimeout(4.5));
    NamedCommands.registerCommand("fullSendCommand6Sec", 
        TurretCommands.fullSendCommand(shooter, feeder, spindexer, rotator).withTimeout(6.0));
    NamedCommands.registerCommand("fullSendCommand9Sec", 
        TurretCommands.fullSendCommand(shooter, feeder, spindexer, rotator).withTimeout(9.0));
    NamedCommands.registerCommand("fullSendCommandNoTimeout", 
        TurretCommands.fullSendCommand(shooter, feeder, spindexer, rotator));
    NamedCommands.registerCommand("toggleTargeting", 
        TurretCommands.toggleTargeting(rotator, shooter));
    NamedCommands.registerCommand("targetingIsOnCommand", 
        TurretCommands.targetingIsOnCommand(rotator, shooter));
    NamedCommands.registerCommand("intakeRunCommand", 
        IntakeCommands.intakeRunCommand(intake).withTimeout(3.0));
    NamedCommands.registerCommand("autoDeployIntake", 
        IntakeCommands.autoDeployIntake(intake));
    NamedCommands.registerCommand("autoUndeployIntake", 
        IntakeCommands.autoUndeployIntake(intake));
    NamedCommands.registerCommand("autoRunIntake", 
        IntakeCommands.autoRunIntake(intake).withTimeout(3.5));
    NamedCommands.registerCommand("autoRunIntake2Sec", 
        IntakeCommands.autoRunIntake(intake).withTimeout(2.0));
    NamedCommands.registerCommand("intakePumpFake", 
        IntakeCommands.autoIntakeShake(intake).withTimeout(4));
    // NamedCommands.registerCommand("autoDeployAndRunIntake", 
    //     IntakeCommands.autoDeployAndRunIntake(intake));
    NamedCommands.registerCommand("climbUp", 
        ClimbCommands.climbUp(climb).withTimeout(2));
    NamedCommands.registerCommand("climbDown", 
        ClimbCommands.climbDown(climb).withTimeout(2));
    NamedCommands.registerCommand("autoDeployAndRunIntake",
        IntakeCommands.autoDeployIntake(intake)
        .andThen(IntakeCommands.autoRunIntake(intake).withTimeout(5.5)));
}

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    drive.setDefaultCommand(
      DriveCommands.joystickDrive(drive, 
      () -> drive.isShooting() ? -0.7 * player1.getLeftY() : -player1.getLeftY(), //was -0.87
      () -> drive.isShooting() ? -0.7 * player1.getLeftX() : -player1.getLeftX(), //was -0.87
      () -> -player1.getRightX())
    );

    //spindexer.setDefaultCommand(TurretCommands.spindex(spindexer));
    player1.x().onTrue(TurretCommands.spindexCommand(spindexer));
    player1.y().whileTrue(TurretCommands.unjam(spindexer, feeder));

    // climber commands
    player1.povUp().whileTrue(ClimbCommands.climbUp(climb));
    player1.povDown().whileTrue(ClimbCommands.climbDown(climb));

    //Intake Default Command function has been implemented inside the subsystem's periodic function.
    intake.setDefaultCommand(IntakeCommands.intakeDefaultCommand(intake));

    shooter.setDefaultCommand(TurretCommands.primeShooter(shooter));

    player1.a()
      .onTrue(
        IntakeCommands.intakeRunCommand(intake)
      );
    
    player1.b()
      .onTrue(
        IntakeCommands.intakeDeployCommand(intake)
      );

    

    // player1.rightBumper()
    //   .whileTrue(
    //     Commands.run(() -> shooter.runShooter(), shooter)
    //       .finallyDo(() -> shooter.stopShooter())
    //   );
      
    player1.leftBumper()
      .whileTrue(TurretCommands.fullSendCommand(shooter, feeder, spindexer, rotator)
      );
    player1.rightBumper()
      .whileTrue(TurretCommands.fullSendCommandOpen(shooter, feeder, spindexer, rotator)
      );

    rotator.setDefaultCommand(
      TurretCommands.openLoopRotate(
        rotator, 
        () -> -player1.getRightY()
      )
    );

    //pitch.setDefaultCommand(TurretCommands.defaultPitchCommand(pitch));

    player1.rightStick().onTrue(
      TurretCommands.toggleTargeting(rotator, shooter)
    );

    // player2.rightBumper()
    //   .whileTrue(TurretCommands.fullSendCommand(shooter, feeder, spindexer, rotator)
    // );

    // rumble while shooting
    player2.rightBumper()
    .whileTrue(
        TurretCommands.fullSendCommand(shooter, feeder, spindexer, rotator)
            .deadlineWith(
                Commands.startEnd(
                    () -> player2.getHID().setRumble(GenericHID.RumbleType.kBothRumble, 1.0),
                    () -> player2.getHID().setRumble(GenericHID.RumbleType.kBothRumble, 0.0)
                )
            )
    );

    // rotator.setDefaultCommand(
    //   TurretCommands.openLoopRotate(
    //     rotator, 
    //     () -> -player2.getRightY()
    //   )
    // );
    player2.x().onTrue(TurretCommands.spindexCommand(spindexer));
    player2.y().whileTrue(TurretCommands.unjam(spindexer, feeder));

    player2.a()
      .whileTrue(
        IntakeCommands.intakeShake(intake)
      );
    
    player2.b()
      .onTrue(
        IntakeCommands.intakeDeployCommand(intake)
      );
    player2.povUp().whileTrue(ClimbCommands.climbUp(climb));
    player2.povDown().whileTrue(ClimbCommands.climbDown(climb));
    
    // intake.setDefaultCommand(
    //   IntakeCommands.deployIntakeOpenloop(() -> player2.getLeftY(), 
    //   intake)
    // );
    
  }

  //add oomph function
  public void addOomph() {
    double normalizedInput = Math.max((Math.abs(player2.getRightTriggerAxis()) - OperatorConstants.deadband), 0) / (1.0 - OperatorConstants.deadband);
    shooter.addOomph((normalizedInput * normalizedInput) * OperatorConstants.maxOomph);
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
public Command getAutonomousCommand() {
  return autoChooser.getSelected();
}

  private Drive initDrive(){
    return new Drive(new GyroIOPigeon2(),
    new ModuleIOSpark(0),
    new ModuleIOSpark(1),
    new ModuleIOSpark(2),
    new ModuleIOSpark(3));
  }

  public Vision initVision() {
    switch (Constants.currentMode) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        return new Vision(
            drive::addVisionMeasurement,
            new VisionIOLimelight(VisionConstants.camera0Name, drive::getRotation),
            new VisionIOLimelight(VisionConstants.camera1Name, drive::getRotation)
            // new VisionIOPhotonVision(VisionConstants.camera1Name, VisionConstants.robotToCamera1),
            // new VisionIOPhotonVision(VisionConstants.camera2Name, VisionConstants.robotToCamera2)
            );

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        return new Vision(
            drive::addVisionMeasurement,
            new VisionIOPhotonVisionSim(
                VisionConstants.camera0Name, VisionConstants.robotToCamera0, drive::getPose)
            // new VisionIOPhotonVisionSim(
            //     VisionConstants.camera1Name, VisionConstants.robotToCamera1, drive::getPose),
            // new VisionIOPhotonVisionSim(
            //     VisionConstants.camera2Name, VisionConstants.robotToCamera2, drive::getPose)
            );

      default:
        // Replayed robot, disable IO implementations
        return new Vision(drive::addVisionMeasurement, new VisionIO() {}, new VisionIO() {});
    }
  }

  // Check networktables after auto to see if our alliance won auto.  B=blue, R=red, blank=no FMS
  // SmartDashboard key "AutoWinOverride" can be set to "B" or "R" to simulate without a FMS
    public void checkAutoWinAndRumble() {
    // Prefer the SmartDashboard override (for home practice); fall back to FMS data.
    String override = SmartDashboard.getString("AutoWinOverride", "");
    String gameData = (override != null && !override.isEmpty())
        ? override
        : DriverStation.getGameSpecificMessage();

    if (gameData == null || gameData.isEmpty()) {
      return;
    }

    // Read alliance directly rather than relying on the cached field
    Optional<DriverStation.Alliance> currentAlliance = DriverStation.getAlliance();

    boolean weWon = false;
    switch (gameData.charAt(0)) {
      case 'B':
        weWon = currentAlliance.isPresent()
            && currentAlliance.get() == DriverStation.Alliance.Blue;
        break;
      case 'R':
        weWon = currentAlliance.isPresent()
            && currentAlliance.get() == DriverStation.Alliance.Red;
        break;
      default:
        return;
    }

    if (weWon) {
      // Schedule aone time command: full rumble for 5 s, then stop.
      Commands.sequence(
          Commands.runOnce(() ->
              player1.getHID().setRumble(RumbleType.kBothRumble, 1.0)),
          new WaitCommand(3.0),
          Commands.runOnce(() ->
              player1.getHID().setRumble(RumbleType.kBothRumble, 0.0))
      ).ignoringDisable(true).schedule();
    }
  }
}
