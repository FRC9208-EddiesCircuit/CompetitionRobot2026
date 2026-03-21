// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
//Intake is a x44 kraken pivot is a x60 kraken
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new IntakeSubSystem. */
  private double intakeSpeed = .70;
  private TalonFX intakeMotor = new TalonFX(51);
  private TalonFXConfiguration intakeConfiguration = new TalonFXConfiguration();

    
  public IntakeSubsystem() {
    intakeConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    intakeMotor.getConfigurator().apply(intakeConfiguration);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
  }
  //This method sets the motor speed and starts the x44 intake motor
  public void intake(){
    intakeMotor.set(intakeSpeed);
  }

  public void setSpeed(double intakeSpeed) {
    intakeMotor.set(intakeSpeed);
  }
  //This method stops the x44 intake motor 
  public void stopMotor() {
    intakeMotor.stopMotor();
  }


}
