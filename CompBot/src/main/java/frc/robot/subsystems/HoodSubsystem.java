// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkClosedLoopController;

public class HoodSubsystem extends SubsystemBase {
  /** Creates a new IntakePivotSubsystem. */
  private PIDController pivotPID = new PIDController(0.3, 0, 0);
  private SparkMax hoodMotor = new SparkMax(57, MotorType.kBrushless);
  private SparkMaxConfig hoodConfig = new SparkMaxConfig();
  private SparkClosedLoopController sparkPID;// = hoodMotor.getClosedLoopController();

  private double defaultHoodRotations = 0;
  private double hoodRotations;
  private double testHoodRotations = 0;

  private double distance;

  public HoodSubsystem() {
    hoodConfig.idleMode(IdleMode.kBrake);
    hoodConfig.inverted(true);
    hoodConfig.closedLoop.p(0.35);
    //hoodConfig.closedLoop.p(0.3);

    hoodMotor.configure(hoodConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    sparkPID = hoodMotor.getClosedLoopController();

  }

  @Override
  public void periodic() {
  }

  //This sets the rotations of the hoodMotor
  public void setRotations(double rotations) {
    sparkPID.setSetpoint(rotations, ControlType.kPosition);
  }
  //This sets the position of the hoodMotor
  public void setPosition(double position) {
    hoodMotor.set((position - hoodMotor.getEncoder().getPosition()) * 0.05);
  }

  public void testHood(){
    setRotations(this.testHoodRotations);
  }

  public double getTestHoodRotations(){
    return this.testHoodRotations;
  }

  //This sets the speed of the hoodMotor
  public void setSpeed(double intakePivotSpeed) {
    hoodMotor.set(intakePivotSpeed);
  }

  public double getPosition(){
    return hoodMotor.getEncoder().getPosition();
  }
  public void adjustToHub(double metersToHub){

    if(metersToHub < 1.85){
      hoodRotations = 0;
    }else if(metersToHub >= 1.85 && metersToHub < 2.53){
      hoodRotations = 0;
    }else if(metersToHub >= 2.53 && metersToHub < 3.12){
      hoodRotations = 3.74197 * metersToHub - 9.43769;
    }else if(metersToHub >= 3.12 && metersToHub < 3.6){
      hoodRotations = 4.19842 * metersToHub - 13.0878;
    }else if(metersToHub >= 3.6){
      hoodRotations = 1.69201 * metersToHub - 5.02354;
    }

    setRotations(hoodRotations);
  }

  public void adjustToPass(double metersToPass){
    if(metersToPass < 1.85){
      hoodRotations = 0;
    }else if(metersToPass >= 1.85 && metersToPass < 2.53){
      hoodRotations = 0;
    }else if(metersToPass >= 2.53 && metersToPass < 3.12){
      hoodRotations = 3.74197 * metersToPass - 9.43769;
    }else if(metersToPass >= 3.12 && metersToPass < 3.6){
      hoodRotations = 4.19842 * metersToPass - 13.0878;
    }else if(metersToPass >= 3.6){
      hoodRotations = 1.69201 * metersToPass - 5.02354;
    }

    setRotations(hoodRotations);
  }

  public void adjustToDefaultPosition(){
    setRotations(defaultHoodRotations);
  }
}
