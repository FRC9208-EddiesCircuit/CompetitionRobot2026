// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class HoodSubsystem extends SubsystemBase {
  /** Creates a new IntakePivotSubsystem. */
  PIDController pivotPID = new PIDController(1, 0, 0);
  SparkMax hoodMotor = new SparkMax(51, MotorType.kBrushless);
  public HoodSubsystem() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  //This sets the rotations of the hoodMotor
  public void setRotations(double rotations) {
    hoodMotor.set(pivotPID.calculate(hoodMotor.getEncoder().getPosition(), rotations));
  }
  //This sets the position of the hoodMotor
  public void setPosition(double position) {
    hoodMotor.set((position - hoodMotor.getEncoder().getPosition())*0.05);
  }
  //This sets the speed of the hoodMotor
  public void setSpeed(double intakePivotSpeed) {
    hoodMotor.set(intakePivotSpeed);
  }
}
