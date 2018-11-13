package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="SkimAutoDepot")
public class SkimAutoDepot extends LinearOpMode{

    private DcMotor Motor0;
    private DcMotor Motor1;
    private DcMotor Motor2;
    private DcMotor Motor3;
    private DcMotor LiftMotor;
    private Servo MarkerServo;
    final private long dy = 36; //28 in/sec
    final private long dx = 59; //17 in/sec
    final private long dh = 8; //135 deg/sec

    @Override
    public void runOpMode() throws InterruptedException{

        Motor0 = hardwareMap.dcMotor.get("Motor0");
        Motor1 = hardwareMap.dcMotor.get("Motor1");
        Motor2 = hardwareMap.dcMotor.get("Motor2");
        Motor3 = hardwareMap.dcMotor.get("Motor3");
        Motor3.setDirection(DcMotor.Direction.REVERSE);
        LiftMotor = hardwareMap.dcMotor.get("LiftMotor");
        MarkerServo = hardwareMap.servo.get("MarkerServo");
        MarkerServo.setPosition(0);

        waitForStart();

        start();

        //land wheels from lander
        LiftMotor.setPower(-.7);
        sleep(4750);
        LiftMotor.setPower(0);

        //move holding peg away from hook TODO: Make this faster and calculate the timings
        move(0.2,0, -1, 0);
        sleep(800);
        stopMove();
        move(.2,-1,0,0);
        sleep(200);
        stopMove();

        //lower the lift arm
        LiftMotor.setPower(.7); //TODO: setPower at 1 and lower time ***TRY 2500 ms
        sleep(3500);
        LiftMotor.setPower(0);

        //pivot to face wall
        move(.8, 0,0,1);
        sleep(dh * 45);
        stopMove();

        //Move to wall
        move(.8,1,0,0);
        sleep(dx * 36);
        stopMove();

        //Move to depot, hitting the mineral on the way
        move(.8,0,-1,0);
        sleep(dy * 48);
        stopMove();

        //pivot 90 deg cc and deposit team marker
        move(.8,0,0,1);
        sleep(dh * 90);
        stopMove();
        MarkerServo.setPosition(1);
        sleep(100);

        //Move forward into crater
        move(.8,0,1,0);
        sleep(dx * 108);
        stopMove();

    }

    /**
     * The move function makes the robot move or pivot
     *
     * @param speed how fast the robot moves
     * @param dx change in x
     * @param dy change in y
     * @param pivot 0 means it moves normally, 1 = left turn, -1 = right turn
     */
    private void move(double speed, double dx, double dy, double pivot) {
        if (pivot == 0) {
            dy = -dy;
            dx = -dx;
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
        }
    }

    /**
     * stops movement
     */
    private void stopMove() {
        move(0,0,0,0);
    }

}
