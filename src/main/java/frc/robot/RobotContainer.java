// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.TurretCommands;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.intake.Intake;
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
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

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
  private final Drive drive;
  private final Intake intake;
  private final Spindexer spindexer;
  private final Rotator rotator;
  private final Shooter shooter;
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController player1 =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  //dashboard inputs
  private final LoggedDashboardChooser<Command> autoChooser;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    drive = initDrive();
    intake = new Intake();
    spindexer = new Spindexer();
    rotator = new Rotator(drive);
    shooter = new Shooter();
    // Configure the trigger bindings
    configureBindings();
    autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());
    configureAutoCommands();
    autoChooser.addOption("path", DriveCommands.followPath(drive, "Example"));
    autoChooser.addOption("path2", DriveCommands.followPath(drive, "Example"));
    NamedCommands.registerCommand("path2", DriveCommands.followPath(drive, "Example"));
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
      () -> -player1.getLeftY(), 
      () -> -player1.getLeftX(), 
      () -> -player1.getRightX())
    );

    //spindexer.setDefaultCommand(TurretCommands.spindex(spindexer));
    player1.x().onTrue(Commands.run(() -> spindexer.toggleRunning(), spindexer));
    player1.y().onTrue(Commands.run(() -> spindexer.toggleDirection(), spindexer));

    //TODO: COMPOSE COMMANDS TO SIMPIFLY THIS 
    player1.a()
      .whileTrue(
        Commands.run(() -> intake.runIntake(-.5), intake)
        .finallyDo((boolean interrupted) -> intake.stopIntake())
      );
      player1.rightBumper().whileTrue(
        Commands.run(() -> shooter.runShooter(), shooter)
        .finallyDo(() -> shooter.stopShooter())
      );
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    //return autoChooser.get();
    //TODO; get autonimous working with auto chooser. Current autonimous was not pulling from the dashboard
    //Current implementation is a workaround
    return new PathPlannerAuto("TestPath");
  }

  private void configureAutoCommand(String name, Command command) {
    autoChooser.addOption(name, command);
    NamedCommands.registerCommand(name, command);
  }

  private void configureAutoCommands() {
    configureAutoCommand("path", DriveCommands.followPath(drive, "Example"));

    configureAutoCommand("TestPath", new PathPlannerAuto("TestPath"));
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
            new VisionIOLimelight(VisionConstants.camera0Name, drive::getRotation)
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
}
