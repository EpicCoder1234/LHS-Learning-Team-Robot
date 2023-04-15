package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
//left stick and right stick move the robot
//rt moves the arm up
//lt moves the arm down
//y set claw servo to 0
//x set claw servo to 0.5
//dpad left, right, up, down  is the servo position
@TeleOp
public class MecanumTeleOp extends LinearOpMode {
  public DcMotor motorFrontLeft = null;
  public DcMotor motorBackLeft = null;
  public DcMotor motorFrontRight = null;
  public DcMotor motorBackRight = null;
  public DcMotor pully = null;
  public Servo servoHeight = null;
  public Servo servoRight = null;
  public Servo servoLeft = null;
  @Override
  public void runOpMode() throws InterruptedException {
    motorFrontLeft = hardwareMap.dcMotor.get("FL");
    motorBackLeft = hardwareMap.dcMotor.get("BL");
    motorFrontRight = hardwareMap.dcMotor.get("FR");
    motorBackRight = hardwareMap.dcMotor.get("BR");
    pully = hardwareMap.dcMotor.get("pulley");
    servoHeight = hardwareMap.servo.get("CH");
    //left and right claw servos
    servoRight = hardwareMap.servo.get("CR");
    servoLeft = hardwareMap.servo.get("CL");

    motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

    waitForStart();
    telemetry.addData("Status", "Running");
    telemetry.update();

    if (isStopRequested()) return;

    while (opModeIsActive()) {
      this.update();
    }
  }

  void update() {
    
    int PS = 100;
    double y = -gamepad1.left_stick_y;
    double x = gamepad1.left_stick_x;
    double rx = gamepad1.right_stick_x;
    double ry = gamepad1.right_stick_y;
    //Continuing main robot code
    float Rt = gamepad1.right_trigger;
    float Lt = gamepad1.left_trigger;
    boolean Rb = gamepad1.right_bumper;
    boolean Lb = gamepad1.left_bumper;
    boolean DPL = gamepad1.dpad_left;
    boolean DPR = gamepad1.dpad_right;
    boolean DPU = gamepad1.dpad_up;
    boolean DPD = gamepad1.dpad_down;
    //2 lines under is testing stuff
    //Use servos to use the pulley and help the arm movement
    if (Rb) {
      // move to 0 degrees.
      servoRight.setPosition(0);
      servoLeft.setPosition(1);
      while (!Lb) {
        Rb = true;
      }
    } else if (Lb) {
      // move to 90 degrees.
      servoLeft.setPosition(0.5);
      servoRight.setPosition(0.5);
      while (!Rb) {
        Lb = true;
      }
    }
    //claw positions
    /* IMPORTANT
           the actuall motor degrees is not knows, the degrees here are
           just placeholders. There are 3 arm stages; 0 degrees, 90 and 180
        */

    if (DPL) {
      //activate stage 2
      servoHeight.setPosition(0.2);
    }
    if (DPR) {
      //activate stage 3
      servoHeight.setPosition(0.4);
    }
    if (DPU) {
      //activate stage 1
      servoHeight.setPosition(0.7);
    }
    if (DPD) {
      //activate stage 1
      servoHeight.setPosition(1);
    }

    double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
    double frontLeftPower = -(y + x + rx) / denominator;
    double backLeftPower = -(y - x + rx) / denominator;
    backLeftPower = backLeftPower * 117 / 223;
    double frontRightPower = (y - x - rx) / denominator;
    double backRightPower = (y + x - rx) / denominator;
    backRightPower = backRightPower * 117 / 223;
    double pulleyPower = (Rt - Lt) * PS / 435;
    /*
    double frontLeftPower = 
    */
    //Back RPM: 223
    //Front RPM: 117
    motorFrontLeft.setPower(frontLeftPower);
    motorBackLeft.setPower(backLeftPower);
    motorFrontRight.setPower(frontRightPower);
    motorBackRight.setPower(backRightPower);
    pully.setPower(pulleyPower);
    
    telemetry.addData("y", y);
    telemetry.update();
  }
}

/* 
Among us 
*/
