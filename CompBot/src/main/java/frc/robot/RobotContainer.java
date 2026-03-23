// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.FeedCmd;
import frc.robot.commands.IntakeCmd;
import frc.robot.commands.IntakePivotCmd;
import frc.robot.commands.ReverseIntakeCmd;
import frc.robot.commands.ShootCmd;
import frc.robot.commands.ShootHubCmd;
import frc.robot.commands.TestHoodCmd;
import frc.robot.commands.AutoCommands.AutoPivotDownCmd;
import frc.robot.commands.PivotCmds.PivotDownCmd;
import frc.robot.commands.PivotCmds.PivotMidCmd;
import frc.robot.commands.PivotCmds.PivotUpCmd;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.AgitatorSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.IntakePivotSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
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
  
  private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
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
  //AUTON/PATHPLANNER aka 67

  /* Path follower */
  private final SendableChooser<Command> autoChooser;

  //SUBSYSTEMS
  private final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final IntakePivotSubsystem intakePivotSubsystem = new IntakePivotSubsystem();
  private final AgitatorSubsystem agitatorSubsystem = new AgitatorSubsystem();
  private final FeederSubsystem feederSubsystem = new FeederSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final HoodSubsystem hoodSubsystem = new HoodSubsystem();

  //JOYSTICKS
  private final CommandXboxController controller = new CommandXboxController(0);
  private final Joystick twistJS = new Joystick(1);
  private final Joystick driveJS = new Joystick(2);

  //SWERVE REQUESTS
  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
    .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
    .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
  //apply pids here
    private final SwerveRequest.FieldCentricFacingAngle driveAndAimHub = new SwerveRequest.FieldCentricFacingAngle()
    .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1)
    .withHeadingPID(5,0,0).withDriveRequestType(DriveRequestType.OpenLoopVoltage);
  //Tune the pid here 
  private final SwerveRequest.FieldCentricFacingAngle driveAndPass = new SwerveRequest.FieldCentricFacingAngle()
    .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1)
    .withHeadingPID(5,0,0).withDriveRequestType(DriveRequestType.OpenLoopVoltage);


  public RobotContainer() {
    // Configure the trigger bindings
    autoChooser = AutoBuilder.buildAutoChooser("Test Auto");
    SmartDashboard.putData("Auto Mode", autoChooser);


    NamedCommands.registerCommand("ShootCmd", new ShootCmd(shooterSubsystem, hoodSubsystem, agitatorSubsystem, feederSubsystem, () -> true, () -> false, () -> metersToHub(), () -> metersToPass()));
    NamedCommands.registerCommand("IntakeCmd", new IntakeCmd(intakeSubsystem, agitatorSubsystem, feederSubsystem, () -> 0.7));
    NamedCommands.registerCommand("PivotDownCmd", new PivotDownCmd(intakePivotSubsystem, () -> intakePivotSubsystem.intakeStopped()));
    NamedCommands.registerCommand("AimHubCmd", 
      drivetrain.applyRequest(() ->
        driveAndAimHub
          .withVelocityX(0)
          .withVelocityY(0)
          .withTargetDirection(
            calcHubAngle()
          )
      ).withTimeout(0.67)
    );
    NamedCommands.registerCommand("PivotMidCmd", new PivotMidCmd(intakePivotSubsystem, () -> intakePivotSubsystem.intakeStopped()));
    drivetrain.registerTelemetry(logger::telemeterize);
 
    configureBindings();
  }

  public double getDrivetrainVelocity(){
    return drivetrain.getState().Speeds.vxMetersPerSecond;
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

    new JoystickButton(driveJS, 10).whileTrue(
      drivetrain.runOnce(drivetrain::dontTellCTREImDoingThis)
    );

    //new JoystickButton(driveJS, 6).whileTrue(moveBot());
    
    new JoystickButton(driveJS, 6).whileTrue(//Change button, test if transitions from motion
      autoChooser.getSelected()
    );



/* 
    new JoystickButton(driveJS, 9).whileTrue(
      intakePivotSubsystem.resetIntakePivotEncoder()
    );*/
    //TWISTJS BUTTONS
    new JoystickButton(twistJS, 2).whileTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

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

    new JoystickButton(twistJS, 3).whileTrue(  //find button
      drivetrain.applyRequest(() ->
        driveAndPass
          .withVelocityX(-driveJS.getRawAxis(1) * MaxSpeed)
          .withVelocityY(-driveJS.getRawAxis(0) * MaxSpeed)
          .withTargetDirection(
            calcPassAngle()
          )
      )
    );


    intakePivotSubsystem.setDefaultCommand(
      new IntakePivotCmd(
        intakePivotSubsystem,
        () -> controller.a().getAsBoolean(),
        () -> controller.b().getAsBoolean(),
        () -> controller.axisGreaterThan(2, 0.1).getAsBoolean()
      )
    );

    controller.axisGreaterThan(2, 0.1).whileTrue(
      new IntakeCmd(
        intakeSubsystem, 
        
        agitatorSubsystem,
        feederSubsystem,
        () -> controller.getLeftTriggerAxis()
      )
    );

    controller.y().whileTrue(new ReverseIntakeCmd(intakeSubsystem));



    controller.axisGreaterThan(3, 0.1).whileTrue(
      new ShootCmd(
        shooterSubsystem, 
        hoodSubsystem, 
        agitatorSubsystem, 
        feederSubsystem, 
        () -> twistJS.getRawButton(1), 
        () -> twistJS.getRawButton(3), 
        () -> drivetrain.getState().Pose.getTranslation().getDistance(hubPose.getTranslation()),
        () -> metersToPass()
      )
    );



  }

  /*
   * Calculates the angle at which the hub is to the robot.
   * Used in driveAndAimHub SwerveRequest.
   */

  public Rotation2d calcHubAngle(){
    if (DriverStation.getAlliance().get() == Alliance.Red) {
      angleToHub = new Rotation2d(
        drivetrain.getState().Pose.getX() - hubPose.getX(),//hubPose.getX() - drivetrain.getState().Pose.getX(),
        drivetrain.getState().Pose.getY() - hubPose.getY()//hubPose.getY() - drivetrain.getState().Pose.getY()
      );
      return angleToHub;
    }else if(DriverStation.getAlliance().get() == Alliance.Blue) {
      angleToHub = new Rotation2d(
        hubPose.getX() - drivetrain.getState().Pose.getX(),
        hubPose.getY() - drivetrain.getState().Pose.getY()
      );
      return angleToHub;
    }else{
      return new Rotation2d(0);
    }
  }

  public Rotation2d calcPassAngle(){
    if (DriverStation.getAlliance().get() == Alliance.Red) {
      angleToHub = new Rotation2d(
        drivetrain.getState().Pose.getX() - 14.5,
        0
      );
      return angleToHub;
    }else if(DriverStation.getAlliance().get() == Alliance.Blue) {
      angleToHub = new Rotation2d(
        2.5 - drivetrain.getState().Pose.getX(),
        0
      );
      return angleToHub;
    }else{
      return new Rotation2d(0);
    }
  }
/* 
  public Rotation2d calcHubAngle(){
    angleToHub = new Rotation2d(
        drivetrain.getState().Pose.getX() - hubPose.getX(),//hubPose.getX() - drivetrain.getState().Pose.getX(),
        drivetrain.getState().Pose.getY() - hubPose.getY()//hubPose.getY() - drivetrain.getState().Pose.getY()
    );
    return angleToHub;
  }

  public Rotation2d calcHubAngleBlue(){
    angleToHub = new Rotation2d(
        hubPose.getX() - drivetrain.getState().Pose.getX(),
        hubPose.getY() - drivetrain.getState().Pose.getY()
    );
    return angleToHub;
  }*/

  public double metersToHub(){
    return drivetrain.getState().Pose.getTranslation().getDistance(hubPose.getTranslation());
  }

  public double metersToPass(){
    if (DriverStation.getAlliance().get() == Alliance.Red) {
      return Math.abs(drivetrain.getState().Pose.getX() - 14.5);
    }else if(DriverStation.getAlliance().get() == Alliance.Blue) {
      return Math.abs(2.5 - drivetrain.getState().Pose.getX());
    }else{
      return 0;
    }
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    Command autoCommand = autoChooser.getSelected();
    String autoName = autoCommand.getName();
    PathPlannerAuto auto = new PathPlannerAuto(autoName);
    Pose2d startingPose = auto.getStartingPose();
    drivetrain.resetPose(startingPose);
    return auto;
  }
}
