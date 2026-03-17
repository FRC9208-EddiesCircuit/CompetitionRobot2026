// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AgitatorSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShootHubCmd extends Command {

  private ShooterSubsystem shooterSubsystem;
  private HoodSubsystem hoodSubsystem;

  public ShootHubCmd(
    ShooterSubsystem shooterSubsystem,
    HoodSubsystem hoodSubsystem
  ) {
    this.shooterSubsystem = shooterSubsystem;
    this.hoodSubsystem = hoodSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //shooterSubsystem.testLeftShooter(0.067);
    //shooterSubsystem.testRightShooter(0.067);

    shooterSubsystem.setShooter(0.60);//needs testing
    //shooterSubsystem.setShooterVelocity(67);//Bang bang contoller perchance

    //hoodSubsystem.adjustToHub(67);//not used yet, needs regression
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterSubsystem.setShooter(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
