// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.PivotCmds.PivotDownCmd;
import frc.robot.commands.PivotCmds.PivotMidCmd;
import frc.robot.commands.PivotCmds.PivotUpCmd;
import frc.robot.subsystems.IntakePivotSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakePivotCmd extends Command {

  private IntakePivotSubsystem intakePivotSubsystem;
  private Supplier<Boolean> pivotRequest, jiggleRequest, intakeRequest;

  private enum PivotState{
    UP,
    MID,
    DOWN
  }
  private PivotState lastPivotCmd = PivotState.UP;
  private boolean givenCmd = false;
  private boolean runOnce = true;


  public IntakePivotCmd(IntakePivotSubsystem intakePivotSubsystem, Supplier<Boolean> pivotRequest, Supplier<Boolean> jiggleRequest, Supplier<Boolean> intakeRequest) {
    this.intakePivotSubsystem = intakePivotSubsystem;
    this.pivotRequest = pivotRequest;
    this.jiggleRequest = jiggleRequest;
    this.intakeRequest = intakeRequest;

    addRequirements(intakePivotSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {


    if((pivotRequest.get() || jiggleRequest.get())){
      if(((lastPivotCmd == PivotState.DOWN && pivotRequest.get()) || 
          (lastPivotCmd == PivotState.MID && pivotRequest.get())) &&
          !givenCmd
        ){
          lastPivotCmd = PivotState.UP;
          givenCmd = true;
          System.out.println("Pivot up");

          CommandScheduler.getInstance().schedule(
            new PivotUpCmd(intakePivotSubsystem, () -> intakePivotSubsystem.intakeStopped())
          );
      }else if(((lastPivotCmd == PivotState.UP && pivotRequest.get()) ||
                (lastPivotCmd == PivotState.MID && jiggleRequest.get())) &&
                !givenCmd
              ){
          lastPivotCmd = PivotState.DOWN;
          givenCmd = true;
          System.out.println("Pivot down");

          CommandScheduler.getInstance().schedule(
            new PivotDownCmd(intakePivotSubsystem, () -> intakePivotSubsystem.intakeStopped())
          );
      }else if(((lastPivotCmd == PivotState.DOWN && jiggleRequest.get()) ||
                lastPivotCmd == PivotState.UP && jiggleRequest.get()) &&
                !givenCmd
              ){
          lastPivotCmd = PivotState.MID;
          givenCmd = true;
          System.out.println("Pivot mid");
          CommandScheduler.getInstance().schedule(
            new PivotMidCmd(intakePivotSubsystem, () -> intakePivotSubsystem.intakeStopped())
          );
      }
    }
    
    if(intakeRequest.get()){
      intakePivotSubsystem.pivotDownIntake();
      System.out.println("pushing down");
    }    

    givenCmd = false;
  }

  //old way
  /*CommandScheduler.getInstance().schedule(
      intakePivotSubsystem.pivotDown()
        .until(() -> intakePivotSubsystem.intakeStopped())
    );
  */
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
