// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutoCommands;

import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.IntakeCmd;
import frc.robot.commands.PivotCmds.PivotDownCmd;
import frc.robot.subsystems.AgitatorSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakePivotSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoIntakeCmd extends Command {

  IntakeSubsystem intakeSubsystem;
  IntakePivotSubsystem intakePivotSubsystem;
  FeederSubsystem feederSubsystem;

  PivotDownCmd pivotDownCmd;

  public AutoIntakeCmd(IntakeSubsystem intakeSubsystem, IntakePivotSubsystem intakePivotSubsystem, FeederSubsystem feederSubsystem) {
    this.intakeSubsystem = intakeSubsystem;
    this.intakePivotSubsystem = intakePivotSubsystem;
    this.feederSubsystem = feederSubsystem;

    addRequirements(intakeSubsystem, intakePivotSubsystem, feederSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pivotDownCmd = new PivotDownCmd(intakePivotSubsystem, () -> intakePivotSubsystem.intakeStopped());
    CommandScheduler.getInstance().schedule(
      pivotDownCmd
    );
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intakeSubsystem.intake();
    if(pivotDownCmd.isFinished()){
      intakePivotSubsystem.pivotDownIntake();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.stopMotor();
    intakePivotSubsystem.stopMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
