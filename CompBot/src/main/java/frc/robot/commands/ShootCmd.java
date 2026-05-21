// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterSubsystem.shotState;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShootCmd extends Command {

  private ShooterSubsystem shooterSubsystem;
  private HoodSubsystem hoodSubsystem;
  private FeederSubsystem feederSubsystem;
  private Supplier<Boolean> hubShot, passShot;
  private Supplier<Double> metersToHub, metersToPass;
  private shotState currentState;

  private FeedCmd feedCmd;// = new FeedCmd(agitatorSubsystem, feederSubsystem);

  //private double shooterSpeed = 0.55;
  private double defaultShooterVelocity = 42.5;
  private double shooterVelocity = defaultShooterVelocity; //RPS
  private double calculatedVelocity;
  private boolean isFeeding = false;

  private double meters;


  public ShootCmd(
    ShooterSubsystem shooterSubsystem,
    HoodSubsystem hoodSubsystem, 
    FeederSubsystem feederSubsystem, 
    Supplier<Boolean> hubShot, Supplier<Boolean> passShot, 
    Supplier<Double> metersToHub, 
    Supplier<Double> metersToPass) 
  {
    this.shooterSubsystem = shooterSubsystem;
    this.hoodSubsystem = hoodSubsystem;
    this.feederSubsystem = feederSubsystem;
    this.hubShot = hubShot;
    this.passShot = passShot;
    this.metersToHub = metersToHub;
    this.metersToPass = metersToPass;

    addRequirements(shooterSubsystem, hoodSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    feedCmd = new FeedCmd(feederSubsystem);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //meters = hoodSubsystem.getTestHoodRotations() + 0.3495;
    currentState = shotState.DEMO; // Hardcoded Demo branch override
    double demoVelocity = 35.0;

    // if(hubShot.get()){
    //   currentState = shotState.HUB;
    // }else if(passShot.get()){
    //   currentState = shotState.PASS;
    // }else{
    //   currentState = shotState.DEFAULT;
    // }

    switch (currentState) {
      case DEMO:
        // hardcoded above for memorial day demo branch
        hoodSubsystem.setRotations(0);
        shooterVelocity = demoVelocity; // low velocity for small space
      break;
      case HUB:
        hoodSubsystem.adjustToHub(metersToHub.get());
        shooterVelocity = adjustShooterVelocity(metersToHub.get());//(meters);
        break;
      case PASS:
        hoodSubsystem.adjustToPass(metersToPass.get());
        shooterVelocity = adjustShooterVelocity(metersToPass.get());
        break;
      case DEFAULT:
        hoodSubsystem.adjustToHub(metersToHub.get());
        shooterVelocity = defaultShooterVelocity;
        break;    
      default:
        break;
    }

    shooterSubsystem.setShooterVelocity(shooterVelocity);

    if(shooterSubsystem.getShooterVelocity() >= shooterVelocity && !isFeeding){//shooterSubsystem.getShooterVelocity() >= shooterVelocity &&
      isFeeding = true;
      CommandScheduler.getInstance().schedule(feedCmd);
    }
    
  }

  public double adjustShooterVelocity(double metersToHub){
    if(metersToHub < 1.85){
      calculatedVelocity = 42.5;
    }else if(metersToHub >= 1.85 && metersToHub < 2.53){
      calculatedVelocity = 8.59005 * metersToHub + 28.34849;
    }else if(metersToHub >= 2.53 && metersToHub < 3.12){
      calculatedVelocity = 50;//calculatedVelocity = 50 * 1.2;//calculatedVelocity = 50;
    }else if(metersToHub >= 3.12 && metersToHub < 3.6){
       calculatedVelocity = 60;//calculatedVelocity = 60 * 1.6;//calculatedVelocity = 60;
    }else if(metersToHub >= 3.6){
       calculatedVelocity = 65;//calculatedVelocity = 65 * 1.3;//calculatedVelocity = 65;
    }
    return calculatedVelocity;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    hoodSubsystem.setRotations(0); 
    //hoodSubsystem.adjustToDefaultPosition();

    shooterSubsystem.stopMotors();
    feedCmd.finish();
    isFeeding = false;

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
