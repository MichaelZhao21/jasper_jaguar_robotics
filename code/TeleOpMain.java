package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TeleOpMain")
public class TeleOpMain extends LinearOpMode{

    private DcMotor Motor0; //Front Right Wheel Motor
    private DcMotor Motor1; //Back Right Wheel Motor
    private DcMotor Motor2; //Back Left Wheel Motor
    private DcMotor Motor3; //Front Left Wheel Motor
	private DcMotor ArmMotor; //Mineral Arm Up/Down Motor
	private DcMotor FlipMotor; //Mineral Arm Rotation Motor
	private DcMotor LiftMotor; //Robot Up/Down Motor

    @Override
    public void runOpMode() throws InterruptedException{

        int mode = 0; //0(x): Pickup Minerals, 1(y): Lift Robot
        double speed = 0.5;
        Motor0 = hardwareMap.dcMotor.get("Motor0");
        Motor1 = hardwareMap.dcMotor.get("Motor1");
        Motor2 = hardwareMap.dcMotor.get("Motor2");
        Motor3 = hardwareMap.dcMotor.get("Motor3");
        Motor3.setDirection(DcMotor.Direction.REVERSE);
        LiftMotor = hardwareMap.dcMotor.get("LiftMotor");
		FlipMotor = hardwareMap.dcMotor.get("FlipMotor");
        ArmMotor = hardwareMap.dcMotor.get("ArmMotor");

        waitForStart();

        while (opModeIsActive()) {

            //Drive Code
            float M1 = gamepad1.left_stick_y - gamepad1.left_stick_x;
            float M2 = gamepad1.left_stick_y + gamepad1.left_stick_x;

            if (gamepad1.left_trigger > 0 || gamepad1.right_trigger > 0) {

                if (gamepad1.left_trigger > 0 && gamepad1.right_trigger == 0) {
                    pivot(gamepad1.left_trigger, speed);
                } else if (gamepad1.left_trigger == 0 && gamepad1.right_trigger > 0) {
                    pivot(-gamepad1.right_trigger, speed);
                }

            }
            else {

                Motor0.setPower((M1) * speed);
                Motor1.setPower((M2) * speed);
                Motor2.setPower((-M1) * speed);
                Motor3.setPower((-M2) * speed);

            }

            //Changing Wheel Speeds
            if (gamepad1.dpad_up) {
                speed = 0.5;
            }
            else if (gamepad1.dpad_down) {
                speed = 0.2;
            }

            //Changing Secondary Arm Modes
            if (gamepad2.x) {
                mode = 0;
            }
            else if (gamepad2.y) {
                mode = 1;
            }

            //Arm Movement Code
            if (mode == 0) {
                //Mineral Pickup arm
                if (-gamepad2.right_stick_y > 0) {
                    FlipMotor.setPower(-gamepad2.right_stick_y * .5); //Rotate Up
                }
                else{
                    FlipMotor.setPower(-gamepad2.right_stick_y * .3); //Rotate Down
                }

                ArmMotor.setPower(gamepad2.left_stick_y * .2); //Move Arm Up/Down

            }
            else {
                //Robot lifting arm
                LiftMotor.setPower(gamepad2.left_stick_y * .7);
            }

        }
    }

    private void pivot(float target, double multiplier){

        Motor0.setPower(target * multiplier);
        Motor1.setPower(target * multiplier);
        Motor2.setPower(target * multiplier);
        Motor3.setPower(target * multiplier);

    }

}
