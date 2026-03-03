// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import frc.robot.Constants.OperatorConstants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  
  private double MaxSpeed = 0.6 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
  private double MaxAngularRate = 0.7 * RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
  private final Telemetry logger = new Telemetry(MaxSpeed);

  //VISION
  private Rotation2d angleToHub;
  private Pose2d hubPose;
  private Pose2d redHubPose = new Pose2d(
      11.9154194,
      4.0346376,
      new Rotation2d()
  );
  private Pose2d blueHubPose = new Pose2d(
      4.6256194,
      4.0346376,
      new Rotation2d()
  );

  //AUTON/PATHPLANNER

  /* Path follower */
  private final SendableChooser<Command> autoChooser;

  //SUBSYSTEMS
  public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

  //JOYSTICKS
  private final CommandXboxController controller = new CommandXboxController(0);
  private final Joystick twistJS = new Joystick(1);
  private final Joystick driveJS = new Joystick(2);

  //SWERVE REQUESTS
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.FieldCentricFacingAngle driveAndAimHub = new SwerveRequest.FieldCentricFacingAngle()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1)
            .withHeadingPID(1.5,0,0).withDriveRequestType(DriveRequestType.OpenLoopVoltage);


  public RobotContainer() {
    // Configure the trigger bindings
    autoChooser = AutoBuilder.buildAutoChooser("Test Auto");
    SmartDashboard.putData("Auto Mode", autoChooser);

    drivetrain.registerTelemetry(logger::telemeterize);

    configureBindings();

  }

  /*
   * Sets hub pose depending on alliance.
   * Called in Robot.java disabledPeriodic
   */
  public void setHubPose(){
    if (DriverStation.getAlliance().get() == Alliance.Red) {
      hubPose = redHubPose;
    }else if(DriverStation.getAlliance().get() == Alliance.Blue) {
      hubPose = blueHubPose;
    }
  }

  //CONFIGURE BUTTONS
  private void configureBindings() {
    //DEFAULT SWERVE REQUEST
    
    drivetrain.setDefaultCommand(
      // Drivetrain will execute this command periodically
      drivetrain.applyRequest(() ->
        drive.withVelocityX(-driveJS.getRawAxis(1) * MaxSpeed) // Drive forward with negative Y (forward)
          .withVelocityY(-driveJS.getRawAxis(0) * MaxSpeed) // Drive left with negative X (left)
          .withRotationalRate(-twistJS.getRawAxis(2) * MaxAngularRate)
      )
    );
    //DRIVEJS BUTTONS

    new JoystickButton(twistJS, 1).whileTrue(
      drivetrain.applyRequest(() ->
        driveAndAimHub
          .withVelocityX(-driveJS.getRawAxis(1) * MaxSpeed)
          .withVelocityY(-driveJS.getRawAxis(0) * MaxSpeed)
          .withTargetDirection(
            calcHubAngle()
          )
      )
    );
    new JoystickButton(driveJS, 7).whileTrue(//Change button, test if transitions from motion
      AutoBuilder.pathfindToPose(
        new Pose2d(15.18, 4.323, new Rotation2d(0)),//15.18, 4.323
        new PathConstraints(
          3.0, 4.0, Units.degreesToRadians(540), Units.degreesToRadians(720)
        ),
        0
      )
    );
    //TWISTJS BUTTONS
    new JoystickButton(twistJS, 2).whileTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

    //CONTROLLER BUTTONS


  }

  /*
   * Calculates the angle at which the hub is to the robot.
   * Used in driveAndAimHub SwerveRequest.
   */
  public Rotation2d calcHubAngle(){
    angleToHub = new Rotation2d(
        hubPose.getX() - drivetrain.getState().Pose.getX(),
        hubPose.getY() - drivetrain.getState().Pose.getY()
    );

    return angleToHub;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return  autoChooser.getSelected();
  }
}
