package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="TeleOpOneController")
public class TeleOpOneController extends LinearOpMode{

    private DcMotor Motor0;
    private DcMotor Motor1;
    private DcMotor Motor2;
    private DcMotor Motor3;
    private DcMotor LiftMotor;
    private DcMotor ArmMotor;
    private DcMotor FlipMotor;

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
        ArmMotor = hardwareMap.dcMotor.get("ArmMotor");
        FlipMotor = hardwareMap.dcMotor.get("FlipMotor");

        waitForStart();

        while (opModeIsActive()) {

            //drive code
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

            //Fast/slow modes
            if (gamepad1.dpad_up) {
                speed = 0.7;
            }
            else if (gamepad1.dpad_left) {
                speed = 0.5;
            }
            else if (gamepad1.dpad_down) {
                speed = 0.2;
            }

            //change arm mode bw Lifting Arm and Mineral Pickup Arm
            if (gamepad1.x) {
                mode = 0;
            }
            else if (gamepad1.y) {
                mode = 1;
            }

            //Switch between
            if (mode == 0) {
                //Mineral Pickup arm
                if (gamepad1.right_bumper){
                    ArmMotor.setPower(0);
                    if (gamepad1.right_stick_y > 0) {
                        FlipMotor.setPower(gamepad1.right_stick_y * .3); //down
                    }
                    else if (gamepad1.right_stick_y < 0) {
                        FlipMotor.setPower(gamepad1.right_stick_y * .65); //up
                    }
                    else {
                        FlipMotor.setPower(0); // stop
                    }
                }
                else {
                    FlipMotor.setPower(0);
                    if (gamepad1.right_stick_y > 0) {
                        ArmMotor.setPower(-0.4); //up
                    }
                    else if (gamepad1.right_stick_y < 0){
                        ArmMotor.setPower(0.4); //down
                    }
                    else {
                        ArmMotor.setPower(0);
                    }
                }

            }
            else {
                //Robot lifting arm
                LiftMotor.setPower(-gamepad1.right_stick_y * .7);
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
