// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

//import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class IntakePivotSubsystem extends SubsystemBase {
  /** Creates a new IntakePivotSubsystem. */
  //private final PIDController pivotPID = new PIDController(1, 0, 0);
  private final TalonFX intakePivotMotor = new TalonFX(52);
  private final TalonFXConfiguration intakePivotConfig = new TalonFXConfiguration();
  private final Slot0Configs pivotGains = new Slot0Configs()
    .withKP(0.67) //not placeholder
    .withKS(1)
    .withKV(4)
    .withKG(0.07)
    .withGravityType(GravityTypeValue.Arm_Cosine);
  private final PositionVoltage positionVoltageRequest = new PositionVoltage(0).withSlot(0);

  //find real values
  private final double upRotations = 0;
  private final double downRotations = 7.5; //7.3;

  private final double rotationDeadband = 0.5;

  public IntakePivotSubsystem() {
    intakePivotConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    intakePivotConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    intakePivotConfig.withSlot0(pivotGains);

    intakePivotMotor.getConfigurator().apply(intakePivotConfig);
  }

  @Override
  public void periodic() {}

  public void setRotations(double rotations) {
    intakePivotMotor.setControl(positionVoltageRequest.withPosition(rotations));
  }

  public double getRotations(){
    return intakePivotMotor.getPosition().getValueAsDouble();
  }



  public Command pivotUp(){
    return run(() -> {
      setRotations(upRotations);
    });
  }

  public Command pivotDown(){
    return run(() -> setRotations(downRotations));
  }

  public boolean isUp(){
    return (Math.abs(upRotations - getRotations()) < rotationDeadband);
  }

  public boolean isDown(){
    return (Math.abs(downRotations - getRotations()) < rotationDeadband);
  }

  public Command stopPivot(){
    return run(() -> stopMotor());
  }

  public void stopMotor(){
    intakePivotMotor.stopMotor();
  }

  /* 
  //This sets the rotations of the intakePivotMotor
  public void setRotations(double rotations) {
    //intakePivotMotor.set(pivotPID.calculate(intakePivotMotor.getPosition().getValueAsDouble(), rotations));
  }*/

  //This sets the position of the intakePivotMotor
  public void setPosition(double position) {
    intakePivotMotor.set((position - intakePivotMotor.getPosition().getValueAsDouble()) * 0.05);
  }
  //This sets the speed of the intakePivotMotor
  public void setSpeed(double intakePivotSpeed) {
    intakePivotMotor.set(intakePivotSpeed);
  }
}
