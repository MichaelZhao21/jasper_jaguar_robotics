package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "SpeedTest")

public class SpeedTest extends LinearOpMode{

    private final double speedX = 0;
    private final double speedY = 1;

    private DcMotor Motor1;
    private DcMotor Motor2;
    private DcMotor Motor3;
    private DcMotor Motor4;

    public void runOpMode() throws InterruptedException {

        Motor1 = hardwareMap.dcMotor.get("Motor1");
        Motor2 = hardwareMap.dcMotor.get("Motor2");
        Motor3 = hardwareMap.dcMotor.get("Motor3");
        Motor4 = hardwareMap.dcMotor.get("Motor4");
        Motor2.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        start();

        moveRobot(speedX, speedY);
        Thread.sleep(1000);
        stopRobot();

    }

    private void moveRobot(double powerX, double powerY) {

        double M1 = powerY - powerX;
        double M2 = powerY + powerX;
        Motor1.setPower(-M1 * .7);
        Motor2.setPower(-M2 * .7);
        Motor3.setPower(M1 * .7);
        Motor4.setPower(M2 * .7);
    }
    private void stopRobot() {
        Motor1.setPower(0);
        Motor2.setPower(0);
        Motor3.setPower(0);
        Motor4.setPower(0);
    }

}
