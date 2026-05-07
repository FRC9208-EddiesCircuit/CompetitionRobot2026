// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
//import frc.robot.subsystems.AgitatorSubsystem;
import frc.robot.subsystems.FeederSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class PreFeedCmd extends Command {
  //private AgitatorSubsystem agitatorSubsystem;
  private FeederSubsystem feederSubsystem;
  private boolean finish = false;

  public PreFeedCmd(FeederSubsystem feederSubsystem) {
    //this.agitatorSubsystem = agitatorSubsystem;
    this.feederSubsystem = feederSubsystem;
    addRequirements(feederSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //agitatorSubsystem.setAgitatorPower(0.41);
    feederSubsystem.setFeederPower(0.05);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    feederSubsystem.stopFeeder();
  }

  
  public void finish(){
    finish = true;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !feederSubsystem.getFeederFull() || finish;
  }
}
