// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.IntakePivotSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakePivotCmd extends Command {

  private IntakePivotSubsystem intakePivotSubsystem;
  private Supplier<Boolean> pivotRequest;

  public IntakePivotCmd(IntakePivotSubsystem intakePivotSubsystem, Supplier<Boolean> pivotRequest) {
    this.intakePivotSubsystem = intakePivotSubsystem;
    this.pivotRequest = pivotRequest;

    addRequirements(intakePivotSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(pivotRequest.get()){
      if(intakePivotSubsystem.isUp()){
        CommandScheduler.getInstance().schedule(
          intakePivotSubsystem.pivotDown()
          .onlyWhile(() -> !intakePivotSubsystem.isDown())
          .andThen(intakePivotSubsystem.stopPivot())
        );
      }else if(intakePivotSubsystem.isDown()){
        CommandScheduler.getInstance().schedule(
          intakePivotSubsystem.pivotUp()
          .onlyWhile(() -> !intakePivotSubsystem.isUp())
          .andThen(() -> intakePivotSubsystem.stopPivot())
        );
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
