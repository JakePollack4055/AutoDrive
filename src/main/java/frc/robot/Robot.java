/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private WPI_TalonSRX motor;
  private XboxController controller;
  private WPI_TalonSRX motor2;
  private WPI_TalonSRX motorR1, motorR2, motorR3, motorR4
  private WPI_TalonSRX motorL1, motorL2, motorL3, motorL4;
  private DifferentialDrive drive;
  private DoubleSolenoid PnuematicA;
  private Timer time;
  private DoubleSolenoid Lift;
  private DoubleSolenoid slide;
  private WPI_TalonSRX Hatch;
  private WPI_TalonSRX Cargo;
  private XboxController controller2;
  private SpeedControllerGroup Left;
  private SpeedControllerGroup Right;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    motor = new WPI_TalonSRX(2);

    motor2 = new WPI_TalonSRX(7);
    motorR1 = new WPI_TalonSRX(4);
      motorR2 = new WPI_TalonSRX(5);
      motorR3 = new WPI_TalonSRX( 6);
      motorR4 = new WPI_TalonSRX(7);
    motorL1 = new WPI_TalonSRX(11);
      motorL2 = new WPI_TalonSRX(12);
      motorL3 = new WPI_TalonSRX(13);
      motorL4 = new WPI_TalonSRX(14);
      Right = new SpeedControllerGroup (motorR1, motorR2, motorR3, motorR4);
      Left = new SpeedControllerGroup(motorL1, motorL2, motorL3, motorL4);
    drive = new DifferentialDrive(Left,Right);
    PnuematicA = new DoubleSolenoid(5,1);
    time = new Timer();
    Hatch =new WPI_TalonSRX(10);
    Cargo = new WPI_TalonSRX(8);
    Lift = new DoubleSolenoid(4, 0);
    slide=new DoubleSolenoid(5,1);


  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    time.reset();
    time.start();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    if(time.get()<=5){
     drive.curvatureDrive(1,0, false);
    }
    else if(time.get()<=5.5){
      drive.curvatureDrive(0,0,false);
      Cargo.set(1);
    }

    else if(time.get()<=9){
      drive.curvatureDrive(-1,0,false);
    }
    else if(time.get()<=9.5){
      drive.curvatureDrive(0,0,false);
      Cargo.set(-1);
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    motor.set(controller.getX(GenericHID.Hand.kLeft));
    if(controller.getAButton()){
      motor2.set(1);
    }
    else{

    }
      motor2.set(0);
      drive.curvatureDrive(controller.getTriggerAxis(GenericHID.Hand.kRight) - controller.getTriggerAxis(GenericHID.Hand.kRight), controller.getX(GenericHID.Hand.kLeft), controller.getXButton());
    if(controller.getYButtonPressed()){
      PnuematicA.set(DoubleSolenoid.Value.kForward);
    }
    else if(controller.getBButtonPressed()){
      PnuematicA.set(DoubleSolenoid.Value.kReverse);
    }
    if(controller2.getStartButtonPressed()){
      slide.set(DoubleSolenoid.Value.kForward);
      Lift.set(DoubleSolenoid.Value.kForward);
    }
    if(controller2.getBumperPressed(GenericHID.Hand.kRight)){
      Hatch.set(1);
    }
    else {
      Hatch.set(controller2.getTriggerAxis(GenericHID.Hand.kRight));

    }
    if(controller2.getBumperPressed(GenericHID.Hand.kLeft)) {
      Cargo.set(0);
    }
    else{
      Cargo.set(controller2.getTriggerAxis(GenericHID.Hand.kLeft));

      }
    }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
