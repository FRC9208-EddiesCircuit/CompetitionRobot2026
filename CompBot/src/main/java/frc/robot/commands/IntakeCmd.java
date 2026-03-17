// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import org.opencv.features2d.AgastFeatureDetector;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.AgitatorSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakePivotSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakeCmd extends Command {

  IntakeSubsystem intakeSubsystem;
  IntakePivotSubsystem intakePivotSubsystem;
  AgitatorSubsystem agitatorSubsystem;
  FeederSubsystem feederSubsystem;

  private PreFeedCmd preFeedCmd;

  public IntakeCmd(IntakeSubsystem intakeSubsystem, IntakePivotSubsystem intakePivotSubsystem, AgitatorSubsystem agitatorSubsystem, FeederSubsystem feederSubsystem) {
    this.intakeSubsystem = intakeSubsystem;
    this.intakePivotSubsystem = intakePivotSubsystem;
    this.agitatorSubsystem = agitatorSubsystem;
    this.feederSubsystem = feederSubsystem;

    addRequirements(intakeSubsystem, intakePivotSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    preFeedCmd = new PreFeedCmd(agitatorSubsystem, feederSubsystem);
    if(intakePivotSubsystem.isUp()){
      /*CommandScheduler.getInstance().schedule(
        intakePivotSubsystem.pivotDown()
          .onlyWhile(() -> !intakePivotSubsystem.isDown())
          .andThen(intakePivotSubsystem.stopPivot())
      );*/
    }
    /*CommandScheduler.getInstance().schedule(
      preFeedCmd

    );*/
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intakeSubsystem.intake();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.stopMotor();
    //preFeedCmd.finish();
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
