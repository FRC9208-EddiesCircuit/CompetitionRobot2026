// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.RPM;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {

  private final TalonFX lShooterLeader = new TalonFX(55);
  private final TalonFX rShooterFollower = new TalonFX(56);

  private final TalonFXConfiguration leftConfig = new TalonFXConfiguration();
  private final TalonFXConfiguration rightConfig = new TalonFXConfiguration();

  private final BangBangController shooterController = new BangBangController(3);
  private final SlewRateLimiter shooterSlewRate = new SlewRateLimiter(0.75);

  private double kMultiplier = 1.1;

  public enum shotState {
    HUB,
    PASS,
    DEFAULT
  }
    
  /** Creates a new ShooterSubsystem. */
  public ShooterSubsystem() {
    leftConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    leftConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

    rightConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    rightConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

    lShooterLeader.getConfigurator().apply(leftConfig);
    rShooterFollower.getConfigurator().apply(rightConfig);

    //TEST IF WORKS
    rShooterFollower.setControl(new Follower(lShooterLeader.getDeviceID(), MotorAlignmentValue.Opposed));
    SmartDashboard.putNumber("multiplier", 1.2);
  }

  /* only used for testing, do not use */
  public void testRightShooter(double rightSpeed){
    rShooterFollower.set(rightSpeed);
  }
  public void testLeftShooter(double leftSpeed){
    lShooterLeader.set(leftSpeed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    kMultiplier = SmartDashboard.getNumber("multiplier", 1.1);
  }

  public void setShooter(double shooterSpeed){
    //lShooterLeader.set(shooterSpeed);
    if(getShooterVelocity() < 40){
      lShooterLeader.set(shooterSlewRate.calculate(shooterSpeed));
    }else{
      lShooterLeader.set(shooterController.calculate(shooterSpeed));
    }
  }

  public void stopMotors(){
    lShooterLeader.stopMotor();
  }

  public double getShooterVelocity(){
    return lShooterLeader.getVelocity().getValueAsDouble();
  }

  //BANG BANG VELOCITY CONTROL (Needs testing)
  public void setShooterVelocity(double RPMs){
    RPMs *= kMultiplier;
    lShooterLeader.set(
      shooterController.calculate(
        lShooterLeader.getVelocity().getValueAsDouble(),
        RPMs
      )
    );
  }





  
}
