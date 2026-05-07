// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutoCommands;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.ShootCmd;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterSubsystem.shotState;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoShootCmd extends Command {

  private ShooterSubsystem shooterSubsystem;
  private HoodSubsystem hoodSubsystem;
  private FeederSubsystem feederSubsystem;
  private Supplier<Double> metersToHub, metersToPass;
  private shotState currentState;

  public AutoShootCmd(
    ShooterSubsystem shooterSubsystem,
    HoodSubsystem hoodSubsystem, 
    FeederSubsystem feederSubsystem, 
    Supplier<Double> metersToHub, 
    Supplier<Double> metersToPass
  ){
    this.shooterSubsystem = shooterSubsystem;
    this.hoodSubsystem = hoodSubsystem;
    this.feederSubsystem = feederSubsystem;
    this.metersToHub = metersToHub;
    this.metersToPass = metersToPass;

    addRequirements(shooterSubsystem, hoodSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    CommandScheduler.getInstance().schedule(
      new ShootCmd(shooterSubsystem, hoodSubsystem, feederSubsystem, () -> true, () -> false, metersToHub, metersToPass)
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
