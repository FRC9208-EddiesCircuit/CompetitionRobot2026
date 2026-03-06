// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;



public class FeederSubsystem extends SubsystemBase {
  /** Creates a new FeederSubsystem. */
  TalonFX feederMotor = new TalonFX(54);
  public FeederSubsystem() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public void setFeederPower(double feederPower) {
    feederMotor.set(feederPower);
  }
  public void stopFeeder() {
    feederMotor.stopMotor();
  }
}
