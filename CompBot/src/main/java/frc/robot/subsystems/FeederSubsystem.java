// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

public class FeederSubsystem extends SubsystemBase {

  private TalonFX indexerLeader = new TalonFX(54);
  private TalonFX indexFollower = new TalonFX(53);
  private DigitalInput leftSensor = new DigitalInput(8);
  private DigitalInput rightSensor = new DigitalInput(9);

  private SlewRateLimiter feederSlewRate = new SlewRateLimiter(0.2); // DutyCycle/Sec
 
  public FeederSubsystem() {
    indexFollower.setControl(new Follower(indexerLeader.getDeviceID(), MotorAlignmentValue.Aligned));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public void setFeederPower(double feederPower) {
    indexerLeader.set(feederSlewRate.calculate(feederPower));
    //feederMotor.set(feederPower);

  }
  public void stopFeeder() {
    indexerLeader.stopMotor();
  }

  public boolean getFeederFull(){
    System.out.println(leftSensor.get() || rightSensor.get());
    return leftSensor.get() || rightSensor.get();
  }
}

/*
 * Inital time:46.95
 * Final time:49.01
 * Time from shoot to score: 2.06
 * Time we want: 1.8
 */
