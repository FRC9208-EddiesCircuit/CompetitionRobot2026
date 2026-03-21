// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;

public class FeederSubsystem extends SubsystemBase {

  private TalonFX feederMotor = new TalonFX(54);
  private DigitalInput leftSensor = new DigitalInput(8);
  private DigitalInput rightSensor = new DigitalInput(9);

  private SlewRateLimiter feederSlewRate = new SlewRateLimiter(0.2); // DutyCycle/Sec
 
  public FeederSubsystem() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public void setFeederPower(double feederPower) {
    feederMotor.set(feederSlewRate.calculate(feederPower));
    //feederMotor.set(feederPower);

  }
  public void stopFeeder() {
    feederMotor.stopMotor();
  }

  public boolean getFeederFull(){
    System.out.println(leftSensor.get() || rightSensor.get());
    return leftSensor.get() || rightSensor.get();
  }
}
