package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name="TensorAuto")
public class TensorAuto extends LinearOpMode{

    private DcMotor Motor0;
    private DcMotor Motor1;
    private DcMotor Motor2;
    private DcMotor Motor3;
    private DcMotor LiftMotor;
    private DcMotor ArmMotor;
    private Servo MarkerServo;

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = "AW43gwP/////AAAAmYhyz/zuEEVHnvzoxHlLyZItf4ilP0/dinBMnTUxXLYeVNLMHQmuS0m+8deBPobAQUB6JXl9rH3l3VC6eJQdYCL7ucXcYRzIaySgu5Edw18foo+xbQpFci4D7t/gEPkx5bkW8OsMCN8oaHnjJfDsm2yuE7YGWzmDs4NRIi929mQxrBk7BFxhDpDV97bGssofJZ16mCAaBgeIj+IUtW2RfZZ9QNOQRs0l0Nlf6vaFtI8/alOhJPjwpQc9ZXmyjF8Yc83mSOKLW8ei3UsYTzrAlZtYeHPiG4FHuGx6t/OCuN5z3V4sw06bvt7Hi9eYa2MivKl8GXlKppNt6kUPHRNFTVz11vboZTYAAAzafNiXyfNj";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private boolean detected = false;

    @Override
    public void runOpMode() throws InterruptedException{

        Motor0 = hardwareMap.dcMotor.get("Motor0");
        Motor1 = hardwareMap.dcMotor.get("Motor1");
        Motor2 = hardwareMap.dcMotor.get("Motor2");
        Motor3 = hardwareMap.dcMotor.get("Motor3");
        Motor3.setDirection(DcMotor.Direction.REVERSE);
        LiftMotor = hardwareMap.dcMotor.get("LiftMotor");
        ArmMotor = hardwareMap.dcMotor.get("ArmMotor");
        MarkerServo = hardwareMap.servo.get("MarkerServo");
        MarkerServo.setPosition(0);

        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        //start command
        waitForStart();

        start();

        if (tfod != null) {
            tfod.activate();
        }

        //land wheels from lander and raise arm slightly
        ArmMotor.setPower(-.5);
        sleep(500);
        ArmMotor.setPower(0);

        LiftMotor.setPower(-.7);
        sleep(6000);
        LiftMotor.setPower(0);

        //move holding peg away from hook TODO: Make this faster and calculate the timings
        move(0.2,0, -1, 0);
        sleep(800);
        stopMove();
        move(.2,-1,0,0);
        sleep(400);
        stopMove();

        //lower the lift arm
        LiftMotor.setPower(1); //TODO: setPower at 1 and lower time ***TRY 2500 ms
        ArmMotor.setPower(-.2);
        sleep(1500);
        ArmMotor.setPower(0);
        sleep(1500);
        LiftMotor.setPower(0);

        //move to sampling position
        move(.2,0,1,0);
        sleep(1600);
        stopMove();

        String side;
        while (!detected) {
            side = getMinerals();
            telemetry.addData("Gold Side", side);
            telemetry.update();
        }

        sleep(2000);

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

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    private String getMinerals() {
        String side = "None";
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("I SPY", updatedRecognitions.size());
            if (updatedRecognitions.size() == 2) {
                int goldMineralX = -1;
                int silverMineralX = -1;
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                        goldMineralX = (int) recognition.getLeft();
                    } else if (silverMineralX == -1) {
                        silverMineralX = (int) recognition.getLeft();
                    }
                }
                telemetry.addData("GoldX", goldMineralX);
                telemetry.addData("SilverX", silverMineralX);
                if (goldMineralX == -1) {
                    side = "Right";
                } else if (goldMineralX > silverMineralX) {
                    side = "Left";
                } else {
                    side = "Center";
                }
                detected = true;
            }
        }
        return side;
    }

}
