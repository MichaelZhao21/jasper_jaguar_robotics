package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="FinalAuto")
public class FinalAuto extends LinearOpMode{

    private DcMotor Motor0;
    private DcMotor Motor1;
    private DcMotor Motor2;
    private DcMotor Motor3;
    private DcMotor LiftMotor;
    private Servo MarkerServo;
    private ColorSensor Picker;

    @Override
    public void runOpMode() {

        Motor0 = hardwareMap.dcMotor.get("Motor0");
        Motor1 = hardwareMap.dcMotor.get("Motor1");
        Motor2 = hardwareMap.dcMotor.get("Motor2");
        Motor3 = hardwareMap.dcMotor.get("Motor3");
        Motor3.setDirection(DcMotor.Direction.REVERSE);
        LiftMotor = hardwareMap.dcMotor.get("LiftMotor");
        MarkerServo = hardwareMap.servo.get("MarkerServo");
        Picker.argb();

        MarkerServo.setPosition(0);
        waitForStart();

        start();

        //land right wheels from lander
        LiftMotor.setPower(-.7);
        sleep(4000);
        LiftMotor.setPower(0);

        //Move so that both wheels touch the ground
        move(0.2, 0,1,0);
        sleep(2000);
        stopMove();

        //move holding peg away from hook
        move(0.2,0, -1, 0);
        sleep(500);
        stopMove();

        //lower the lift arm whilst pivoting towards the wall
        LiftMotor.setPower(.7);
        move(0.2,1,0, 0);
        sleep(200);
        stopMove();
        sleep(1000);
//        move(.3, 0,0,1); //Im removing this line for now, but its supposed to pivot
        sleep(2300);
        LiftMotor.setPower(0);
        stopMove();

    }

    /**
     * Logs "caption:value" to the phone
     *
     * @param caption before the value
     * @param value that is displayed
     */
    private void tele(String caption, String value) {
        telemetry.addData(caption, value);
        telemetry.update();
    }

    /**
     * The move function makes the robot move or pivot
     *
     * @param speed how fast the robot moves
     * @param dx change in x
     * @param dy change in y
     * @param pivot 0 means it moves normally, 1 = left turn, -1 = right turn
     */
    private void move(double speed, double dx, double dy, int pivot) {
        if (pivot == 0) {
            float M1 = (float)(dy - dx);
            float M2 = (float)(dy + dx);
            Motor0.setPower((M1) * speed);
            Motor1.setPower((M2) * speed);
            Motor2.setPower((-M1) * speed);
            Motor3.setPower((-M2) * speed);
        }
        else {
            pivot *= speed;
            Motor0.setPower(pivot);
            Motor1.setPower(pivot);
            Motor2.setPower(pivot);
            Motor3.setPower(pivot);
            tele("pivot", Integer.toString(pivot));
        }
    }

    /**
     * stops movement
     */
    private void stopMove() {
        move(0,0,0,0);
    }

}
