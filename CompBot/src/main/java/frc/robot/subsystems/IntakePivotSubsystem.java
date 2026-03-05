// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.core.CoreTalonFX;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;

public class IntakePivotSubsystem extends SubsystemBase {
  /** Creates a new IntakePivotSubsystem. */
  PIDController pivotPID = new PIDController(1, 0, 0);
  TalonFX intakePivotMotor = new TalonFX(52);
  public IntakePivotSubsystem() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  //This sets the rotations of the intakePivotMotor
  public void setRotations(double rotations) {
    intakePivotMotor.set(pivotPID.calculate(intakePivotMotor.getPosition().getValueAsDouble(), rotations));
  }
  //This sets the position of the intakePivotMotor
  public void setPosition(double position) {
    intakePivotMotor.set((position - intakePivotMotor.getPosition().getValueAsDouble())*0.05);
  }
  //This sets the speed of the intakePivotMotor
  public void setSpeed(double intakePivotSpeed) {
    intakePivotMotor.set(intakePivotSpeed);
  }
}
