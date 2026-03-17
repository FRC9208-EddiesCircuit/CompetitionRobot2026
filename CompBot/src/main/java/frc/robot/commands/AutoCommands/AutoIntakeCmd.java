// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutoCommands;

import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.IntakeCmd;
import frc.robot.subsystems.AgitatorSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakePivotSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoIntakeCmd extends Command {

  IntakeSubsystem intakeSubsystem;
  IntakePivotSubsystem intakePivotSubsystem;
  AgitatorSubsystem agitatorSubsystem;
  FeederSubsystem feederSubsystem;

  public AutoIntakeCmd(IntakeSubsystem intakeSubsystem, IntakePivotSubsystem intakePivotSubsystem, AgitatorSubsystem agitatorSubsystem, FeederSubsystem feederSubsystem) {
    this.intakeSubsystem = intakeSubsystem;
    this.intakePivotSubsystem = intakePivotSubsystem;
    this.agitatorSubsystem = agitatorSubsystem;
    this.feederSubsystem = feederSubsystem;

    addRequirements(intakeSubsystem, intakePivotSubsystem, agitatorSubsystem, feederSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    CommandScheduler.getInstance().schedule(
      new IntakeCmd(intakeSubsystem, intakePivotSubsystem, agitatorSubsystem, feederSubsystem)
        .withTimeout(6.7)
    );
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
