package frc.robot.subsystems.LED;

import static frc.robot.subsystems.LED.LEDConstants.*;

import org.ejml.equation.Variable;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.intake.Intake;

public class LED extends SubsystemBase {
    private final Spark blinkin;
    private final Intake intake;
    private double teamColor;

    public LED(Intake intake) {
        blinkin = new Spark(blinkinPWMPort);
        this.intake = intake;
    }

    public void setPattern(double value) {
        blinkin.set(value);
    }

    @Override
    public void periodic() {
        if (intake.getIsRunning()) {
            setPattern(solidBlue);
        } else {
            setPattern(solidRed);
        }
    }
    Variable alliance = DriverStation.getAlliance();
    if (alliance.isPresent() && alliance.get() == Alliance.Red){
        teamColor = solidBlue;
    }
    else {
        teamColor = solidRed;
    }
}