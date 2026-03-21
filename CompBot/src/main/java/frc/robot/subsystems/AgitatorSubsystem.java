// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class AgitatorSubsystem extends SubsystemBase {
  /** Creates a new AgitatorSubsystem. */
  private SparkMax agitatorMotor = new SparkMax(53, MotorType.kBrushless);
  private SparkMaxConfig agitatorConfig = new SparkMaxConfig();
  private SlewRateLimiter agitatorSlewRate = new SlewRateLimiter(0.2);


  public AgitatorSubsystem() {
    //Setting up configurations for the agitatorMotor
    agitatorConfig.idleMode(IdleMode.kCoast);
    agitatorConfig.inverted(false);

    agitatorMotor.configure(agitatorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setAgitatorPower(double agitatorPower) {
    //This method allows for setting the agitatorMotor's speed
    agitatorMotor.set(agitatorSlewRate.calculate(agitatorPower));
    //agitatorMotor.set(agitatorPower);
  }

  public void stopAgitator() {
    //This method allows for stopping the agitatorMotor
    agitatorMotor.stopMotor();
  }
}
