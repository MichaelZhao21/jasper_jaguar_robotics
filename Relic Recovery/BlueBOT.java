//HEY LISTEN UP ABBI, this code is from Blue side. All values are working 100% IF POSITIONING ON BALANCE PLATE IS RIGHT.
//If any problems occcur, blame engineering.

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name = "BlueAutoBot")

public class BlueAutoBot extends LinearOpMode {

    public DcMotor Motor1;
    public DcMotor Motor2;
    public DcMotor Motor3;
    public DcMotor Motor4;
    public int var = 0;
    public DcMotor GlyphMotor;
    public DcMotor GlyphMotor2;
    public Servo ServoLeft;
    public Servo ServoRight;
    public Servo jervo;
    public Servo Mini;
    public ColorSensor Color;
    public int relicMove;

    public void moveForward ( double howFast){

        Motor1.setPower(-howFast);
        Motor2.setPower(-howFast);
        Motor3.setPower(howFast);
        Motor4.setPower(howFast);
    }

    public void stopRobo() {
        Motor1.setPower(0);
        Motor2.setPower(0);
        Motor3.setPower(0);
        Motor4.setPower(0);
    }

    public void vuforiaSetUp() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);


        parameters.vuforiaLicenseKey = "AW43gwP/////AAAAmYhyz/zuEEVHnvzoxHlLyZItf4ilP0/dinBMnTUxXLYeVNLMHQmuS0m+8deBPobAQUB6JXl9rH3l3VC6eJQdYCL7ucXcYRzIaySgu5Edw18foo+xbQpFci4D7t/gEPkx5bkW8OsMCN8oaHnjJfDsm2yuE7YGWzmDs4NRIi929mQxrBk7BFxhDpDV97bGssofJZ16mCAaBgeIj+IUtW2RfZZ9QNOQRs0l0Nlf6vaFtI8/alOhJPjwpQc9ZXmyjF8Yc83mSOKLW8ei3UsYTzrAlZtYeHPiG4FHuGx6t/OCuN5z3V4sw06bvt7Hi9eYa2MivKl8GXlKppNt6kUPHRNFTVz11vboZTYAAAzafNiXyfNj";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);


    }

    public static final String TAG = "Vuforia VuMark Sample";
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;

    public void runOpMode() throws InterruptedException {


        Motor1 = hardwareMap.dcMotor.get("Motor1");
        Motor2 = hardwareMap.dcMotor.get("Motor2");
        Motor3 = hardwareMap.dcMotor.get("Motor3");
        Motor4 = hardwareMap.dcMotor.get("Motor4");
        GlyphMotor = hardwareMap.dcMotor.get("Gmotor");
        GlyphMotor2 = hardwareMap.dcMotor.get("Gmotor2");
        ServoLeft = hardwareMap.servo.get("ServoL");
        ServoRight = hardwareMap.servo.get("ServoR");
        jervo = hardwareMap.servo.get("jervo");
        Mini = hardwareMap.servo.get("Mini");
        Color = hardwareMap.colorSensor.get("JARMcl");

        vuforiaSetUp();

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        relicTrackables.activate();


        waitForStart();

        start();

        relicTrackables.activate();
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        Motor2.setDirection(DcMotorSimple.Direction.REVERSE);

        if (vuMark == RelicRecoveryVuMark.LEFT) {

            telemetry.addData("VuMark", "%s visible", vuMark);
            telemetry.addLine(" left detected");
            telemetry.update();

            relicMove = 0;


        } else if (vuMark == RelicRecoveryVuMark.RIGHT) {

            telemetry.addLine("right detected");
            telemetry.addData("VuMark", "%s visible", vuMark);
            telemetry.update();

            relicMove = 2;

        } else if (vuMark == RelicRecoveryVuMark.CENTER) {
            telemetry.addLine("center detected");
            telemetry.addData("VuMark", "%s visible", vuMark);
            telemetry.update();

            relicMove = 1;

        } else {
            telemetry.addLine("failed");
            telemetry.update();

            relicMove = 1;
        }



        //main arm up
        GlyphMotor.setPower(0.5);
        Thread.sleep(600);

        GlyphMotor.setPower(0);
        Mini.setPosition(0.45);
        Thread.sleep(200);

        //servos open
        GlyphMotor2.setPower(0);
        ServoLeft.setPosition(0.8);
        ServoRight.setPosition(0.2);
        Thread.sleep(750);

        //mAIN ARM down
        GlyphMotor.setPower(-0.2);
        Thread.sleep(2000);

        GlyphMotor2.setPower(-0.4);
        Thread.sleep(1650);

        stopRobo();
        GlyphMotor2.setPower(0);
        Thread.sleep(400);

        //closing servos
        ServoLeft.setPosition(0.35);
        ServoRight.setPosition(0.65);
        Thread.sleep(800);

        //Move mini-arms a bit up
        GlyphMotor2.setPower(0.4);
        Thread.sleep(2050);


        //move main arm up
        GlyphMotor2.setPower(0);
        GlyphMotor.setPower(0.35);
        Thread.sleep(1500);

        //keep main arm in one position for auto
        GlyphMotor.setPower(0.07);

        //servo down
        jervo.setPosition(1.0);

        Thread.sleep(1000);













//fix these color sensor values














        if (Color.blue() > 0.1) {

            telemetry.addLine("Blue Detected");
            telemetry.update();

            Mini.setPosition(1.0);
            Thread.sleep(500);

            stopRobo();
            Thread.sleep(500);


        } else if (Color.red() > Color.blue()) {

            telemetry.addLine("Red Detected");
            telemetry.update();

            Mini.setPosition(0.0);
            Thread.sleep(500);

            stopRobo();
            Thread.sleep(500);


        } else {



            telemetry.addLine("Nothing Detected");
            telemetry.update();
            Thread.sleep(2000);

            stopRobo();
            Thread.sleep(500);

        }


        Mini.setPosition(0.45);
        Thread.sleep(500);

        jervo.setPosition(0.4);
        Thread.sleep(1500);


        moveForward(-0.2);
        Thread.sleep(1400);
        stopRobo();
        Thread.sleep(600);


        if(relicMove ==  2){
            Motor1.setPower(0.5);
            Motor2.setPower(0.5);
            Motor3.setPower(0.5);
            Motor4.setPower(0.5);
            Thread.sleep(845); //3500
            stopRobo();

        } else if(relicMove == 1){
            Motor1.setPower(0.5);
            Motor2.setPower(0.5);
            Motor3.setPower(0.5);
            Motor4.setPower(0.5);
            Thread.sleep(950); //3500
            stopRobo();


        } else if(relicMove == 0){
            Motor1.setPower(0.5);
            Motor2.setPower(0.5);
            Motor3.setPower(0.5);
            Motor4.setPower(0.5);
            Thread.sleep(1125); //3500
            stopRobo();
        }


        Thread.sleep(500);
        stopRobo();
        Thread.sleep(500);



            if(relicMove == 1 || relicMove == 0) {
                //deposit glyph
                moveForward(0.2);
                Thread.sleep(2700);

                stopRobo();
                Thread.sleep(750);

                //release glyph and go forward again
                ServoRight.setPosition(0.2);
                ServoLeft.setPosition(0.8);
                moveForward(-0.2);
                Thread.sleep(1000);


                stopRobo();

                //go back
                moveForward(0.2);
                Thread.sleep(450);
                GlyphMotor.setPower(0);

                stopRobo();
                Thread.sleep(500);

            } else if (relicMove == 2) {
                //deposit glyph
                moveForward(0.2);
                Thread.sleep(2700);

                stopRobo();
                Thread.sleep(750);

                //release glyph and go forward again
                ServoRight.setPosition(0.2);
                ServoLeft.setPosition(0.8);
                moveForward(-0.2);
                Thread.sleep(500);


                stopRobo();

                //go back
                moveForward(0.2);
                Thread.sleep(1000);

                stopRobo();

                moveForward(-0.2);
                Thread.sleep(200);
                GlyphMotor.setPower(0);

                stopRobo();
                Thread.sleep(500);


            }



        }


    }



