// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import org.opencv.features2d.AgastFeatureDetector;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakePivotSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ReverseIntakeCmd extends Command {

  IntakeSubsystem intakeSubsystem;
  FeederSubsystem feederSubsystem;
  ReverseFeedCmd reverseFeedCmd;

  public ReverseIntakeCmd(IntakeSubsystem intakeSubsystem, FeederSubsystem feederSubsystem) { //, ReverseFeedCmd reverseFeedCmd) {
    this.intakeSubsystem = intakeSubsystem;
    this.feederSubsystem = feederSubsystem;
    addRequirements(intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    reverseFeedCmd = new ReverseFeedCmd(feederSubsystem);
    CommandScheduler.getInstance().schedule(reverseFeedCmd);
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    intakeSubsystem.setSpeed(-0.7);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.stopMotor();
    reverseFeedCmd.finish();
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
